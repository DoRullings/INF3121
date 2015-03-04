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
 * A <param decl>
 */
class Primary extends SyntaxUnit {
    
    Primary nextPrimary = null;
    PrefixOpr preOp = null;
    Operand operand;
    Type type = null;

    @Override void check (DeclList curDecls) {
    	operand.check(curDecls);
    	type = operand.type;
    	if (preOp != null) {
    		Log.noteTypeCheck(preOp.oprToken + " e", operand.type, "e", lineNum);
    		switch(preOp.oprToken) {
    			case starToken:
	    			if (!(operand.type instanceof PointerType))
	    				error("Unary * operator may be applied to pointers.");
	    			type = operand.type.getElemType();;
	    			break;
    			case subtractToken:
	    			if (operand.type != Types.intType)
	    				error("Unary - operator may only be applied to integers.");
	    			type = Types.intType;
    		}
    	}
    }


    @Override void genCode (FuncDecl curFunc) {
    	operand.genCode(curFunc);
    	if (preOp != null)
    		preOp.genCode(curFunc);
    }


    static Primary parse() {
        Log.enterParser("<primary>");
        Primary localPrimary = new Primary();
        if (Token.isPrefixOperator(Scanner.curToken)){
            localPrimary.preOp = PrefixOpr.parse();
        }
        localPrimary.operand = Operand.parse();
        Log.leaveParser("</primary>");
        return localPrimary;
    }

    @Override void printTree() {
        if (preOp != null)
            preOp.printTree();
        operand.printTree();
    }
}
