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
import no.uio.ifi.alboc.types.ValueType;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    //-- Must be changed in part 1+2:
    Expression test;
    StatmList body;
    StatmList elseBody;

    @Override void check(DeclList curDecls) {
    	test.check(curDecls);
    	body.check(curDecls);
    	if (elseBody != null)
    		elseBody.check(curDecls);
    	Log.noteTypeCheck("if (t) ...", test.type, "t", lineNum);
    	if (!(test.type instanceof ValueType))
    		error("if-test must be a value.");
    }

    @Override void genCode(FuncDecl curFunc) {
    	String label1 = Code.getLocalLabel();
    	Code.genInstr("", "", "", "Start if-statement");
    	test.genCode(curFunc);
    	if (elseBody != null) {
    		String label2 = Code.getLocalLabel();
    		Code.genInstr("", "cmpl", "$0,%eax", "");
    		Code.genInstr("", "je", label2, "");
    		body.genCode(curFunc);
    		Code.genInstr("", "jmp", label1, "");
    		Code.genInstr(label2, "", "", "  else-part");
    		elseBody.genCode(curFunc);
    	} else {
    		Code.genInstr("", "cmpl", "$0,%eax", "");
    		Code.genInstr("", "je", label1, "");
    		body.genCode(curFunc);
    	}
    	Code.genInstr(label1, "", "", "End if-statement");
    }

    static IfStatm parse() {
		Log.enterParser("<if-statm>");
	        IfStatm localIfStatm = new IfStatm();
	        Scanner.skip(ifToken);
	        Scanner.skip(leftParToken);
	        localIfStatm.test = Expression.parse();
	        Scanner.skip(rightParToken);
	        Scanner.skip(leftCurlToken);
	        localIfStatm.body = StatmList.parse();
	        Scanner.skip(rightCurlToken);
	        if (Scanner.curToken == elseToken) {
	            Log.enterParser("<else-token>");
	            Scanner.skip(elseToken);
	            Scanner.skip(leftCurlToken);
	            localIfStatm.elseBody = StatmList.parse();
	            Scanner.skip(rightCurlToken);
	            Log.leaveParser("</else-token>");
	        }
	        Log.leaveParser("</if-statm>");
		return localIfStatm;
    }

    @Override void printTree() {
	//-- Must be changed in part 1:
        Log.wTree("if (");
        test.printTree();
        Log.wTreeLn(") {");
        Log.indentTree();
        body.printTree();
        if (elseBody != null) {
            Log.outdentTree();
            Log.wTreeLn("} else {");
            Log.indentTree();
            elseBody.printTree();
        }
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}
