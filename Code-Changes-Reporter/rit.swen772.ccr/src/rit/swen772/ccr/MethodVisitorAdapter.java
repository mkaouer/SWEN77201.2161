package rit.swen772.ccr;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitorAdapter extends VoidVisitorAdapter<Object> {
	
	private Counter methodCounter;
	
	public MethodVisitorAdapter(Counter mCounter)
	{
		methodCounter = mCounter;	
	}
	@Override
	public void visit(MethodDeclaration methodDeclaration, Object arg) {
		//System.out.println("Method Name: " + methodDeclaration.getName());
		String methodModifiers =  methodDeclaration.getModifiers().toString();
		if(methodModifiers.contains("PUBLIC"))
		{methodCounter.incrementNumberOfPublicMethods();}
		else if(methodModifiers.contains("PRIVATE"))
		{methodCounter.incrementNumberOfPrivateMethods();}
		else if(methodModifiers.contains("PROTECTED"))
		{ methodCounter.incrementNumberOfProtectedMethods(); }
		else
		{methodCounter.incrementNumberOfDefaultAccessorMethods();}
		methodCounter.incrementNumberOfMethods();
	}
	
}
