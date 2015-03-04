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
 * A local variable declaration
 */
class LocalVarDecl extends VarDecl {
    String varName;

    LocalVarDecl(String n) {
    	super(n); 
    }

    @Override void genCode(FuncDecl curFunc) {
    	// ?
    }

    static LocalVarDecl parse(DeclType declType) {
    	Log.enterParser("<var decl>");
        LocalVarDecl localVarDecl = new LocalVarDecl(Scanner.curName);
        localVarDecl.typeSpec = declType;
        Scanner.skip(nameToken);
        if (Scanner.curToken == leftBracketToken) {
            Scanner.skip(leftBracketToken);
            Scanner.check(numberToken);
            localVarDecl.isArray = true;
            localVarDecl.numElems = Scanner.curNum;
            Scanner.readNext();
            Scanner.skip(rightBracketToken);
        }
        Scanner.skip(semicolonToken);
        Log.leaveParser("</var decl>");
        return localVarDecl;
    }
}
