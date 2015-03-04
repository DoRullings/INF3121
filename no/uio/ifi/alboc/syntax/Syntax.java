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
 * Creates a syntax tree by parsing an AlboC program; 
 * prints the parse tree (if requested);
 * checks it;
 * generates executable code. 
 */
public class Syntax {
    static DeclList library;
    static Program program;

    public static void init() {
    	
        library = new GlobalDeclList();
        // add built in functions
        library.lineNum = -19;
        library.addDecl(new FuncDecl("exit", Types.intType, Types.intType));
        library.addDecl(new FuncDecl("getchar", Types.intType, null));
        library.addDecl(new FuncDecl("getint", Types.intType, null));
        library.addDecl(new FuncDecl("putchar", Types.intType, Types.intType));
        library.addDecl(new FuncDecl("putint", Types.intType, Types.intType));
    }

    public static void finish() {
	// ?
    }

    public static void checkProgram() {
    	program.check(library);
    	program.progDecls.printDecls();
    }

    public static void genCode() {
    	program.genCode(null);
    }

    public static void parseProgram() {
    	program = Program.parse();
    }

    public static void printProgram() {
    	program.printTree();
    }
}













































	











































