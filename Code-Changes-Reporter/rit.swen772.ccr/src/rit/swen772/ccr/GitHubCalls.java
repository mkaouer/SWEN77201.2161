package rit.swen772.ccr;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Release;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.UserStargazer;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.GitHubService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.StargazerService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.service.WatcherService;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("deprecation")
public class GitHubCalls {
	private GitHubClient client;
	private RepositoryService service;
	private RepositoryId repoID;
	private CommitService cService;
	private StargazerService starService;
	private WatcherService watcherService;
	private UserService uService;
	private String userName;
	private String project;
	private Repository repository;
	private List<Repository> repositories;
	private List<Release> releases;
	private List<RepositoryCommit> commits;
	private List<RepositoryBranch> branches;
	private List<RepositoryTag> tags;
	private List<Repository> forks;
	private List<Issue> issues;
	private IssueService iService;
	private ContributorCommit contributorCommit;
	private Map<String, JsonObject> jsonReleases;
	private JsonArray jsonContributors;
	private JavaCodeParser javaCodeParser;
	
	//CONSTANTS
	public static final String separator = File.separator; 
	public static final String currentDirectoryPath = Paths.get("").toAbsolutePath().toString() + separator;
	public static final String releaseDownloadsPath = currentDirectoryPath + "ReleaseDownloads";
	private String repositoryDownloadPath;
	private final int BUFFER_SIZE = 4096;
	private final Gson gson = new Gson();
	private final JsonParser parser = new JsonParser();
	
	public GitHubCalls(String token, String userName, String project){
		this.userName = userName;
		this.project = project;
		this.repository = null;
		javaCodeParser = new JavaCodeParser();
		this.client = new GitHubClient();
		this.client.setOAuth2Token(token);
		this.service = new RepositoryService(this.client);
		this.repoID = new RepositoryId(this.userName, this.project);
		this.cService = new CommitService(this.client);
		this.starService = new StargazerService(this.client);
		this.watcherService = new WatcherService(this.client);
		this.iService = new IssueService();
		this.uService = new UserService(this.client);
		
		this.repositories = null;
		this.releases = null;
		this.commits = null;
		this.branches = null;
		this.tags = null;
		this.forks = null;
		this.issues = null;
	}
	
	public String getJSONReleases(String token) {
		String result = null;
		try {
			if (this.jsonReleases == null) {
				this.jsonReleases = new HashMap<>();
			}
			this.getReleases();
			this.getCommitsInfo();
			this.makeCallsOnContributorCommit(token);

			Map<String,Integer> forks = this.getNumberOfForksPerRelease();
			Map<String,Integer> branches = this.getNumberOfBranchesPerRelease();
			Map<String,Integer> stars = this.getNumberOfStarsPerRelease();
			Map<String,Integer> issues = this.getTotalNumberOfIssuesPerRelease();
			
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for(Release release : this.releases){
				JsonObject releaseJSON = new JsonObject();
				releaseJSON.addProperty("tag_name", release.getTagName());
				releaseJSON.addProperty("created_at", formatter.format(release.getCreatedAt()));
				releaseJSON.addProperty("commits_per_release", this.contributorCommit.getCommitsPerRelease().get(release.getTagName()).size());;
				releaseJSON.addProperty("loc_release_additions", this.contributorCommit.getReleaseStats().get(release.getTagName()).get(0));
				releaseJSON.addProperty("loc_release_deletions", this.contributorCommit.getReleaseStats().get(release.getTagName()).get(1));
				releaseJSON.addProperty("release_forks",forks.get(release.getTagName()));
				releaseJSON.addProperty("release_branches", branches.get(release.getTagName()));
				releaseJSON.addProperty("release_stars", stars.get(release.getTagName()));
				releaseJSON.addProperty("release_issues", issues.get(release.getTagName()));
				this.downloadSourceCode(release);
				
				Counter parserMetricsCounter = javaCodeParser.getMetrics(release, this.getRepositoryDownloadPath());
				releaseJSON.addProperty("total_number_methods",parserMetricsCounter.getNumberOfMethodsPerRelease());
				releaseJSON.addProperty("avg_number_methods_per_class", parserMetricsCounter.getAvgNumberOfMethodsPerRelease());
				releaseJSON.addProperty("avg_number_of_fields", parserMetricsCounter.getAvgNumberOfFieldsPerRelease());
				releaseJSON.addProperty("total_number_fields",parserMetricsCounter.getNumberOfFeildsPerRelease());
				releaseJSON.addProperty("number_of_files",parserMetricsCounter.getNumberOfFilesPerRelease());
				parserMetricsCounter.resetCounter();
				
				jsonReleases.put(release.getTagName(), releaseJSON);
			}
			result = this.gson.toJson(this.jsonReleases);
			this.deleteDirectory(this.getRepositoryDownloadPath());
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}
	
	private void makeCallsOnContributorCommit(String token) {
		//Call this only after you have releases & commits
		
		if (this.releases != null && this.commits != null) {
			this.contributorCommit = new ContributorCommit(releases, commits, this.repoID, this.cService);
			this.contributorCommit.getContributorsAndCommitsPerRelease();
			this.contributorCommit.fetchCommitStats(token);
		}
	}
	
	public List<Repository> getRepositories(){
		// get list of repositories
		if(this.repositories == null){
			try {
				this.repositories = this.service.getRepositories(this.userName);
			} catch(Exception e) { e.printStackTrace(); }
		}
		return this.repositories;
	}
	
	public String getContributors(){

		if (this.jsonContributors == null) {
			this.jsonContributors = new JsonArray();			
		}

		if(this.contributorCommit != null && this.contributorCommit.getCommitsByContributors() != null){
			try {				
				for (Map.Entry<String, List<RepositoryCommit>> entry : this.contributorCommit.getCommitsByContributors().entrySet()) {
					JsonObject contributorJSON = new JsonObject();	
					contributorJSON.addProperty("contributor_name", entry.getKey());
					contributorJSON.addProperty("number_of_contributions", entry.getValue().size());
					this.jsonContributors.add(contributorJSON);
				}
				
			} catch(Exception e) { e.printStackTrace(); }
		}		
		return gson.toJson(this.jsonContributors);
	}
	
	public String getRepository(){
		String result = null;
		if(this.repository == null){
			try {
				this.repository = this.service.getRepository(this.repoID);
				this.repository.setOwner(this.uService.getUser(this.userName));
				// another way of initializing a Repository object 
				// Repository repo = this.service.getRepository(this.userName, this.project);
				
				try {
					JsonObject tObj = new JsonObject();
					
					JsonObject rjObject = this.parser.parse(this.gson.toJson(this.repository)).getAsJsonObject();
					SRepository repository = this.gson.fromJson(rjObject, SRepository.class);
					String rJson = gson.toJson(repository);
					rjObject = this.parser.parse(rJson).getAsJsonObject();
					tObj.add("repository", rjObject);
					
					result = gson.toJson(tObj);
				} catch(Exception e) { e.printStackTrace(); }
				
			} catch(Exception e) { e.printStackTrace(); }
		}
		return result;
	}
	
	public List<RepositoryTag> getRepositoryTags(){
		try {
			if(this.tags == null){
				this.tags = this.service.getTags(this.repoID);
			}
		} catch(Exception e) { e.printStackTrace(); }
		return this.tags;
	}
	
	public List<Release> getReleases(){
		
		try {
			if(this.releases == null){
				this.releases = this.service.getReleases(this.repoID);
				//this.getMetrics();
				String repoAuthorAndName=repoID.getOwner()+repoID.getName();
				repositoryDownloadPath = releaseDownloadsPath+separator+repoAuthorAndName;
			}
		} catch(Exception e) { e.printStackTrace(); }
		return this.releases;
	}
	
	private void downloadSourceCode(Release release) {
		try {
			URL url = new URL(release.getZipballUrl());
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
			int responseCode = httpsURLConnection.getResponseCode();
		
			if (responseCode == HttpsURLConnection.HTTP_OK) {
			
				InputStream inputStream = httpsURLConnection.getInputStream();
				File aFile = new File(this.getRepositoryDownloadPath());
				aFile.mkdirs();
				String downloadFilePath = this.repositoryDownloadPath + separator +release.getTagName() + ".zip" ;
				FileOutputStream fileOutPutStream = new FileOutputStream(downloadFilePath);
			
				int bytesRead = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					fileOutPutStream.write(buffer, 0, bytesRead);
				}
 
				fileOutPutStream.close();
				inputStream.close();
				httpsURLConnection.disconnect();
				unzipFiles(downloadFilePath, repositoryDownloadPath, release.getTagName());	
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	private void unzipFiles(String zipFilePath, String unzipFilePath, String releaseName) throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipInputStream.getNextEntry();
        
        String unzipFileDirName = unzipFilePath + separator + releaseName + separator;
		File aFile = new File(unzipFileDirName);
		aFile.mkdirs();
        
     // iterates over entries in the zip file
        while (entry != null) {
            String filePath = unzipFileDirName + separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipInputStream, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipInputStream.closeEntry();
            entry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
	}
	
	private void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));

		byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipInputStream.read(bytesIn)) != -1) {
        	bufferedOutputStream.write(bytesIn, 0, read);
        }
        bufferedOutputStream.close();
    }
	
	private void deleteDirectory(String directoryPath) {
		
		if (JavaCodeParser.isDirectoryPresent(directoryPath)) {
			
			Path directory = Paths.get(directoryPath);
			try {
				Files.walkFileTree(directory, new SimpleFileVisitor<Path>(){
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						   Files.delete(file);
						   return FileVisitResult.CONTINUE;
					   }
					
					@Override
					   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						   Files.delete(dir);
						   return FileVisitResult.CONTINUE;
					   }
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public List<RepositoryCommit> getCommitsInfo(){
		// get information of commits for a repository. It is limited to 20 commits
		if(this.commits == null){
			try {
				this.commits = cService.getCommits(this.repoID);
			} catch(Exception e) { e.printStackTrace(); }
		}
		return this.commits;
	}
	
	public HashMap<String,Integer> getNumberOfForksPerRelease()
	{	//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		HashMap<String, Integer> numberOfForksPerRel = new HashMap<String,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		try
		{
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);
			// Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			if(this.forks == null)
				this.forks = this.service.getForks(this.repoID);
			//int nonReleaseForksCounter = 0;
		
			for(int i=0;i<this.releases.size();i++)
			{
				Release current = this.releases.get(i);
				Release next = null;
				int forksCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=this.releases.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = this.releases.get(i+1); } 
				
				for(int j =0;j<this.forks.size();j++) // for each fork, look if it falls in release i or i+i
				{
					Date currentForkDate = this.forks.get(j).getCreatedAt();
					if(next!=null)
					{
						if(currentForkDate.before(current.getCreatedAt()) && currentForkDate.after(next.getCreatedAt()))
						{	numberOfForksPerRel.put(current.getTagName(),++forksCounter);}
/*						else if(i==0 && currentForkDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfForksPerRel.put("non-release",++nonReleaseForksCounter);}*/			
					}
					else
					{	if(currentForkDate.before(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfForksPerRel.put(current.getTagName(), ++forksCounter);			}
					}		
				}
				if(forksCounter ==0)
				{
					numberOfForksPerRel.put(current.getTagName(), forksCounter);
				}	
			}
		} catch(IOException exception) { exception.printStackTrace(); }
		return numberOfForksPerRel;
	}
	
	private String getRepositoryDownloadPath() {
		return repositoryDownloadPath;
	}
	public HashMap<String, Integer>  getNumberOfBranchesPerRelease()
	{	//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		HashMap<String, Integer> numberOfBranchesPerRel = new HashMap<String,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		try
		{
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);
			// Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			if(this.branches == null)
				this.branches = this.service.getBranches(this.repoID);

			//int nonReleaseBranchesCounter = 0;
			
			for(int i=0;i<this.releases.size();i++)
			{
				Release current = this.releases.get(i);
				Release next = null;
				int branchCounter = 0;			
				if((i+1)!=releases.size()) 
				{	next = releases.get(i+1); } 
				
				for(int j =0;j<branches.size();j++) 
				{
					//TODO: VERIFY IF getSha() RETRIEVES AN UNIQUE IDENTIFIER AND GET IT FROM THE this.commits
					String branchCommitSha = this.branches.get(j).getCommit().getSha();
					RepositoryCommit commitcomment = this.cService.getCommit(this.repoID, branchCommitSha);
					Date currentBranchDate = commitcomment.getCommit().getCommitter().getDate();	
					if(next!=null)
					{
						if(!currentBranchDate.after(current.getCreatedAt()) && currentBranchDate.after(next.getCreatedAt()))
						{	numberOfBranchesPerRel.put(current.getTagName(),++branchCounter);		 }
/*						else if(i==0 && currentBranchDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfBranchesPerRel.put(100L,++nonReleaseBranchesCounter);}*/			
					}
					else
					{	if(!currentBranchDate.after(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfBranchesPerRel.put(current.getTagName(), ++branchCounter);			}
					}	
				}
				if(branchCounter ==0)
				{
					numberOfBranchesPerRel.put(current.getTagName(), branchCounter);
				}
			}
		} catch(IOException exception) { exception.printStackTrace(); }
		return numberOfBranchesPerRel;
	}
	
	public HashMap<String, Integer> getNumberOfStarsPerRelease()
	{
		//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		client.setHeaderAccept(GitHubService.ACCEPT_STAR);
		HashMap<String, Integer> numberOfStarsPerRel = new HashMap<String,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		List <UserStargazer> repStars;
		try
		{
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);	
			repStars = starService.getStargazersWithCreatedDate(repoID);			
			//int nonreleaseStargazerCounter = 0;
			for(int i=0;i<releases.size();i++)
			{
				Release current = releases.get(i);
				Release next = null;
				int stargazerCounter = 0;			
				if((i+1)!=releases.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = releases.get(i+1); } 
				
				for(int j =0;j<repStars.size();j++) // for each fork, look if it falls in release i or i+i
				{
					Date currentStarDate = repStars.get(j).getStarredAt();	
					if(next!=null)
					{
						if(!currentStarDate.after(current.getCreatedAt()) && currentStarDate.after(next.getCreatedAt()))
						{	numberOfStarsPerRel.put(current.getTagName(),++stargazerCounter);		 }
						/*else if(i==0 && currentStarDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfStarsPerRel.put(100L,++nonreleaseStargazerCounter);}	*/		
					}
					else
					{	if(!currentStarDate.after(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfStarsPerRel.put(current.getTagName(), ++stargazerCounter);			}
					}	
				}
				if(stargazerCounter ==0)
				{
					numberOfStarsPerRel.put(current.getTagName(), stargazerCounter);
				}
			}
		}catch(IOException exception) { exception.printStackTrace(); }
		return numberOfStarsPerRel;
	}
	
	/*Please note that WatcherService has been tagged as depreciated and it recommends to use the StargazerService. The problem is
	that StargazerService allows you to retrieve only the stars of the repository. I changed the URL in Watcher.java of the core library
	to comply with the latest GitHub API call of retrieving the list of Watchers. */
	public HashMap<String, Integer> getNumberOfRepositoryWatchers() 
	{
		HashMap<String, Integer> numberOfWatchersForProject = new HashMap<>();

		try
		{	
			numberOfWatchersForProject.put(this.repoID.generateId(), this.watcherService.getWatchers(this.repoID).size());
		}
		catch(IOException exception) { exception.printStackTrace(); }
		return numberOfWatchersForProject;
	}

	public HashMap<String,Integer> getTotalNumberOfIssuesPerRelease()
	{
		//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		HashMap<String, Integer> issuesPerRelease = new HashMap<String, Integer>();
		Release currentRelease = null;
		Release nextRelease = null;
		Date currentReleaseDate = null;
		Date nextReleaseDate = null;
		Map<String, String> filter1 = new HashMap<String, String>();
		filter1.put(IssueService.FIELD_SORT, IssueService.SORT_CREATED);
		try {
			if (this.releases == null)
				this.releases = this.service.getReleases(this.repoID);

			if (this.issues == null)
				this.issues = iService.getIssues(this.repoID, filter1);

			Collections.sort(this.releases, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			Collections.sort(this.issues, (issue1, issue2) -> issue1.getCreatedAt().compareTo(issue2.getCreatedAt()));


			for (int i = 0; i < this.releases.size(); i++) {
				int issueCounter = 0;
				currentRelease = this.releases.get(i);
				currentReleaseDate = currentRelease.getCreatedAt();
				if((i+1)!= this.releases.size())
					nextRelease = this.releases.get(i+1);
				if(nextRelease!=null){
					nextReleaseDate = nextRelease.getCreatedAt();
				}
				//TODO: CHECK from where you left off whenever the inner if was false, that way you don't have to start from 0
				for (int j = 0; j < this.issues.size() ; j++) {
					Date issueDate = this.issues.get(j).getCreatedAt();

					if(nextRelease!=null){
						if(issueDate.after(currentReleaseDate) && issueDate.before(nextReleaseDate)){
							issuesPerRelease.put(currentRelease.getTagName(), issueCounter++);
						}
					} else if(i == (issues.size()-1) && issueDate.after(currentReleaseDate)){
						issuesPerRelease.put(currentRelease.getTagName(), issueCounter++);
					}
				}
			}
			/*for(Issue issue : this.issues){
				if(issue.getState().equals("open")){
					openIssueCounter++;
				}
				else if(issue.getState().equals("close")){
					closedIssueCounter++;
				}
			}*/
		}
		catch(Exception exception) { exception.printStackTrace(); }
		return issuesPerRelease;
	}
}
