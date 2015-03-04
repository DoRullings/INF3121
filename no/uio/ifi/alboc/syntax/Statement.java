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
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {

    Statement nextStatm = null;
    static Statement parse() {
	Log.enterParser("<statement>");
	Statement s = null;
	if ((Scanner.curToken==nameToken) && (Scanner.nextToken==leftParToken)) {
            s = CallStatm.parse();
	} else if ((Scanner.curToken==nameToken) || (Scanner.curToken==starToken)) {
            s = AssignStatm.parse();
	} else if (Scanner.curToken == forToken) {
            s = ForStatm.parse();
	} else if (Scanner.curToken == ifToken) {
	    s = IfStatm.parse();
	} else if (Scanner.curToken == returnToken) {
            s = ReturnStatm.parse();
	} else if (Scanner.curToken == whileToken) {
	    s = WhileStatm.parse();
	} else if (Scanner.curToken == semicolonToken) {
	    s = EmptyStatm.parse();
	} else {
	    Error.expected("A statement");
	}
	Log.leaveParser("</statement>");
	return s;
    }
}
