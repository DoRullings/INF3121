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
import no.uio.ifi.alboc.types.Types;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A <return-statm>.
 */

class ReturnStatm extends Statement {
    Expression retExpr;

    @Override void check (DeclList curDecls) {
    	retExpr.check(curDecls);
    }

    @Override void genCode (FuncDecl curFunc) {
    	Log.noteTypeCheck("return e; in " + " f(...)", retExpr.type, "e", lineNum);
    	if ((!retExpr.type.isSameType(curFunc.type)) && (retExpr.type != Types.intType))
    		error("Return value with wrong type.");
    	retExpr.genCode(curFunc);
    	Code.genInstr("", "jmp", curFunc.exitLabel, "Return-statement");
    }

    static ReturnStatm parse() {
        Log.enterParser("<return-statm>");
        ReturnStatm localReturnStatm = new ReturnStatm();
        Scanner.skip(returnToken);
        localReturnStatm.retExpr = Expression.parse();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</return-statm>");
        return localReturnStatm;
    }

    void printTree() {
        Log.wTree("return ");
        retExpr.printTree();
        Log.wTreeLn(";");
    }
}
