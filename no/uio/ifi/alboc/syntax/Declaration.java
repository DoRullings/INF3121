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
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

/*
 * Any kind of declaration.
 */
abstract class Declaration extends SyntaxUnit {
    String name, assemblerName;
    DeclType typeSpec;
    Type type;
    boolean visible = false;
    Declaration nextDecl = null;

    Declaration(String n) {
    	name = n;
    }

    abstract int declSize();

    static Declaration parse(DeclType declType) {
    	System.out.println("TypeSpec = " + declType);
		Declaration decl = null;
		if (Scanner.curToken==nameToken && Scanner.nextToken==leftParToken) {
		    decl = FuncDecl.parse(declType);
		    BrowseParams(decl);
		} else if (Scanner.curToken == nameToken) {
		    decl = GlobalVarDecl.parse(declType);
		} else {
		    Error.expected("A declaration name");
		}
		decl.typeSpec = declType;
		return decl;
    }

    static void BrowseParams(Declaration decl) {
    	System.out.println("params in Declaration: ");
    	FuncDecl dl = (FuncDecl)decl;
    	Declaration pd = dl.funcParams.firstDecl;
    	while (pd != null) {
    		System.out.println("func params = " + pd.name);
    		pd = pd.nextDecl;
    	}
    }

    /**
     * checkWhetherVariable: Utility method to check whether this Declaration is
     * really a variable. The compiler must check that a name is used properly;
     * for instance, using a variable name a in "a()" is illegal.
     * This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be a
     *      variable, the parser will first search for a's declaration d.
     * <li> The parser will call d.checkWhetherVariable(this).
     * <li> Every sub-class of Declaration will implement a checkWhetherVariable.
     *      If the declaration is indeed a variable, checkWhetherVariable will do
     *      nothing, but if it is not, the method will give an error message.
     * </ul>
     * Examples
     * <dl>
     *  <dt>GlobalVarDecl.checkWhetherVariable(...)</dt>
     *  <dd>will do nothing, as everything is all right.</dd>
     *  <dt>FuncDecl.checkWhetherVariable(...)</dt>
     *  <dd>will give an error message.</dd>
     * </dl>
     */
    abstract void checkWhetherVariable(SyntaxUnit use);

    /**
     * checkWhetherFunction: Utility method to check whether this Declaration
     * is really a function.
     * 
     * @param nParamsUsed Number of parameters used in the actual call.
     *                    (The method will give an error message if the
     *                    function was used with too many or too few parameters.)
     * @param use From where is the check performed?
     * @see   checkWhetherVariable
     */
    abstract void checkWhetherFunction(int nParamsUsed, SyntaxUnit use);
}
