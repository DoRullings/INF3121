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
 * A <func decl>
 */
class FuncDecl extends Declaration {
    ParamDeclList funcParams;
    String exitLabel;
    FuncBody funcBody;
    String name;

    FuncDecl(String n) {
    	super(n);
    	name = n;
    	//visible = true;
    	System.out.println("Created FuncDecl object with name = " + n);
		assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
		exitLabel = ".exit$" + n;
    }

    
    FuncDecl (String n, Type type, Type paramType) {
		super(n);
		name = n;
		assemblerName = (AlboC.underscoredGlobals() ? "_" : "") + n;
    	lineNum = -1;
        this.type = type;
		visible = true;
        funcParams = new ParamDeclList();
        if (paramType != null) {
        	ParamDecl lpd = new ParamDecl("param1");
        	lpd.type = paramType;
        	funcParams.addDecl(lpd);
        }
    }
    
    @Override int declSize() {
    	return 0;
    }

    @Override void check(DeclList curDecls) {
    	System.out.println("FuncDecl->type = " + curDecls.numDecls());
    	typeSpec.check(curDecls);
    	type = typeSpec.type;
    	funcParams.check(curDecls);
    	visible = true;
    	funcBody.check(funcParams);
    }
    


    @Override void checkWhetherFunction(int nParamsUsed, SyntaxUnit use) {
    	StackTraceElement[] ste = Thread.currentThread().getStackTrace();
    	System.out.println("this method is called from: " + ste[2].getClassName());

    	int i = funcParams.numDecls();
    	System.out.println("nParamsUsed = " + nParamsUsed + " , numDecls = " + i);
    	if (nParamsUsed != i) {
    		use.error("Calls to " + name + " should have " + i + " parameters, not " + nParamsUsed + "!");
    	}
    }
	
    @Override void checkWhetherVariable(SyntaxUnit use) {
    	use.error(name + " is a function and no variable!");
    }

    @Override void genCode(FuncDecl curFunc) {
    	//System.out.println("FuncDecl-genCode()");
    	int i = funcBody.localDeclList.dataSize();
    	Code.genInstr("", ".globl", assemblerName, "");
    	Code.genInstr(assemblerName, "enter", "$"+i+",$0", "Start function " + name);
    	funcParams.genCode(this);
    	funcBody.genCode(this);
    	Code.genInstr(exitLabel, "leave", "", "");
    	Code.genInstr("", "ret", "", "End function " + name);
    }

    static FuncDecl parse(DeclType ts) {
        //System.out.println("FuncDecl->parse() " + ts.type);
        Log.enterParser("<func decl>");
        FuncDecl declList = new FuncDecl(Scanner.curName);
        declList.typeSpec = ts;
        Scanner.skip(nameToken);
        declList.funcParams = ParamDeclList.parse();
        declList.funcBody = FuncBody.parse();
        Log.leaveParser("</func decl>");
        BrowseFuncParams(declList);
        return declList;
    }
    
    static void BrowseFuncParams(FuncDecl decl) {
    	System.out.println("FuncDecl -> Params in: " + decl.name);
		Declaration currDecl= decl.funcParams.firstDecl;
		
		while (currDecl != null) {
			Declaration param = decl.funcParams.firstDecl;
			System.out.println("Param: " + param.name);
			param = param.nextDecl;
			currDecl = currDecl.nextDecl;
		}
    }

    @Override void printTree() {
        //System.out.println("FuncDecl.printTree()");
        typeSpec.printTree();
        Log.wTree(" " + name + " (");
        funcParams.printTree();
        Log.wTreeLn(")");
        Log.wTreeLn("{");
        Log.indentTree();
        funcBody.printTree();
        Log.outdentTree();
        Log.wTreeLn("}");
    }
}
