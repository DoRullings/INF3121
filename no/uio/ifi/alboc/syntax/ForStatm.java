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
 * A <for-statm>.
 */

class ForStatm extends Statement {
    Assignment init;
    Expression test;
    Assignment incr;
    StatmList body;

    @Override void check(DeclList curDecls) {
    	init.check(curDecls);
		test.check(curDecls);
		incr.check(curDecls);
		body.check(curDecls);
	
		Log.noteTypeCheck("for (...; t; ...) ...", test.type, "t", lineNum);
		
		if (!(test.type instanceof ValueType))
			error("For-loop test must be a value.");
    }

    @Override void genCode(FuncDecl curFunc) {
    	Code.genInstr("", "", "", "Start for-statement");
    	init.genCode(curFunc);
    	String label1 = Code.getLocalLabel();
    	Code.genInstr(label1, "", "", "");
    	String label2 = Code.getLocalLabel();
    	test.genCode(curFunc);
    	Code.genInstr("", "cmpl", "$0,%eax",  "");
    	Code.genInstr("", "je", label2, "");
    	body.genCode(curFunc);
    	incr.genCode(curFunc);
    	Code.genInstr("", "jmp", label1, "");
    	Code.genInstr(label2, "", "", "End for-statement");
    }

    static ForStatm parse() {
        //System.out.println("WhileStatm->parse()");
        Log.enterParser("<for-statm>");
        ForStatm forStatm = new ForStatm();
        Scanner.readNext();
        Scanner.skip(leftParToken);
        forStatm.init = Assignment.parse();
        Scanner.skip(semicolonToken);
        forStatm.test = Expression.parse();
        Scanner.skip(semicolonToken);
        forStatm.incr = Assignment.parse();
        Scanner.skip(rightParToken);
        Scanner.skip(leftCurlToken);
        forStatm.body = StatmList.parse();
        Scanner.skip(rightCurlToken);
        Log.leaveParser("</for-statm>");
	return forStatm;
    }

    @Override void printTree() {
        Log.wTree("for (");
	init.printTree();
	Log.wTree("; ");
	test.printTree();
	Log.wTree("; ");
	incr.printTree();
	Log.indentTree();
        Log.wTreeLn(") {");
	body.printTree();
	Log.outdentTree();
	Log.wTreeLn("}");
    }
}
