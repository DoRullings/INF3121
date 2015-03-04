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
 * Super class for all syntactic units.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class SyntaxUnit {
    int lineNum;

    SyntaxUnit() {
    	lineNum = Scanner.curLine;
    }

    abstract void check(DeclList curDecls);
    abstract void genCode(FuncDecl curFunc);
    abstract void printTree();

    void error(String message) {
        //StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        //int index = 2;
        //System.out.println("Method: \"" + ste[index].getMethodName() + "\" in " + ste[index].getClassName() + " class");
        Error.error(lineNum, message);
    }
}
