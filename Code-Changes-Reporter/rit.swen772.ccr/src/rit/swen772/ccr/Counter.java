package rit.swen772.ccr;

public class Counter {
	
	private int numberOfMethodsPerRelease=0;
	private int numberOfPrivateMethodsPerRelease =0;
	private int numberOfPublicMethodsPerRelease=0;
	private int numberOfDefaultAccessorMethodsPerRelease=0;
	private int numberOfProtectedMethodsPerRelease=0;
	private int numberOfFilesPerRelease =0;
	
	
	private int numberOfFieldsPerRelease = 0;
	private int numberOfPrimitiveFieldsPerRelease=0;
	private int numberOfObjectFieldsPerRelease=0;
	
	private double avgNumberOfMethodsPerRelease =0;
	private double avgNumberOfFieldsPerRelease = 0;

	
	public double getAvgNumberOfMethodsPerRelease()
	{	avgNumberOfMethodsPerRelease = (double)this.getNumberOfMethodsPerRelease()/this.getNumberOfFilesPerRelease();
		return avgNumberOfMethodsPerRelease;
	}
	
	public double getAvgNumberOfFieldsPerRelease()
	{
		avgNumberOfFieldsPerRelease = (double)this.getNumberOfFeildsPerRelease()/this.getNumberOfFilesPerRelease();
		return avgNumberOfFieldsPerRelease;
	}

	public int getNumberOfMethodsPerRelease() {
		return numberOfMethodsPerRelease;
	}
	public int getNumberOfPrivateMethodsPerRelease() {
		return numberOfPrivateMethodsPerRelease;
	}
	public int getNumberOfPublicMethodsPerRelease() {
		return numberOfPublicMethodsPerRelease;
	}
	public int getNumberOfDefaultAccessorMethodsPerRelease() {
		return numberOfDefaultAccessorMethodsPerRelease;
	}
	public int getNumberOfProtectedMethodsPerRelease() {
		return numberOfProtectedMethodsPerRelease;
	}
	
	public int getNumberOfFilesPerRelease()
	{	return numberOfFilesPerRelease;
	}
	
	public int getNumberOfFeildsPerRelease()
	{	return numberOfFieldsPerRelease;
	}
	
	public int getNumberOfPrimitiveFieldsPerRelease()
	{	return numberOfPrimitiveFieldsPerRelease;
	}

	public int getNumberOfObjectFieldsPerRelease()
	{	return numberOfObjectFieldsPerRelease;
	}
	
	
	
	public void incrementNumberOfMethods() {
		this.numberOfMethodsPerRelease++;
	}
	public void incrementNumberOfPrivateMethods() {
		this.numberOfPrivateMethodsPerRelease++;
	}
	public void incrementNumberOfPublicMethods() {
		this.numberOfPublicMethodsPerRelease++;
	}
	public void incrementNumberOfDefaultAccessorMethods() {
		this.numberOfDefaultAccessorMethodsPerRelease++;
	}
	public void incrementNumberOfProtectedMethods() {
		this.numberOfProtectedMethodsPerRelease++;
	}
	
	public void incrementNumberOfFiles()
	{	this.numberOfFilesPerRelease++; 
	}
	
	public void incrementNumberOfFields()
	{	this.numberOfFieldsPerRelease++;
	}
	
	public void incrementNumberOfPrimitiveFields()
	{	this.numberOfPrimitiveFieldsPerRelease++;
	}
	
	public void incrementNumberOfObjectFields()
	{	this.numberOfObjectFieldsPerRelease++;
	}
	
	public void resetCounter()
	{
		numberOfMethodsPerRelease=0;
		numberOfPrivateMethodsPerRelease =0;
		numberOfPublicMethodsPerRelease=0;
		numberOfDefaultAccessorMethodsPerRelease=0;
		numberOfProtectedMethodsPerRelease=0;
		numberOfFilesPerRelease=0;
		numberOfFieldsPerRelease = 0;
		numberOfPrimitiveFieldsPerRelease=0;
		numberOfObjectFieldsPerRelease=0;
		avgNumberOfMethodsPerRelease =0;
		avgNumberOfFieldsPerRelease = 0;
		
	}
	

}
