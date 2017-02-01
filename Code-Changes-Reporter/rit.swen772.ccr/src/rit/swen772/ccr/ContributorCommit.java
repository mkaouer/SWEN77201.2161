package rit.swen772.ccr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Release;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

public class ContributorCommit {
	private RepositoryId repoID;
	private CommitService commitService;
	private List<Release> releases;
	private List<String> releaseTags;
	private List<RepositoryCommit> commits;
	
	private ArrayList<CommitUser> projectContributorsList; // Total contributors in the Project
	public Map<String, List<RepositoryCommit>> commitsByContributors; //String is contributor's email //Can get number of commits for each contributor
	private List <RepositoryCommit> listCommit; //List of commits with stats
	private Map<String, List<String>> contributorsPerRelease; //Release Tag : List of Contributor Email
	private Map<String, List<Integer>> releaseStats;
	private Map<String, List<RepositoryCommit>> commitsPerRelease;
	
	public ContributorCommit(List<Release> releases, List<RepositoryCommit> commits, RepositoryId repoID, CommitService commitService) {
		this.releases = releases;
		createReleaseTags();
		this.commits = commits;
		this.repoID = repoID;
		this.commitService = commitService;
	}
	
	public Map<String, List<RepositoryCommit>> getCommitsByContributors() {
		return commitsByContributors;
	}
	
	public Map<String, List<Integer>> getReleaseStats() {
		return releaseStats;
	}
	
	public Map<String, List<RepositoryCommit>> getCommitsPerRelease() {
		return commitsPerRelease;
	}
	
	public void createReleaseTags() {
		
		this.releaseTags = new ArrayList<String>();
		
		if (this.releases != null) {
			for (Release release : this.releases) {
				this.releaseTags.add(release.getTagName());
			}
		}
	}
	
	public void getContributorsAndCommitsPerRelease() {
		
		if (this.releases != null && this.commits != null && this.commitService != null) {
				
			
			// save release and contributors per release
			try {			
				//Sort Releases
				Collections.sort(this.releases, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
	
				List <RepositoryCommit> partialListCommit = this.commits;
	
				//Sort Repository Commits
				Collections.sort(partialListCommit, (repoCommit1, repoCommit2) -> repoCommit1.getCommit().getCommitter().getDate().compareTo(repoCommit2.getCommit().getCommitter().getDate()));
				
				//This file will be created in your source repository. It will contain the details
				
				Date previousReleaseDate = new Date();
				boolean isFirst = true;
				this.projectContributorsList = new ArrayList<CommitUser>();
				this.commitsByContributors = new HashMap<String, List<RepositoryCommit>>();
				this.contributorsPerRelease = new HashMap<String, List<String>>();
				this.commitsPerRelease = new HashMap<String, List<RepositoryCommit>>();
				
				for (Release release : this.releases) {
	
					ArrayList<String> releaseContributorsList = new ArrayList<String>();
					
					final Date pReleaseDate = previousReleaseDate;
					
					//Identify Commits Per Release
					List <RepositoryCommit> releaseCommits;
					if (isFirst) {
						releaseCommits = partialListCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt())).collect(Collectors.toList());
						isFirst = !isFirst;
					} else {
						releaseCommits = partialListCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt()) && repoCommit.getCommit().getCommitter().getDate().after(pReleaseDate)).collect(Collectors.toList());
					}
					
					this.commitsPerRelease.put(release.getTagName(), releaseCommits);
					
					previousReleaseDate = release.getCreatedAt();
					
					for (RepositoryCommit repositoryCommit : releaseCommits) {
						
						//Identify Contributors per Release
						if (!releaseContributorsList.contains(repositoryCommit.getCommit().getCommitter().getEmail())) {
							releaseContributorsList.add(repositoryCommit.getCommit().getCommitter().getEmail());
						}
	
						//Identify Contributors for the Project
						if (!this.projectContributorsList.contains(repositoryCommit.getCommit().getCommitter())) {
							this.projectContributorsList.add(repositoryCommit.getCommit().getCommitter());						
						}
						
						//Mapping Commits with Contributors
						String commitUserEmail;

						//
						try {
							commitUserEmail = repositoryCommit.getCommitter().getLogin();
						} catch (NullPointerException e) {
							commitUserEmail = repositoryCommit.getCommit().getCommitter().getEmail();
						}
												
//						String commitUserEmail = repositoryCommit.getCommit().getCommitter().getEmail();
						
						if (this.commitsByContributors.get(commitUserEmail) == null) {
							List <RepositoryCommit> repoCommits = new ArrayList<RepositoryCommit>(); 
							repoCommits.add(repositoryCommit);
							this.commitsByContributors.put(commitUserEmail, repoCommits);
						} else {
							List <RepositoryCommit> repoCommits = this.commitsByContributors.get(commitUserEmail);
							repoCommits.add(repositoryCommit);
							this.commitsByContributors.put(commitUserEmail, repoCommits);
						}
					}
					
					this.contributorsPerRelease.put(release.getTagName(), releaseContributorsList);
	
				}
				
			} catch(Exception e) { e.printStackTrace(); }
		}
	}
	
	public void fetchCommitStats(String token) {
		
		if (this.releases != null && this.commits != null) {
				
			this.listCommit = new ArrayList<RepositoryCommit>();
			
			try {

				this.releaseStats = new HashMap<String, List<Integer>>();
				
				//HARD CODE NEW COMMIT SERVICE TO HAVE UNINTERRUPTED CALLS
				GitHubClient newclient = new GitHubClient();
				newclient.setOAuth2Token(token);
				CommitService newCommitService = new CommitService(newclient);
				
				//This is done because the normal fetch does not provide CommitStat details
				//For that, one has to individually fetch each commit using the SHA
				//I'm storing the new commit details in the 'listcommit' variable
				//That variable is used for further processing
				//TODO: Try finding a GitHub API Call which provides CommitStats along with the getCommits
				for (RepositoryCommit repositoryCommit : this.commits) {
					//TODO: USE ABOVE HARDCODED COMMITSERVICE TO MAKE THE EXTRA CALLS FOR STATS
					RepositoryCommit aRepoCommit = newCommitService.getCommit(repoID, repositoryCommit.getSha());
						this.listCommit.add(aRepoCommit);
				}
				
				boolean isFirst = true;
				Date previousReleaseDate = new Date();
				
				for (Release release : this.releases) {
					final Date pReleaseDate = previousReleaseDate;
					
					//Identify Commits Per Release
					List <RepositoryCommit> releaseCommits;
					if (isFirst) {
						releaseCommits = this.listCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt())).collect(Collectors.toList());
						isFirst = !isFirst;
					} else {
						releaseCommits = this.listCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt()) && repoCommit.getCommit().getCommitter().getDate().after(pReleaseDate)).collect(Collectors.toList());
					}
					
					previousReleaseDate = release.getCreatedAt();
					
					int releaseCommitAdditions = 0;
					int releaseCommitDeletions = 0;
										
					for (RepositoryCommit repositoryCommit : releaseCommits) {
						releaseCommitAdditions += repositoryCommit.getStats().getAdditions();
						releaseCommitDeletions += repositoryCommit.getStats().getDeletions();
					}
					
					List<Integer> statForRelease = new ArrayList<Integer>();
					statForRelease.add(new Integer(releaseCommitAdditions));
					statForRelease.add(new Integer(releaseCommitDeletions));
					this.releaseStats.put(release.getTagName(), statForRelease);
				
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1)  {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
}
