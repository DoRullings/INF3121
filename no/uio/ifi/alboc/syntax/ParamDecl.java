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
 * A <param decl>
 */
class ParamDecl extends VarDecl {
    ParamDecl(String n) {
		super(n);
    	System.out.println("Created ParamDecl object with name = " + n);
    }

    @Override void genCode(FuncDecl curFunc) {
    	// ?
    }

    static ParamDecl parse(DeclType declType) {
		Log.enterParser("<param decl>");
		ParamDecl paramDecl = new ParamDecl(Scanner.curName);
	    paramDecl.typeSpec = declType;
		Scanner.skip(nameToken);
		Log.leaveParser("</param decl>");
		System.out.println("paramDecl = "+ paramDecl.name);
		System.out.println("DeclType = " + declType);
		return paramDecl;
    }

    @Override void printTree() {
        typeSpec.printTree(); 
        Log.wTree(" "+name);
    }
}
