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

class FuncBody extends SyntaxUnit {
    LocalDeclList localDeclList;
    StatmList funcStatms;
    
    @Override void check (DeclList curDecls) {
        localDeclList.check(curDecls);
        //BrowseFuncParams(curDecls);
        funcStatms.check(localDeclList);
    }
    
    static void BrowseFuncParams(FuncDecl decl) {
    	System.out.println("FuncDecl -> Params in: " + decl.name);
		Declaration currDecl= decl.funcParams.firstDecl;
		
		while (currDecl != null) {
			Declaration param = decl.funcParams.firstDecl;
			System.out.println("Param: " + param.name);
			param = param.nextDecl;
			currDecl = currDecl.nextDecl;
		}
    }

    @Override void genCode (FuncDecl curFunc) {
    	localDeclList.genCode(curFunc);
    	funcStatms.genCode(curFunc);
    }

    static FuncBody parse() {
        //System.out.println("FuncBody->parse()");
        Log.enterParser("<func body>");
        FuncBody localFB = new FuncBody();
        Scanner.skip(leftCurlToken);
        localFB.localDeclList = LocalDeclList.parse();
        localFB.funcStatms = StatmList.parse();
        Scanner.skip(rightCurlToken);
        Log.leaveParser("</func body>");
        return localFB;
    }

    void printTree() {
        //System.out.println("FuncBody->printTree()");
    	Log.indentTree();
        if (localDeclList.firstDecl != null) {
        	localDeclList.printTree();
        }
        funcStatms.printTree();
		Log.outdentTree();
        Log.wTreeLn();
    }
}
