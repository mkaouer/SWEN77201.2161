package rit.swen772.ccr;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FieldVisitorAdapter extends VoidVisitorAdapter<Object>{
	private Counter fieldCounter;
	
	public FieldVisitorAdapter(Counter c)
	{
		fieldCounter = c;
	}

	public void visit(FieldDeclaration n, Object arg) 
		{	
			if(n.getElementType() instanceof PrimitiveType)	
				fieldCounter.incrementNumberOfPrimitiveFields();
			else 
				fieldCounter.incrementNumberOfObjectFields();		
			fieldCounter.incrementNumberOfFields();				
		}
}
