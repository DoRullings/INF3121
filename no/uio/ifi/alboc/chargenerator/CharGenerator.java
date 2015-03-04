package no.uio.ifi.alboc.chargenerator;

/*
 * module CharGenerator
 */

import java.io.*;
import no.uio.ifi.alboc.alboc.AlboC;
import no.uio.ifi.alboc.error.Error;
import no.uio.ifi.alboc.log.Log;

/*
 * Module for reading single characters.
 */
public class CharGenerator {
    public static char curC, nextC;
    public static int lineLength;

    private static LineNumberReader sourceFile = null;
    private static String sourceLine;
    private static int sourcePos;
	
    public static void init() {
	try {
	    sourceFile = new LineNumberReader(new FileReader(AlboC.sourceName));
	} catch (FileNotFoundException e) {
	    Error.error("Cannot read " + AlboC.sourceName + "!");
	}
	sourceLine = "";  sourcePos = 0;  curC = nextC = ' ';
	readNext();  readNext();
    }
	
    public static void finish() {
	if (sourceFile != null) {
	    try {
		sourceFile.close();
	    } catch (IOException e) {
		Error.error("Could not close source file!");
	    }
	}
    }
	
    public static boolean isMoreToRead() {
	//-- Must be changed in part 0:
        if (sourceLine != null)
            return true;
        return false;
    }
	
    public static int curLineNum() {
	return (sourceFile == null ? 0 : sourceFile.getLineNumber());
    }
	
    public static void readNext() {
        curC = nextC;
        try {
            if (sourcePos == sourceLine.length()) { 
		sourceLine = sourceFile.readLine();
                sourcePos = 0;
                if (sourceLine != null) Log.noteSourceLine(curLineNum(), sourceLine);
		while (isMoreToRead() && (sourceLine.length() == 0 || sourceLine.charAt(0) == '#')) {
		    sourceLine = sourceFile.readLine();
                    Log.noteSourceLine(curLineNum(), sourceLine);
		}
                if (sourceLine != null) nextC = sourceLine.charAt(sourcePos++);
            } else {
                nextC = sourceLine.charAt(sourcePos++);
            }
        } catch (IOException e) {
	    e.printStackTrace();
            Error.error(curLineNum(), "IO Error");
            return;
        }
    }
}
