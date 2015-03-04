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
 * A global <var decl>.
 */
class GlobalVarDecl extends VarDecl {
    GlobalVarDecl(String n) {
		super(n);
		assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
    }

    @Override void genCode(FuncDecl curFunc) {
    	System.out.println("curFunc GlobalDeclList -> genCode = " + curFunc);
    	System.out.println("numElems = " + numElems);
    	System.out.println("type.size() = " + type.size());
    	if (isArray) {
    		Code.genVar(assemblerName, true, numElems, type.getElemType().size(),type + " " + name);
    	} else {
    		
    		Code.genVar(assemblerName, true, 1,  type.size(), type + " " + name);
    	}
    }

    static GlobalVarDecl parse(DeclType dt) {
        //System.out.println("GlobalVarDecl.parse(dt) dt = " + dt);
        Log.enterParser("<var decl>");
        GlobalVarDecl gvd = new GlobalVarDecl(Scanner.curName);
        gvd.typeSpec = dt;
        Scanner.skip(nameToken);
        System.out.println("Scanner.curToken = " + Scanner.curToken);
        while (Scanner.curToken == leftBracketToken) {
            Scanner.skip(leftBracketToken);
            Scanner.check(numberToken);
            gvd.isArray = true;
            gvd.numElems = Scanner.curNum;
            Scanner.readNext();
            Scanner.skip(rightBracketToken);
            System.out.println("isArray = " + gvd.isArray + ", numElems = " + gvd.numElems + ", gvd = " + gvd.name);
        }
        Scanner.skip(semicolonToken);
        Log.leaveParser("</var decl>");
        return gvd;
    }
    
    void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
    	error(name + " is not a function!");
    }
    
    void checkWhetherVariable(SyntaxUnit use) {
    	// OK
    }
}
