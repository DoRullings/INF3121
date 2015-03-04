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
 * An <address>.
 */
class Address extends Operand {
    Variable var;

    @Override void check(DeclList curDecls) {
		var.check(curDecls);
		type = new PointerType(var.type);
    }

    @Override void genCode(FuncDecl curFunc) {
		var.genAddressCode(curFunc);
    }

    static Address parse() {
		Log.enterParser("<address>");
		Address a = new Address();
		Scanner.skip(ampToken);
		a.var = Variable.parse();
		Log.leaveParser("</address>");
		return a;
    }

    @Override void printTree() {
		Log.wTree("&");  var.printTree();
    }
}
