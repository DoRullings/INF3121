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
import no.uio.ifi.alboc.types.Types;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A <number>.
 */
class Number extends Operand {
    int numVal;

   @Override void check(DeclList curDecls) {
       this.type = Types.intType;
    }
	
    @Override void genCode(FuncDecl curFunc) {
    	Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal); 
    }

    static Number parse() {
		Log.enterParser("<number>");
		Number localNumber = new Number();
		localNumber.numVal = Scanner.curNum;
		Scanner.skip(numberToken);
		Log.leaveParser("</number>");
		return localNumber;
    }

     @Override void printTree() {
    	 Log.wTree("" + numVal);
    }
}
