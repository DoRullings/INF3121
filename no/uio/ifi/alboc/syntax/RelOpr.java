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
 * A <rel opr> (==, !=, <, <=, > or >=).
 */

class RelOpr extends Operator {
    @Override void genCode(FuncDecl curFunc) {
		Code.genInstr("", "popl", "%ecx", "");
		Code.genInstr("", "cmpl", "%eax,%ecx", "");
		Code.genInstr("", "movl", "$0,%eax", "");
		switch (oprToken) {
			case equalToken:        
			    Code.genInstr("", "sete", "%al", "Test ==");  break;
			case notEqualToken:
			    Code.genInstr("", "setne", "%al", "Test !=");  break;
			case lessToken:
			    Code.genInstr("", "setl", "%al", "Test <");  break;
			case lessEqualToken:
			    Code.genInstr("", "setle", "%al", "Test <=");  break;
			case greaterToken:
			    Code.genInstr("", "setg", "%al", "Test >");  break;
			case greaterEqualToken:
			    Code.genInstr("", "setge", "%al", "Test >=");  break;
			}
    }

    static RelOpr parse() {
		Log.enterParser("<rel opr>");
		RelOpr localRelOpr = new RelOpr();
		localRelOpr.oprToken = Scanner.curToken;
		Scanner.readNext();
		Log.leaveParser("</rel opr>");
		return localRelOpr;
    }

    @Override void printTree() {
		String op = "";
		switch (oprToken) {
		case equalToken:        op = "==";  break;
		case notEqualToken:     op = "!=";  break;
		case lessToken:         op = "<";   break;
		case lessEqualToken:    op = "<=";  break;
		case greaterToken:      op = ">";   break;
		case greaterEqualToken: op = ">=";  break;
		}
		Log.wTree(" " + op + " ");
    }
}
