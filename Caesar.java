/**
* Name: John Otto
* Pennkey: jwotto
* Execution: java Caesar
*
* Program Description: I will code a program that can encrypt, decrypt, 
* and crack caesar cipehrs
*/

public class Caesar {
    /*
    * Description: converts a string to a symbol array, where each element 
    * of the array is an integer encoding of the corresponding element of the string.

    * Input:  the string of the message text to be converted
    * Output: the integer encoded version of the message
    */
    public static int[] stringToSymbolArray(String str) {
        str = str.toUpperCase();
        
        char[] stringToChar = new char[str.length()];
        int[] strToInt = new int [str.length()];
        
        for (int i = 0; i < stringToChar.length; i++) {
            stringToChar[i] = str.charAt(i);
            strToInt[i] = (int) stringToChar[i] - 'A';
        }
        return strToInt;
    }
    
    /*
    * Description: converts an array of symbols to a string, where each 
    * element of the array is an integer encoding of the corresponding
    * element of the string.
    *
    * Input:  integer encoded version of the message
    * Output: the string of message text
    */
    public static String symbolArrayToString(int[] symbols) {
        char [] intToChar = new char [symbols.length];
        String concatenatedString = "";
        
        for (int i = 0; i < intToChar.length; i++) {
            intToChar[i] = (char) (symbols[i] + 'A');
            concatenatedString += intToChar[i];
        }

        return concatenatedString;
    }
    
    /*
    * Description: shift the message by a certain offset value
    * offset value to to encrypt the message
    *
    * Input:  integer of the offset symbol
    * Output: the int of the shifted symbol
    */
    public static int shift(int symbol, int offset) {
        int shiftedSymbol = symbol;
        if (shiftedSymbol >= 0 && shiftedSymbol <= 25) {
            shiftedSymbol = (shiftedSymbol + offset) % 26;
        }
        return shiftedSymbol;
    }
    
    /*
    * Description: unshift the mesage by a certain
    * offset value to decrypt the message
    *
    * Input:  the interger value of the symbol and offset
    * Output: the int of the shifted symbol
    */
    public static int unshift(int symbol, int offset) {
        int shiftedSymbol = symbol;
        if (shiftedSymbol >= 0 && shiftedSymbol <= 25) {
            shiftedSymbol = (shiftedSymbol + 26 - offset) % 26;
        }
        return shiftedSymbol;
    }
    
    /*
    * Description: encrypt the message using the shift key
    *
    * Input:  string of the plain text and a the int of an encryption key
    * Output: the string of the encrypted message
    */
    public static String encrypt(String message, int key) {
        String encryptMessage = "";
        int [] encryptArray = stringToSymbolArray(message);
        
        for (int i = 0; i < encryptArray.length; i++) {
            encryptArray[i] =  shift(encryptArray[i], key);
        }
        encryptMessage = symbolArrayToString(encryptArray);

        return encryptMessage;
    }
    
    /*
    * Description: encrypt the message using the shift key
    *
    *
    * Input:  string of the cipher text and the int of the decryption key
    * Output: the string of the decrypted text
    */
    public static String decrypt(String cipher, int key) {
        String decryptMessage = "";
        int [] decryptArray = stringToSymbolArray(cipher);
        
        for (int i = 0; i < decryptArray.length; i++) {
            decryptArray[i] = unshift(decryptArray[i], key);
        }
        decryptMessage = symbolArrayToString(decryptArray);
        
        return decryptMessage;
    }
    
    /*
    * Description: We want to assign a the frequency at which each letter
    * shows up in the english language to each letter in the alphabet
    *
    * Input:  The name of the file containing frequencies 
    * Output: double array with each letter letter frequency
    */
    public static double[] getDictionaryFrequencies(String filename) {
        double[] letterFrequency = new double [26];
        In inStream = new In(filename);
        
        for (int i = 0; i < 26; i++) {
            letterFrequency[i] = inStream.readDouble();
        }
        return letterFrequency;
    }
    
    /*
    * Description: We want to obtain the frequencies of each letter
    * in the cipher text file
    *
    * Input:  an array of the interger values of each letter in the cipher text
    * Output: double array where the first element in the array is the frequency of 
    * 'A' in the ciphertext, and so on. 
    */
    public static double[] findFrequencies(int[] symbols) {
        double [] letterFrequencies = new double[26];
        double sum = 0;
        
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i] >= 0 && symbols[i] < 26) {
            sum = sum + 1;
            letterFrequencies[symbols[i]]++;
            }
        }
        for (int i = 0; i < 26; i++) {
            letterFrequencies[i] = letterFrequencies[i] / sum;
        }
        return letterFrequencies;
    }
    
    /*
    * Description: We want to score the frequency by finding the absolute 
    * value of the difference between each letter in the ciphertext frequenices
    * and its corresponding letter in the English frequencies
    *
    *
    * Input:  two doouble arrays of the english letter frequency 
    * and cipher text letter frequency
    * Output: the sum/difference between each value pair in the double array
    */
    public static double scoreFrequencies(double[] english, double[] currentFreqs) {
        double score = 0.0;
        for (int i = 0; i < 26; i++) {
            score += Math.abs(currentFreqs[i] - english[i]);
        }
        return score;
    }
        
    /*
    * Description: We want to take a caesar cipher and then decrypt it 
    *
    * Input: the string of cipher text and the name of the file that 
    * contains the frequencies of the english letters
    * Output: the output is the string of the decoded message
    */
    public static String crack(String cipherMessage, String englishLetterFrequency) {
        double lowestScore = 100;
        int optimalKey = 0;

        for (int i = 0; i < 26; i++) {
            int[] intCipherMessage = stringToSymbolArray(decrypt(cipherMessage, i));
            double [] freqOfLetterInCipher = findFrequencies(intCipherMessage);
            double currentScore = scoreFrequencies(
            getDictionaryFrequencies(englishLetterFrequency), freqOfLetterInCipher);
            if (currentScore < lowestScore) {
                lowestScore = currentScore;
                optimalKey = i;
            }
        }
        int [] decodedArrayMessage = stringToSymbolArray(
                                     decrypt(cipherMessage, optimalKey));
        String decodedStringMessage = symbolArrayToString(decodedArrayMessage);
        System.out.println(decodedStringMessage);

        return decodedStringMessage;
        }
    
    public static void main(String[] args) {
        // Handle command line arguments
        String functionCall = args[0];
        if (functionCall.equals("encrypt")) {
            String filename = args[1];
            String letter = args[2];
            In inStream = new In(filename);
            String str = inStream.readAll();
            char key = letter.charAt(0);
            int keyForEncryption = (int) key - 65;
            String str2 = encrypt(str, keyForEncryption);
            System.out.println(str2);
        }

        if (functionCall.equals("decrypt")) {
            String filename = args[1];
            String letter = args[2];
            In inStream = new In(filename);
            String s = inStream.readAll();
            char key = letter.charAt(0);
            int keyForDecryption = (int) key - 65;
            String str2 = decrypt(s, keyForDecryption);
            System.out.println(str2);
        }
        
        if (functionCall.equals("crack")) {
            String filename = args[1];
            String filename2 = args[2];
            In inStream = new In(filename);
            String cipherCode = inStream.readAll();
            crack(cipherCode, filename2);
        }
    }
}