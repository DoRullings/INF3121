package no.uio.ifi.alboc.syntax;

/*
 * module Syntax
 */
import no.uio.ifi.alboc.code.Code;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;
import no.uio.ifi.alboc.scanner.Token;
import static no.uio.ifi.alboc.scanner.Token.*;
import no.uio.ifi.alboc.types.*;

/*
 * A <call-statm>.
 */
class CallStatm extends Statement {
    //-- Must be changed in part 1+2:
    FunctionCall func;

    @Override void check(DeclList curDecls) {
    	func.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
    	func.genCode(curFunc);
    }

    static CallStatm parse() {
        //System.out.println("CallStatm->parse()");
        Log.enterParser("<call-statm>");
        CallStatm callStatm = new CallStatm();
        callStatm.func = FunctionCall.parse();
        Scanner.skip(semicolonToken);
        Log.leaveParser("</call-statm>");
        return callStatm;
    }

    @Override void printTree() {
        func.printTree();
        Log.wTreeLn(";");
    }
}
