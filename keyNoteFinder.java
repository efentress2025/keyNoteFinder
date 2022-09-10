import java.io.*;
import java.util.*;

public class keyNoteFinder {
    private static int MAX_STRING_LENGTH = 2;
    private static int LETTER_INDEX = 0;
    private static int ACCIDENTAL_INDEX = 1;

    public static void main(String[] args) throws IOException {  
        findNotesFromKeyLetter();
      }

    private static void findNotesFromKeyLetter() throws IOException {
        System.out.println("What key would you like the notes for? " +
            "(Use uppercase letters, and do not specify major/minor yet. " +
            "Use symbols # and b if applicable).");
        Scanner scnr = new Scanner(System.in);
        String keyLetter = scnr.nextLine();

        while (checkKeyValidity(keyLetter) == false) {
            System.out.println("Entered key is invalid.");
            keyLetter = scnr.nextLine();
        }

        System.out.println("Major or Minor?");
        String keyMode = scnr.nextLine().toLowerCase();
        while ((!keyMode.equals("major")) &&
            (!keyMode.equals("minor"))) {
                System.out.println("Major or Minor?");
                keyMode = scnr.nextLine().toLowerCase();
                keyMode = keyMode.toLowerCase();
        }        

        scnr.close();
        System.out.println(retrieveKeyNotes(keyLetter, keyMode));
    }

    private static boolean checkKeyValidity(String keyInput) {
        if (keyInput.length() > MAX_STRING_LENGTH) {
            return false;
        }

        if (letterIsValid(keyInput) == false) {
            return false;
        }

        if (accidentalIsValid(keyInput) == false) {
            return false;
        }

        return true;
    }

    private static boolean letterIsValid(String keyInput) {
        if (((int)keyInput.charAt(LETTER_INDEX) >= 60) &&
            ((int)keyInput.charAt(LETTER_INDEX) <= 71)) {
                return true;
        }
        return false;
    }

    private static boolean accidentalIsValid(String keyInput) {
        if (keyInput.length() < 2) {
            return true;
        }

        if (((int)keyInput.charAt(ACCIDENTAL_INDEX) == 35) ||
            ((int)keyInput.charAt(ACCIDENTAL_INDEX) == 98)) {
                return true;
        }

        return false;
    }

    private static String retrieveKeyNotes(String keyInput, String modeInput) throws IOException {
        // Open a file containing every relevant key and the notes within them.
        BufferedReader reader = new BufferedReader(new FileReader("key_notes.txt"));
        
        // Get the right key from the file using the inputted key.
        String fileLine = reader.readLine();
        while (fileLine != null) {
            if (fileLine.length() != 0) {
                String parsingOutput = parseLine(keyInput, modeInput, fileLine, reader);
                if (parsingOutput != null) {
                    return parsingOutput;
                }
            }
            fileLine = reader.readLine();
        }
        reader.close();
        return "null";
    }

    private static String parseLine(String keyInput, String modeInput,
        String fileLine, BufferedReader reader) throws IOException {
            if (fileLine.charAt(0) == keyInput.charAt(0)) {
                String checkSharpFlatResult = checkSharpFlat(keyInput, modeInput, reader);
                if (checkSharpFlatResult == null) {
                    checkMajorMinor(modeInput, reader);
                    return reader.readLine();
                }
                reader.close();
                return checkSharpFlatResult;
            }
            return null;
    }

    private static String checkSharpFlat(String keyInput, String modeInput, BufferedReader openFile) throws IOException {
        // Check if input has an accidental (i.e., has a length of two).
        if (keyInput.length() == 2) {
            while (true) {
                // Go to the sharpened/flatted version of the key.
                openFile.readLine();
                openFile.readLine();
                if (openFile.readLine().charAt(1) == keyInput.charAt(1)) {
                    checkMajorMinor(modeInput, openFile);
                    return openFile.readLine();
                }
            }   
        }
        return null;
    }

    private static void checkMajorMinor(String modeInput, BufferedReader openFile) throws IOException {
        if (modeInput.equals("major")) {
            return;
        }
        else {
            openFile.readLine();
            openFile.readLine();
            openFile.readLine();
            return;
        }
    }
}
