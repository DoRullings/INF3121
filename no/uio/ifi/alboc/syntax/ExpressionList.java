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
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
	
    Expression firstExpr = null;

    @Override void check(DeclList curDecls) {
    	Expression le = firstExpr;
    	while (le != null) {
    		le.check(curDecls);
    		le = le.nextExpr;
    	}
    }
    
    int numExprs() {
    	Expression le = firstExpr;
    	int i = 0;
    	while (le != null) {
    		i++;
    		le = le.nextExpr;
    	}
    	return i;
    }

    @Override void genCode(FuncDecl curFunc) {
	//-- ?
    }

    /*
    static ExprList parse() {      
	
        ExprList expl = new ExprList();

        while(Scanner.curToken != rightParToken){
            if(expl.firstExpr == null ){

                expl.firstExpr = Expression.parse();
                expl.lastExpr = expl.firstExpr;

            } else if(Scanner.curToken != rightParToken){
                do{

                    Scanner.skip(commaToken);
                    expl.lastExpr.nextExpr = Expression.parse();	
                    expl.lastExpr = expl.lastExpr.nextExpr;

                }	
                while(Scanner.nextToken == commaToken);
            }
        }
        Log.leaveParser("</expr list");
        //-- Must be changed in part 1:
        return expl;
    }
    */   
    
    static ExprList parse() {
        Expression tempExpr = null;
        Log.enterParser("<expr list>");
        ExprList localExprList = new ExprList();
	        
        if (Scanner.curToken != rightParToken) {
            while (true) {
                Expression localExpression = Expression.parse();
                if (tempExpr == null) {
                    localExprList.firstExpr = (tempExpr = localExpression);

                } else {
                    tempExpr.nextExpr = (tempExpr = localExpression);
                    tempExpr = localExpression;
                    //System.out.println("tempExpr.nextExpr = " + tempExpr.nextExpr + " _!!!!!!!!!!!!!!!!!!!!!!_ tempExpr = " + tempExpr + "_!!!!!!!!!!!!!!!!_ localExpression " + localExpression);
                }
                if (Scanner.curToken != Token.commaToken)
                    break;
                Scanner.skip(commaToken);
            }
        }
        Log.leaveParser("</expr list");
        //-- Must be changed in part 1:
        return localExprList;
    }
    

    @Override void printTree() {
        Expression expressionList = firstExpr;
        while (expressionList != null) {
            expressionList.printTree();
            expressionList = expressionList.nextExpr;
            if (expressionList != null) {
                //System.out.println("printTree()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Log.wTree(",");
            }
        }
    }
}
