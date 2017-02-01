package rit.swen772.ccr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.egit.github.core.Release;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;


public class JavaCodeParser {

	private String releaseDirectoryPath;
	private File file;
	private MethodVisitorAdapter methodVisitor;
	private FieldVisitorAdapter fieldVisitor;
	private Counter counter;
	
	public JavaCodeParser() {
		counter = new Counter();
		methodVisitor = new MethodVisitorAdapter(counter);
		fieldVisitor = new FieldVisitorAdapter(counter);
	}
	
	public static boolean isDirectoryPresent(String directoryPath) {
		Path directory = Paths.get(directoryPath);
		return Files.exists(directory);
	}
	
	public Counter getMetrics(Release release,String repoPath){
		releaseDirectoryPath = repoPath+File.separator+release.getTagName(); 
		this.file = new File(releaseDirectoryPath);

		if (JavaCodeParser.isDirectoryPresent(this.releaseDirectoryPath)) {
			try {
				listFilesForFolder(file);
			}
			catch(IOException exception) { exception.printStackTrace(); }
		}
		else {
			System.out.println("Directory not found");
		}
		return counter;
		
		//The Method counter is ready to start counting the metrics for the next release:
	}
	
	
	
	public void listFilesForFolder(File folder) throws IOException {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {        	
	        	if (fileEntry.getName().endsWith(".java")) {
		            parserMethodsForFile(fileEntry);
	        		counter.incrementNumberOfFiles();
				}
	        }
	    }
	}
	
	public void parserMethodsForFile(File file) throws IOException {
		InputStream in = null;
		CompilationUnit cu;	
		try {
			in = new FileInputStream(file);
			cu = JavaParser.parse(in);
			methodVisitor.visit(cu, null);
			fieldVisitor.visit(cu, null);	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception: "+ e.toString());
		}
		finally {
			in.close();
		}
	}
}
