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

class Factor extends SyntaxUnit {
    
    Primary firstPrimary;
    Operator firstOpr = null;
    Factor nextFactor = null;
    Type type = null;
    
    @Override void check(DeclList curDecls) {
    	firstPrimary.check(curDecls);
    	if (firstOpr != null) {
    		Primary lp = firstPrimary;
    		Primary nextlp = lp.nextPrimary;
    		Operator lo = firstOpr;
    		while (nextlp != null) {
    			nextlp.check(curDecls);
    			Log.noteTypeCheck("x " + lo.oprToken + " y", lp.type, "x", lineNum);
    			if ((lp.type != Types.intType) || (nextlp.type != Types.intType))
    				error("Type error for " + lo.oprToken + " operator.");
    			lp = nextlp;
    			nextlp = lp.nextPrimary;
    			lo = lo.nextOpr;
    		}
    		type = Types.intType;
    		
    	} else {
    		type = firstPrimary.type;
    	}
    }

    @Override void genCode (FuncDecl curFunc) {
    	Primary lp = firstPrimary;
    	Operator lo = firstOpr;
    	lp.genCode(curFunc);
    	lp = lp.nextPrimary;
    	while (lp != null) {
    		Code.genInstr("", "pushl",  "%eax", "");
    		lp.genCode(curFunc);
    		lo.genCode(curFunc);
    		lp = lp.nextPrimary;
    		lo = lo.nextOpr;
    	}
    	
    }

    static Factor parse() {
		Log.enterParser("<factor>");
		Factor factor = new Factor();
		factor.firstPrimary = Primary.parse();
		Primary tempPrimary = factor.firstPrimary;
		FactorOpr tempOpr = null;
		
		while (Token.isFactorOperator(Scanner.curToken)) {
	            FactorOpr localFO = FactorOpr.parse();
			    if (tempOpr == null) {
			        factor.firstOpr = localFO;
		                tempOpr = localFO;
		                //tempOpr = factor.head;
			    } else {
			        tempOpr.nextOpr = localFO;
		                tempOpr = localFO;
		            }
		            tempPrimary.nextPrimary= Primary.parse();
		            tempPrimary = tempPrimary.nextPrimary;
			}
			Log.leaveParser("</factor>");
		return factor;
    }

    @Override void printTree() {
		Primary localPrimary = firstPrimary;
		Operator localOperator = firstOpr;
	        firstPrimary.printTree();
		localPrimary = localPrimary.nextPrimary;
		while (localOperator != null) {
	            localOperator.printTree();
		    localOperator = localOperator.nextOpr;
		    localPrimary.printTree();
		    localPrimary = localPrimary.nextPrimary;
		}
    }
}
