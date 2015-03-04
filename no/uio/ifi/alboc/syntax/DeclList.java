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
import no.uio.ifi.alboc.types.*;

/*
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

abstract class DeclList extends SyntaxUnit {
	Declaration firstDecl = null;
    DeclList outerScope;

    @Override void check(DeclList curDecls) {
		outerScope = curDecls;
		//System.out.println("this = " + this + ", outerScope = "+outerScope);
		Declaration declList = firstDecl;
		while (declList != null) {
			//System.out.println("declList = " + declList);
		    declList.check(this);  
        	declList = declList.nextDecl;
		}
    }

    @Override void printTree() {
        Declaration declarationList = firstDecl;
        //System.out.println("DeclList->printTree() " + firstDecl);
        while (declarationList != null) {
            declarationList.printTree();
            declarationList = declarationList.nextDecl;
            if (declarationList != null)
                Log.wTreeLn();
        }
    }
    
    void addDecl(Declaration decl) {
/*    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	System.out.println("addDecl is called from: " + ste[2].getClassName());
    	System.out.println("addDecl = " + decl);*/
    	Declaration ld = firstDecl;
    	if (ld == null) {
    		firstDecl = decl;
    		return;
    	}
    	while (true) {
    		//System.out.println("ld."+ld.name+".equals(" + decl.name + ")");
    		if (ld.name.equals(decl.name)) {
    			decl.error("Name " + ld.name + " already declared!");
    		}
    		if (ld.nextDecl == null) {
    			ld.nextDecl = decl;
    			break;
    		}
    		ld = ld.nextDecl;
    	}
    }
    
    Declaration getFirstDecl() {
    	return firstDecl;
    }

/*    void addDecl(Declaration decl) {
    	System.out.println("add decl");
		if(firstDecl == null){
			System.out.println("firstDecl in addDecl = " + firstDecl);
			firstDecl = decl;
			lastDecl = firstDecl;
		}else {
			lastDecl.nextDecl = decl;
			lastDecl = lastDecl.nextDecl;
		}
		
    }*/


    int dataSize() {
		Declaration dx = firstDecl;
		int res = 0;
	
		while (dx != null) {
		    res += dx.declSize();  
		    dx = dx.nextDecl;
		}
		return res;
    }
    
    int numDecls() {
    	Declaration ld = firstDecl;
    	int i = 0;
    	while (ld != null) {
    		i++;
    		System.out.println("numDecls = " + ld.name);
    		ld = ld.nextDecl;
    	}
    	return i;
    }

    
    
    /* Finner alle her */
    
    void printDecls() {
    	System.out.println("DeclList->GlobalDeclList:");
    	Declaration ld = firstDecl;
    	while (ld != null) {
    		//System.out.println("Declarations = " + ld.name);
    		if (ld instanceof FuncDecl) {
    			System.out.println("Function: " + ((FuncDecl) ld).name);
    			Declaration pd = ((FuncDecl) ld).funcParams.firstDecl;
    			while (pd != null) {
    				System.out.println("  param: " + pd.name);
    				pd = pd.nextDecl;
    			}
    		} 
    		ld = ld.nextDecl;
    	}
    }
    
    Declaration findDecl(String name, SyntaxUnit use) {
    	
    	Declaration ld = firstDecl;

		while (ld != null) {
			if (ld.visible && ld.name.equals(name)) {
				Log.noteBinding(ld.name, ld.lineNum, use.lineNum);
				return ld;
			}
			ld = ld.nextDecl;
		}
		if (outerScope == null) {
			use.error("Name " + name + " is unknown!");
		}
		printDecls();
		System.out.println("outerScope = " + ld);
		return outerScope.findDecl(name, use);
    }
}
