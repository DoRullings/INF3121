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
class PrefixOpr extends Operator {

    @Override void genCode(FuncDecl curFunc) {
    	switch (oprToken) {
    	case starToken:
    		Code.genInstr("", "movl", "(%eax),%eax", "Compute prefix *");
    		break;
    	case subtractToken:
    		Code.genInstr("", "negl", "%eax", "Compute prefix -");
    		break;
    	}
    }

    static PrefixOpr parse() {
        Log.enterParser("<prefix opr>");
        PrefixOpr localPrefixOpr = new PrefixOpr();
        localPrefixOpr.oprToken = Scanner.curToken;
        Scanner.readNext();
        Log.leaveParser("</prefix>");
        return localPrefixOpr;
    }

    @Override void printTree() {
        String op = "&";
        switch(oprToken) {
        case starToken: op = "*"; break;  
        case subtractToken: op = "-"; break;
        }
        Log.wTree(op);
    }
}
