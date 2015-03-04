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
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operand nextOperand = null;
    Type type;

    static Operand parse() {
        Log.enterParser("<operand>");
	
        Operand o = null;
        if (Scanner.curToken == numberToken) {
            o = Number.parse();
        } else if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
            o = FunctionCall.parse();
        } else if (Scanner.curToken == nameToken) {
            o = Variable.parse();
        } else if (Scanner.curToken == ampToken) {
            o = Address.parse();
        } else if (Scanner.curToken == leftParToken) {
            o = InnerExpr.parse();  
        } else {
            Error.expected("An operand");
        }
	
        Log.leaveParser("</operand>");
        return o;
    }
}
