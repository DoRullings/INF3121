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
 * A <term>
 */
class Term extends SyntaxUnit {
    //-- Must be changed in part 1+2:
    //-- Must be changed in part 1+2:
    Factor firstFactor;
    Operator firstOpr = null;
    Type type = null;

    @Override void check(DeclList curDecls) {
    	firstFactor.check(curDecls);
    	if (firstOpr != null) {
    		Factor lf = firstFactor;
    		Factor lfNext = ((Factor)lf).nextFactor;
    		Operator lo = firstOpr;
    		while (lfNext != null) {
    			lfNext.check(curDecls);
    			Log.noteTypeCheck("x " + lo.oprToken + " y", lf.type, "x", lineNum);
    			if ((lf.type != Types.intType) || (lf.type != Types.intType))
    				error("Type error for " + lo.oprToken + " operator.");
    			lf = lfNext;
    			lfNext = lf.nextFactor;
    			lo = lo.nextOpr;
    		}
    		type = Types.intType;
    	} else {
    		type = firstFactor.type;
    	}
    }

    @Override void genCode(FuncDecl curFunca) {
    	Factor lf = firstFactor;
    	Operator lo = firstOpr;
    	lf.genCode(curFunca);
    	lf = lf.nextFactor;
    	while (lf != null) {
    		Code.genInstr("", "pushl", "%eax", "");
    		lf.genCode(curFunca);
    		lo.genCode(curFunca);
    		lf = lf.nextFactor;
    		lo = lo.nextOpr;
    	}
    }

    static Term parse() {
        Log.enterParser("<term>");
        Term localTerm = new Term();
        localTerm.firstFactor = Factor.parse();
        Factor localFactor = localTerm.firstFactor;
        TermOpr firstOpr = null;
        while (Token.isTermOperator(Scanner.curToken)) {
            TermOpr localTermOpr = TermOpr.parse();
            if (firstOpr == null) 
                localTerm.firstOpr = firstOpr = localTermOpr;
            else
                firstOpr.nextOpr = firstOpr = localTermOpr;
            localFactor.nextFactor = localFactor = Factor.parse();
            
        }
        Log.leaveParser("</term>");
	return localTerm;
    }

    @Override void printTree() {

        /*
        Factor localFactor = firstFactor;
        Operator localOperator = firstOpr;
        while (true) {
            //if (localOperator == null) System.out.println("is null");
            localFactor.printTree();
            localFactor = localFactor.nextFactor;
            if (localOperator == null) {
                break;
            }
            localOperator.printTree();
            localOperator = localOperator.nextOpr;
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + localOperator);
        }
         */   
        Factor localFactor = firstFactor;
        for (Operator localOperator = firstOpr; ; localOperator = localOperator.nextOpr) {
            localFactor.printTree();
            localFactor = localFactor.nextFactor;
            if (localOperator == null)
                break;
            localOperator.printTree();
            
        }
            
    }
}
