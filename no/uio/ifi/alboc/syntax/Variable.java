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
 * A <variable>.
 */

class Variable extends Operand {
    String varName;
    VarDecl declRef = null;
    Expression index = null;

    @Override void check(DeclList curDecls) {
    	//StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	//System.out.println("Variable->check is called from: " + ste[3].getClassName());
    	
		Declaration d = curDecls.findDecl(varName, this);
		//System.out.println("firstDecl= " + curDecls.firstDecl);
		d.checkWhetherVariable(this);
		declRef = (VarDecl)d;
	
		if (index == null) {
		    type = d.type;
		} else {
		    index.check(curDecls);
		    Log.noteTypeCheck("a[e]", d.type, "a", index.type, "e", lineNum);
	
		    if (index.type == Types.intType) {
			// OK
		    } else {
		    	error("Only integers may be used as index.");
		    }
		    if (d.type.mayBeIndexed()) {
			// OK
		    } else {
		    	error("Only arrays and pointers may be indexed.");
		    }
		    type = d.type.getElemType();
		    System.out.println("returned type from getElemType() = " + type);
		}
    }

    @Override void genCode(FuncDecl curFunc) {
    	System.out.println("declRef.assemblerName = " + declRef.assemblerName);
    	if (index == null) {
    		if (declRef.type instanceof ArrayType) {
    			Code.genInstr("", "leal", declRef.assemblerName + ",%eax", varName);
    		} else {
    			Code.genInstr("", "movl", declRef.assemblerName + ",%eax", varName);
    		}
    	} else {
    		index.genCode(curFunc);
    		if (declRef.type instanceof ArrayType) {
    			Code.genInstr("", "leal", declRef.assemblerName + ",%edx", varName + "[...]");
    		} else {
    			Code.genInstr("", "movl", declRef.assemblerName + ",%edx", varName + "[...]");
    		}
    		Code.genInstr("", "movl", "(%edx,%eax,4),%eax", "");
    	}
    }

    void genAddressCode(FuncDecl curFunc) {
		// Generate code to load the _address_ of the variable
		// rather than its value.
	    	
		if (index == null) {
		    Code.genInstr("", "leal", declRef.assemblerName+",%eax", varName);
		} else {
		    index.genCode(curFunc);
		    if (declRef.type instanceof ArrayType) {
		    	Code.genInstr("", "leal", declRef.assemblerName+",%edx", varName + "[...]");
		    } else {
		    	Code.genInstr("", "movl", declRef.assemblerName+",%edx", varName + "[...]");
		    }
		    Code.genInstr("", "leal", "(%edx,%eax,4),%eax", "");
		}
    }

    static Variable parse() {
    	Log.enterParser("<variable>");
        Variable localVariable = new Variable();
        localVariable.varName = Scanner.curName;
        Scanner.skip(nameToken);
		if (Scanner.curToken == leftBracketToken) {
		    Scanner.skip(leftBracketToken);
		    localVariable.index = Expression.parse();
		    Scanner.skip(rightBracketToken);
		}
		//System.out.println(localVariable.varName);
		Log.leaveParser("</variable>");
		return localVariable;
    }

    @Override void printTree() {
        Log.wTree(varName);
        if (index != null) {
            Log.wTree("[");
            index.printTree();
            Log.wTree("]");
        }
    }
}
