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
 * A list of global declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class GlobalDeclList extends DeclList {

    @Override void genCode(FuncDecl curFunc) {
    	Declaration ld = firstDecl;
    	while (ld != null) {
    		ld.genCode(null);
    		ld = ld.nextDecl;
    	}
    }

    static GlobalDeclList parse() {
		//System.out.println("GlobalDeclList->parse()");
		GlobalDeclList globalDeclList = new GlobalDeclList();
		while (Scanner.curToken == intToken) {
		    DeclType declType = DeclType.parse();
		    //System.out.println("GlobalDeclList.addDecl("+declType+")");
		    globalDeclList.addDecl(Declaration.parse(declType));
		}
		BrowseFuncParams(globalDeclList);
		return globalDeclList;
    }
    
    
    static void BrowseFuncParams(GlobalDeclList list) {
    	System.out.println("GlobalDeclList -> Params in: ");
    	
    	Declaration current = list.firstDecl;
    	while (current != null) {
	    	if (current instanceof FuncDecl) {
	    		System.out.println("FuncDecl: " + current.name);
	    		current = (FuncDecl)current;
	    		Declaration currDecl = ((FuncDecl) current).funcParams.firstDecl;
	    		while (currDecl != null) {
	    			System.out.println("currDecl = " + currDecl.name);
	    			currDecl = currDecl.nextDecl;
	    		}
	    	}
	    	current = current.nextDecl;
    	}
    }
}
