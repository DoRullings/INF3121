package no.uio.ifi.alboc.error;

/*
 * module Error
 */

import no.uio.ifi.alboc.log.Log;
import no.uio.ifi.alboc.scanner.Scanner;

/*
 * Print error messages.
 */
public class Error {
    public static void error(String where, String message) {
	String eMess = "AlboC error" + 
	    (where.length()>0 ? " "+where : "") + ": " + message;
	Log.noteError(eMess);
	throw new AlboCError(eMess);
    }
	
    public static void error(String message) {
	error("", message);
    }
	
    public static void error(int lineNum, String message) {
        //StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        //int index = 2;
        //System.out.println("Method: \"" + ste[index].getMethodName() + "\" in " + ste[index].getClassName() + " class");
	if (lineNum > 0)
	    error("in line "+lineNum, message);
	else
	    error(message);
        System.out.println("ERROR! line: " + lineNum + ", message: " + message);
    }

    public static void panic(String where) {
	error("in method "+where, "PANIC! PROGRAMMING ERROR!");
    }

    public static void init() {
	//-- Must be changed in part 0:
        
    }
    public static void finish() {
	//-- Must be changed in part 0:
    }

    public static void expected(String exp) {
        //StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        //System.out.println("class: " + ste[2].getClassName() + " -> " + ste[1].getMethodName());
	error(Scanner.curLine, 
	      exp + " expected, but found a " + Scanner.curToken + "!");
    }
}
