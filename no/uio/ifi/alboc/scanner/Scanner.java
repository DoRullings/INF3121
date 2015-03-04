package no.uio.ifi.alboc.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.alboc.chargenerator.CharGenerator;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;
import static no.uio.ifi.alboc.scanner.Token.*;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
    public static Token curToken, nextToken;
    public static String curName, nextName;
    public static int curNum, nextNum;
    public static int curLine, nextLine;
    private static boolean EOF;

    public static void init() {
	//-- Must be changed in part 0:
        readNext();
        readNext();
    }
	
    public static void finish() {
	//-- Must be changed in part 0:
    }
	
    public static void readNext() {
	curToken = nextToken;  curName = nextName;  curNum = nextNum;
	curLine = nextLine;

	nextToken = null;
	while (nextToken == null) {
	    nextLine = CharGenerator.curLineNum();
	    if (! CharGenerator.isMoreToRead()) {
                if (!EOF) {
                    if (CharGenerator.nextC == '}') {
                        nextToken = rightCurlToken;
                        EOF = true;
                    } else {
                        Error.error(nextLine, "Expected closing curl bracket");
                    }
                } else {
                    nextToken = eofToken;
                }
	    } else {
                /* 
                   TODO:
                   1. read the last char in the file
                   2. fix nameToken when there is only one char
                */
                
                if (CharGenerator.curC == '/' && CharGenerator.nextC == '*') {
                    boolean commentEnd = false;
                    while(true) {
                        CharGenerator.readNext();
                        if (CharGenerator.curC == '*' && CharGenerator.nextC == '/') {
			    CharGenerator.readNext();
                            break;
                        }
			if (!CharGenerator.isMoreToRead()) {
			    Error.error(nextLine, "Missing multiline end token");
			    break;
			}
                    }
                } else if (CharGenerator.curC == '=' && CharGenerator.nextC == '=') {
                    nextToken = equalToken; 
                    CharGenerator.readNext();
                } else if (CharGenerator.curC == '!' && CharGenerator.nextC == '=') {
                    nextToken = notEqualToken;
                    CharGenerator.readNext();
                } else if (CharGenerator.curC == '<' && CharGenerator.nextC == '=') {
                    nextToken = lessEqualToken;
                    CharGenerator.readNext();
                } else if (CharGenerator.curC == '>' && CharGenerator.nextC == '=') {
                    nextToken = greaterEqualToken;
                    CharGenerator.readNext();
                } else if (CharGenerator.curC == '+') {
		    nextToken = addToken;
		} else if (CharGenerator.curC == '&') {
		    nextToken = ampToken;
		} else if (CharGenerator.curC == '=') {
		    nextToken = assignToken;
		} else if (CharGenerator.curC == ',') {
		    nextToken = commaToken;
		} else if (CharGenerator.curC == '/') {
		    nextToken = divideToken;
		} else if (CharGenerator.curC == '>') {
		    nextToken = greaterToken;
		} else if (CharGenerator.curC == '<') {
		    nextToken = lessToken;
		} else if (CharGenerator.curC == '[') {
		    nextToken = leftBracketToken;
		} else if (CharGenerator.curC == ']') {
		    nextToken = rightBracketToken;
		} else if (CharGenerator.curC == '{') {
		    nextToken = leftCurlToken;
		} else if (CharGenerator.curC == '}') {
		    nextToken = rightCurlToken;
		} else if (CharGenerator.curC == '(') {
		    nextToken = leftParToken;
		} else if (CharGenerator.curC == ')') {
		    nextToken = rightParToken;
		} else if (CharGenerator.curC == '*') {
		    nextToken = starToken;
		} else if (CharGenerator.curC == ';') {
		    nextToken = semicolonToken;
		} else if (CharGenerator.curC == '-') {
		    nextToken = subtractToken;
                } else if (isLetterAZ(CharGenerator.curC)) {
                    int prevNumber = CharGenerator.curLineNum();
                    String newString = ""+CharGenerator.curC;
		    char nextC = CharGenerator.nextC;
                    while (prevNumber == CharGenerator.curLineNum() && (isLetterAZ(nextC) || Character.isDigit(nextC) || nextC == '_')) {
                        if (nextC != ' ') newString += nextC;
			CharGenerator.readNext();
                        nextC = CharGenerator.nextC;
                    }

                    if (newString.equals("int")) {
			nextToken = intToken;
		    } else if (newString.equals("if")) {
			nextToken = ifToken;
		    } else if (newString.equals("else")) {
			nextToken = elseToken;
		    } else if (newString.equals("while")) {
			nextToken = whileToken;
		    } else if (newString.equals("for")) {
			nextToken = forToken;
		    } else if (newString.equals("return")) {
			nextToken = returnToken; 
		    } else {
			nextToken = nameToken;
			nextName = newString;
		    }
                } else if (Character.isDigit(CharGenerator.curC) || CharGenerator.curC == '\'') {
                    int prevNumber = CharGenerator.curLineNum();
                    char curC = CharGenerator.curC;
                    String newNumber;
                    if (CharGenerator.curC == '\'') { 
                        CharGenerator.readNext();
                        if (CharGenerator.nextC == '\'') {
                            //newNumber = ""+CharGenerator.curC;
                            nextToken = numberToken;
                            nextNum = CharGenerator.curC;
                            CharGenerator.readNext();
                        } else {
                            while (prevNumber == CharGenerator.curLineNum()) CharGenerator.readNext();
                        }

                    } else {
                        newNumber = ""+CharGenerator.curC;
                        char nextC = CharGenerator.nextC;

                        while (prevNumber == CharGenerator.curLineNum() && (Character.isDigit(nextC))) {
                            if (nextC != ' ') newNumber += nextC;
                            CharGenerator.readNext();
                            nextC = CharGenerator.nextC;
                        }    
                        nextToken = numberToken;
                        nextNum = Integer.parseInt(newNumber);
                    }
                }
                CharGenerator.readNext();
		//Error.error(nextLine, "Illegal symbol: '" + CharGenerator.curC + "'!");
	    }
	}
	Log.noteToken();
    }
	
    private static boolean isLetterAZ(char c) {
	//-- Must be changed in part 0:
        if (Character.isLetter(c) && Character.isDigit(c) != true)
            return true;
	return false;
    }
	
    public static void check(Token t) {
	if (curToken != t)
	    Error.expected("A " + t);
    }
	
    public static void check(Token t1, Token t2) {
	if (curToken != t1 && curToken != t2)
	    Error.expected("A " + t1 + " or a " + t2);
    }
	
    public static void skip(Token t) {
	check(t);  readNext();
    }
	
    public static void skip(Token t1, Token t2) {
	check(t1,t2);  readNext();
    }
}
