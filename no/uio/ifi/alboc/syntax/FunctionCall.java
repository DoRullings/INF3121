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
 * A <function call>.
 */
class FunctionCall extends Operand {
    String fName;
    ExprList fParse;
    FuncDecl declRef = null;
    
    @Override void check(DeclList curDecls) {
    	Declaration ld1 = curDecls.findDecl(fName, this);
    	System.out.println("fParse.numExprs() = " + fParse.numExprs());
    	ld1.checkWhetherFunction(fParse.numExprs(), this);
    	declRef = (FuncDecl)ld1;
    	type = ld1.type;
    	Expression le = fParse.firstExpr;
    	Declaration ld2 = declRef.funcParams.firstDecl;
    	int i = 0;
    	while (le != null) {
    		i++;
    		le.check(curDecls);
    		Log.noteTypeCheck("Parameter #" + i + " in call on " + fName,  le.type, "actual", ld2.type, "formal", lineNum);
    		if ((!le.type.isSameType(ld2.type)) && (le.type != Types.intType)) {
    			error("Type error for parameter #" + i + ".");
    		}
    		le = le.nextExpr;
    		ld2 = ld2.nextDecl;
    	}
    }

    @Override void genCode(FuncDecl curFunc) {
    	int i = 0;
    	if (fParse.firstExpr != null)
    		i = genParams(fParse.firstExpr, 1, curFunc);
    	Code.genInstr("", "call", declRef.assemblerName, "Call " + declRef.name);
    	if (fParse.firstExpr != null)
    		Code.genInstr("", "addl", "$" + i + ",%esp", "Remove parameters");
    }

    private int genParams(Expression exprList, int start, FuncDecl funcDecls) {
    	int i = 0;
    	if (exprList.nextExpr != null)
    		i = genParams(exprList.nextExpr, start+1, funcDecls);
    	exprList.genCode(funcDecls);
    	Code.genInstr("", "pushl", "%eax", "Push parameter #" + start);
    	i += 4;
    	return i;
    }
   
    static FunctionCall parse() {
        Log.enterParser("<function call>");
        FunctionCall localFunctionCall = new FunctionCall();
        localFunctionCall.fName = Scanner.curName;
        Scanner.skip(nameToken);
        Scanner.skip(leftParToken);
        localFunctionCall.fParse = ExprList.parse();
        Scanner.skip(rightParToken);
        Log.leaveParser("</function call>");
	return localFunctionCall;
    }

    @Override void printTree() {
        Log.wTree(fName + "(");
        fParse.printTree();
        Log.wTree(")");
    }
}
