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
 * An <Lhs-variable>
 */

class LhsVariable extends SyntaxUnit {
    int numStars = 0;
    Variable var;
    Type type;

    @Override void check(DeclList curDecls) {
		var.check(curDecls);
		type = var.type;
		for (int i = 1;  i <= numStars;  ++i) {
		    Type e = type.getElemType();
		    if (e == null) 
		    	error("Type error in left-hand side variable!");
		    
		    type = e;
		}
    }

    @Override void genCode(FuncDecl curFunc) {
		var.genAddressCode(curFunc);
		for (int i = 1;  i <= numStars;  ++i)
		    Code.genInstr("", "movl", "(%eax),%eax", "  *");
    }

    static LhsVariable parse() {
		Log.enterParser("<lhs-variable>");
		LhsVariable lhs = new LhsVariable();
		while (Scanner.curToken == starToken) {
		    ++lhs.numStars;  Scanner.skip(starToken);
		}
		Scanner.check(nameToken);
		lhs.var = Variable.parse();
	
		Log.leaveParser("</lhs-variable>");
		return lhs;
    }

    @Override void printTree() {
		for (int i = 1;  i <= numStars;  ++i) Log.wTree("*");
		var.printTree();
    }
}
