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
class FactorOpr extends Operator {

    @Override void genCode(FuncDecl curFunc) {
    	Code.genInstr("", "movl", "%eax,%ecx", "");
    	Code.genInstr("", "popl", "%eax", "");
    	
    	switch(oprToken) {
    	case addToken:
    		Code.genInstr("", "addl", "%ebx,%eax", "Compute +");
    		break;
    	case subtractToken:
    		Code.genInstr("", "subl", "%ebx,%eax", "Compute -");
    		break;
    	case starToken:
    		Code.genInstr("", "imull", "%ecx,%eax", "Compute *");
    		break;
    	case divideToken:
    		Code.genInstr("", "cdq", "", "");
    		Code.genInstr("", "idivl", "%ecx", "Compute /");
    	}
    }

    static FactorOpr parse() {
		Log.enterParser("<factor opr>");
		FactorOpr localFactorOpr = new FactorOpr();
		localFactorOpr.oprToken = Scanner.curToken;
		Scanner.readNext();
		Log.leaveParser("</factor opr>");
		return localFactorOpr;
    }

    @Override void printTree() {
        String op = "";
        switch(oprToken) {
        case divideToken: op = " / "; break;
        case starToken: op = " * "; break;
        } 
        Log.wTree(op);
    }
}
