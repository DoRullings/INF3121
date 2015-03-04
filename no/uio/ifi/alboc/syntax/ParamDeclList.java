package no.uio.ifi.alboc.syntax;

/*
 * module Syntax
 */

import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.code.Code;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;
import no.uio.ifi.alboc.scanner.Token;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A list of parameter declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class ParamDeclList extends DeclList {
   

    @Override void genCode(FuncDecl curFunc) {
    	Declaration ld = firstDecl;
    	int i = 8;
    	while (ld != null) {
    		ld.assemblerName = (i+"(%ebp)");
    		i += ld.declSize();
    		ld.genCode(curFunc);
    		ld = ld.nextDecl;
    		//System.out.println("ParamDeclList: " + ld.name);
    	}
    }
    
    
/*    @Override void check (DeclList curDecls) {
    	Declaration current = curDecls.firstDecl;
    	while (current != null) {
    		System.out.println("DeclList: " + current.name);
    		if (current instanceof ParamDecl) {
    			System.out.println("!!!!!!!!!!!!!!!!!!!11ParamDeclList: " + current);
    		} else {
    			
    		}
    		current = current.nextDecl;
    	}
    }*/

    static ParamDeclList parse() {
    	//StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	//System.out.println("ParamDeclList is called from: " + ste[2].getClassName());
		ParamDeclList paramDeclList = new ParamDeclList();
		Scanner.skip(leftParToken);
		if (Scanner.curToken != rightParToken) {
		    while (true) {
		    	DeclType ldt = DeclType.parse();
		    	paramDeclList.addDecl(ParamDecl.parse(ldt));
		    	if (Scanner.curToken == rightParToken)
		    		break;
		    	Scanner.skip(commaToken);
		    }
		}
		Scanner.skip(rightParToken);
		PrintParamDeclList(paramDeclList);
		return paramDeclList;
    }

    static void PrintParamDeclList(ParamDeclList paramDeclList) {
    	System.out.println("ParamDeclList -> params: ");
		Declaration currDecl= paramDeclList.firstDecl;
		while (currDecl != null) {
			System.out.println("currDecl = " + currDecl.name);
			currDecl = currDecl.nextDecl;
		}
		
	}

	@Override void printTree() {
		Declaration declList = firstDecl;
		while (declList != null) {
			declList.printTree();  
		    declList = declList.nextDecl;
		    if (declList != null) 
		    	Log.wTree(", ");
		}    
	}
}
