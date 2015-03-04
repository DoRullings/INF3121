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
 * A <while-statm>.
 */
class WhileStatm extends Statement {
    Expression test;
    StatmList body;

    @Override void check(DeclList curDecls) {
		test.check(curDecls);
		body.check(curDecls);
	
		Log.noteTypeCheck("while (t) ...", test.type, "t", lineNum);
		if (test.type instanceof ValueType) {
		    // OK
		} else {
		    error("While-test must be a value.");
		}
    }

    @Override void genCode(FuncDecl curFunc) {
		String testLabel = Code.getLocalLabel(), 
		       endLabel  = Code.getLocalLabel();
	
		Code.genInstr(testLabel, "", "", "Start while-statement");
		test.genCode(curFunc);
		Code.genInstr("", "cmpl", "$0,%eax", "");
		Code.genInstr("", "je", endLabel, "");
		body.genCode(curFunc);
		Code.genInstr("", "jmp", testLabel, "");
		Code.genInstr(endLabel, "", "", "End while-statement");
    }

    static WhileStatm parse() {
        //System.out.println("WhileStatm->parse()");
		Log.enterParser("<while-statm>");
		WhileStatm localWhileStatm = new WhileStatm();
		Scanner.skip(whileToken);
		Scanner.skip(leftParToken);
		localWhileStatm.test = Expression.parse();
		Scanner.skip(rightParToken);
		Scanner.skip(leftCurlToken);
		localWhileStatm.body = StatmList.parse();
		Scanner.skip(rightCurlToken);
		Log.leaveParser("</while-statm>");
		return localWhileStatm;
    }

    @Override void printTree() {
		Log.wTree("while (");
		test.printTree();
		Log.wTreeLn(") {");
		Log.indentTree();
		body.printTree();
		Log.outdentTree();
		Log.wTreeLn("}");
    }
}
