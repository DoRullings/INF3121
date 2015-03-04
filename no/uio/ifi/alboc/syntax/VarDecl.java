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
import no.uio.ifi.alboc.types.ArrayType;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
    boolean isArray = false;
    int numElems = 0;

    VarDecl(String n) {
    	super(n);
    }

    @Override void check(DeclList curDecls) {
    	typeSpec.check(curDecls);
    	if (isArray) {
    		type = new ArrayType(typeSpec.type, numElems);
    		if (numElems < 0)
    			error("Arrays cannot have negative size!");
    	} else {
    		type = typeSpec.type;
    	}
    	visible = true;
    }

    @Override void printTree() {
        //System.out.println("VarDecl->printTree()" + typeSpec);
        typeSpec.printTree();
        Log.wTree(" " + name);
        if (isArray)
            Log.wTree("[" + numElems + " ]");
        Log.wTreeLn(";");
    }

    @Override int declSize() {
    	System.out.println("declSize = " + type.size());
    	return type.size();
    }

    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
    	use.error(name + " is a variable and no function!");
    }
	
    @Override void checkWhetherVariable(SyntaxUnit use) {
	// OK
    }
}
