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
 * An <inner expr>.
 */
class InnerExpr extends Operand {
    Expression expr;

    @Override void check(DeclList curDecls) {
    	expr.check(curDecls);
    	type = expr.type;
    }

    @Override void genCode(FuncDecl curFunc) {
    	expr.genCode(curFunc);
    }

    static InnerExpr parse() {
		Log.enterParser("<inner expr>");
	
		InnerExpr ie = new InnerExpr();
		Scanner.skip(leftParToken);
		ie.expr = Expression.parse();
		Scanner.skip(rightParToken);
		Log.leaveParser("</inner expr>");
		return ie;
    }

    @Override void printTree() {
    	Log.wTree("(");  expr.printTree();  Log.wTree(")");
    }
}
