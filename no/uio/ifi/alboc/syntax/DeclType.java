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
 * A <type>
 */
class DeclType extends SyntaxUnit {
    int numStars = 0;
    Type type;

    @Override void check(DeclList curDecls) {
	type = Types.intType;
	for (int i = 1;  i <= numStars;  ++i)
	    type = new PointerType(type);
    }

    @Override void genCode(FuncDecl curFunc) {}

    static DeclType parse() {
        //System.out.println("DeclType->parse()");
		Log.enterParser("<type>");
		DeclType declType = new DeclType();
		Scanner.skip(intToken);
		while (Scanner.curToken == starToken) {
		    ++declType.numStars;
		    Scanner.readNext(); 
		}
	
		Log.leaveParser("</type>");
		return declType;
    }

    @Override void printTree() {
        //StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        //System.out.println(ste[2].getClassName());
        //System.out.println("DeclType->printTree()");
        Log.wTree("int");
        for (int i = 1;  i <= numStars;  ++i) 
            Log.wTree("*");
    }
}
