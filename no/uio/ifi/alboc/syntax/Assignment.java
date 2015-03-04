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
 * A <assignment>
 */
class Assignment extends SyntaxUnit {
    //-- Must be changed in part 1+2:
    LhsVariable lhsVar;
    Expression rhsVar;

    @Override void check(DeclList curDecls) {
    	//System.out.println("hit assignment check");
    	lhsVar.check(curDecls);
    	rhsVar.check(curDecls);
    	String str = "v = e";
    	for (int i=1; i<=lhsVar.numStars; i++){
    		str = "*" + str;
    	}
    	
    	Log.noteTypeCheck(str, lhsVar.type, "v", rhsVar.type, "e", lineNum);
    	//System.out.println("lhsVar.type = " + lhsVar.type);
    	//System.out.println("rhsVar.type = " + rhsVar.type);
    	if (!(lhsVar.type instanceof ValueType) || ((!lhsVar.type.isSameType(rhsVar.type)) && (rhsVar.type != Types.intType)))
    		error("Illegal types in assignment.");
    }

    @Override void genCode(FuncDecl curFunca) {
    	lhsVar.genCode(curFunca);
    	Code.genInstr("", "pushl", "%eax", "");
    	rhsVar.genCode(curFunca);
    	Code.genInstr("", "popl", "%edx", "");
    	Code.genInstr("", "movl", "%eax,(%edx)", "  =");
    }

    static Assignment parse() {
	//-- Must be changed in part 1:
        Log.enterParser("<assignment>");
        Assignment localAssignment = new Assignment();
        localAssignment.lhsVar = LhsVariable.parse();
        Scanner.skip(assignToken);
        localAssignment.rhsVar = Expression.parse();
        Log.leaveParser("</assignment>");
	return localAssignment;
    }

    @Override void printTree() {
        lhsVar.printTree();
        Log.wTree(" = ");
        rhsVar.printTree();
    }
}
