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
class AssignStatm extends Statement {
    //-- Must be changed in part 1+2:
    Assignment assignment;

    @Override void check(DeclList curDecls) {
    	assignment.check(curDecls);
    }

    @Override void genCode(FuncDecl curFunc) {
    	assignment.genCode(curFunc);
    }

    static AssignStatm parse() {
        //System.out.println("AssignStatm->parse()");
	Log.enterParser("<assign-statm>");
        AssignStatm localAssignStatm = new AssignStatm();
        localAssignStatm.assignment = Assignment.parse();  
        Scanner.skip(semicolonToken);
        Log.leaveParser("</assign-statm>");
	return localAssignStatm;
    }

    @Override void printTree() {
        //System.out.println("AssignStatm->printTree()");
        assignment.printTree();
        Log.wTreeLn(";");
    }
}
