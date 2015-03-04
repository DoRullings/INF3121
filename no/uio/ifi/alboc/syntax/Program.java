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
import no.uio.ifi.alboc.types.Types;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A <program>
 */
class Program extends SyntaxUnit {
    DeclList progDecls; // roten av treet
	
    @Override void check(DeclList curDecls) {
		BrowseFuncParams((GlobalDeclList)progDecls);
    	progDecls.check(curDecls);// <- problemer!
		if (!AlboC.noLink) {
			Declaration ld = progDecls.findDecl("main", Syntax.library);
			//System.out.println()
			if (ld instanceof FuncDecl) {
				FuncDecl lfd = (FuncDecl)ld;
				System.out.println("main type = " + lfd.type);
				if (lfd.type != Types.intType) {
					lfd.error("'main' should be of type int!");
				}
				if (lfd.funcParams.numDecls() != 0) {
					lfd.error("Function 'main' have parameters. Expected none!");
				}
			} else {
				ld.error("'main' is not decleared as a function");
			}
		}
    }
		
    @Override void genCode(FuncDecl curFunc) {
    	progDecls.genCode(null);
    }

    static Program parse() {
		Log.enterParser("<program>");
		Program program = new Program();
		program.progDecls = GlobalDeclList.parse(); // initier roten av treet
		BrowseFuncParams((GlobalDeclList)program.progDecls);
		if (Scanner.curToken != eofToken)
		    Error.expected("A declaration");
		Log.leaveParser("</program>");
		return program;
    }
    
    static void BrowseFuncParams(GlobalDeclList list) {
    	System.out.println("Program -> GlobalDeclList -> Params in: ");
    	
    	Declaration current = list.firstDecl;
    	while (current != null) {
	    	if (current instanceof FuncDecl) {
	    		System.out.println("FuncDecls: " + current.name);
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

    @Override void printTree() {
    	progDecls.printTree();
    }
}
