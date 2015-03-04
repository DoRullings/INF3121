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
 * An <expression>
 */
class Expression extends SyntaxUnit {
    Expression nextExpr = null;
    Term firstTerm, secondTerm = null;
    Operator relOpr = null;
    Type type = null;

    @Override void check(DeclList curDecls) {
    	//System.out.println("Expression->check() " + firstTerm);
    	firstTerm.check(curDecls);
    	if (relOpr != null) {
    		secondTerm.check(curDecls);
    		Log.noteTypeCheck("x " + relOpr.oprToken + " y", firstTerm.type, "x", secondTerm.type, "y", lineNum);
    		switch (relOpr.oprToken) {
    		case starToken:
    		case subtractToken:
    			if ((!(firstTerm.type instanceof ValueType)) || (!(secondTerm.type instanceof ValueType)) || ((!firstTerm.type.isSameType(secondTerm.type)) && (firstTerm.type != Types.intType) && (secondTerm.type != Types.intType)))
    				error("Type error for " + relOpr.oprToken + " operator.");
    			break;
    		default:
    			if ((firstTerm.type != Types.intType) || (secondTerm.type != Types.intType))
    				error("Type error for " +relOpr.oprToken + " operator.");
    			break;
    		}
    		type = Types.intType;
    	} else {
    		type = firstTerm.type;
    	}
    }

    @Override void genCode(FuncDecl curFunc) {
    	firstTerm.genCode(curFunc);
    	if (relOpr != null) {
    		Code.genInstr("", "pushl", "%eax", "");
    		secondTerm.genCode(curFunc);
    		relOpr.genCode(curFunc);
    	}
    }

    static Expression parse() {
		//System.out.println("Expression->parse()");
		Log.enterParser("<expression>");
		Expression localExpression = new Expression();
		localExpression.firstTerm = Term.parse();
		if (Token.isRelOperator(Scanner.curToken)) {
		    localExpression.relOpr = RelOpr.parse();
			localExpression.secondTerm = Term.parse();
		}
		Log.leaveParser("</expression>");
		return localExpression;
    }

    void printTree() {
        //-- Must be changed in part 1:
        firstTerm.printTree();
        if(relOpr != null){
            //System.out.println("Expression-printTree()!!!!!!!!!!!!!!!!!!!!!!!1");
            relOpr.printTree();
            secondTerm.printTree();
        }
    }
}
