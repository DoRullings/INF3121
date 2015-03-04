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
 * An <if-statm>.
 */
class EmptyStatm extends Statement {

    @Override void check(DeclList curDecls) {
	//-- nothing todo
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- nothing todo
    }

    static EmptyStatm parse() {
    	Log.enterParser("<empty statm>");
        Scanner.skip(semicolonToken);
        Log.leaveParser("</empty statm>");
        return new EmptyStatm();
    }

    @Override void printTree() {
        Log.wTreeLn(";");
    }
}
