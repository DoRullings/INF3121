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
 * A list of local declarations. 
 * (This class is not mentioned in the syntax diagrams.)
 */
class LocalDeclList extends DeclList {


    @Override void genCode(FuncDecl curFunc) {
    	Declaration ld = firstDecl;
    	int i = 0;
    	while (ld != null) {
    		i -= ld.declSize();
    		ld.assemblerName = (i + "(%ebp)");
    		ld.genCode(curFunc);
    		ld = ld.nextDecl;
    	}
    }
    
/*    void check(DeclList curDecls) {
    	System.out.println("hit check LocalDeclLilst");
    }*/

    static LocalDeclList parse() {
        LocalDeclList localDeclList = new LocalDeclList();
        while (Scanner.curToken == Token.intToken) {
            DeclType localDeclType = DeclType.parse();
            //System.out.println("!!!LocalDeclList decltype = " + localDeclType.type);
            localDeclList.addDecl(LocalVarDecl.parse(localDeclType));
        }
        return localDeclList;
    }
}
