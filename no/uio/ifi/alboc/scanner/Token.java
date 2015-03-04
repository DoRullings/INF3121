package no.uio.ifi.alboc.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { 
    addToken, ampToken, assignToken, 
    commaToken, 
    divideToken,
    elseToken, eofToken, equalToken, 
    forToken, 
    greaterEqualToken, greaterToken, 
    ifToken, intToken, 
    leftBracketToken, leftCurlToken, leftParToken, lessEqualToken, lessToken, 
    nameToken, notEqualToken, numberToken, 
    returnToken, rightBracketToken, rightCurlToken, rightParToken, 
    semicolonToken, starToken, subtractToken, 
    whileToken;

    public static boolean isFactorOperator(Token t) {
    	return (t == starToken) || (t == divideToken);
    }

    public static boolean isTermOperator(Token t) {
    	return (t == addToken) || (t == subtractToken);
    }

    public static boolean isPrefixOperator(Token t) {
    	return (t == starToken) || (t == subtractToken);
    }

    public static boolean isRelOperator(Token t) {
    	return (t == equalToken) || (t == notEqualToken) || (t == lessToken) || (t == lessEqualToken) || (t == greaterToken) || (t == greaterEqualToken);
    }

    public static boolean isOperand(Token t) {
    	return (t == ampToken) || (t == leftParToken) || (t == nameToken) || (t == numberToken);
    }
}
