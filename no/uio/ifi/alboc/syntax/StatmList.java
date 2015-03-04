package no.uio.ifi.alboc.syntax;

/*
 * module Syntax
 */

import java.security.acl.LastOwnerException;

import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.code.Code;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;
import no.uio.ifi.alboc.scanner.Token;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part 1:
    Statement firstStatm = null;
    Statement lastStatm = null;
    
    @Override void check(DeclList curDecls) {
    	Statement ls = firstStatm;
    	while (ls != null) {
    		ls.check(curDecls);
    		ls = ls.nextStatm;
    	}
    }

    @Override void genCode(FuncDecl curFunc) {
    	Statement ls = firstStatm;
    	while (ls != null) {
    		ls.genCode(curFunc);
    		ls = ls.nextStatm;
    	}
    }

/*    static StatmList parse() {
    	Log.enterParser("<statm list_");
    	StatmList sl = new StatmList();
    	Statement lastStatm = null;
    	while (Scanner.curToken != rightCurlToken) {
    		Statement s = Statement.parse();
    		
    		if (lastStatm == null)
    			sl.firstStatm = lastStatm = s;
    		else
    			lastStatm.nextStatm = lastStatm = s;
    	}
		Log.leaveParser("</statm list>");
		return sl;
    }*/
    
    
    static StatmList parse() {
		Log.enterParser("<statm list>");
		StatmList localStatmList = new StatmList();
		
		while (Scanner.curToken != rightCurlToken) {
		    Statement localStatement = Statement.parse();
		    
	            if (localStatmList.firstStatm == null) {
	                localStatmList.firstStatm = localStatement;
	            	localStatmList.lastStatm = localStatmList.firstStatm;
	            } else {
	                localStatmList.lastStatm.nextStatm = localStatement;
	                localStatmList.lastStatm =  localStatmList.lastStatm.nextStatm;
	            }
		}
	
		Log.leaveParser("</statm list>");
		return localStatmList;
    }

    @Override void printTree() {
        for (Statement localStatm = firstStatm; localStatm != null; localStatm = localStatm.nextStatm) {
            localStatm.printTree();
        }
    }
}
