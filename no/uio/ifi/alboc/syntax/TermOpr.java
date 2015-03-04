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
class TermOpr extends Operator {
   
    //TermOpr nextOpr = null;
    
    @Override void genCode(FuncDecl curFunca) {
    	Code.genInstr("", "movl", "%eax,%ecx", "");
    	Code.genInstr("", "popl", "%eax", "");
    	switch (oprToken) {
    	case addToken:        
    	    Code.genInstr("", "addl", "%ecx,%eax", "Compute +");  break;
    	case subtractToken:
    	    Code.genInstr("", "subl", "%ecx,%eax", "Compute -");
    	}   	
    }

    static TermOpr parse() {
		Log.enterParser("<term opr>");
		TermOpr localTermOpr = new TermOpr();
		localTermOpr.oprToken = Scanner.curToken;
		Scanner.readNext();
		Log.leaveParser("</term opr>");
		return localTermOpr;
    }

    @Override void printTree() {
        //System.out.println("TermOpr->printTree()");
        String op = "";
        switch (oprToken) {
        case addToken: op = " + "; break;
        case subtractToken: op = " - "; break;   
        }
        Log.wTree(op);
    }
}
