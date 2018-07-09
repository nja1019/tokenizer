import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.FileReader;

public class tokenization {
    public static void main(String[] args) {
        // Part A

        // Read in files
        FileReader fileReaderA;
        FileReader stopWordFileReader;
        FileReader cristoReader;
        try {
            fileReaderA = new FileReader(new File("tokenization-input-part-A.txt"));
            stopWordFileReader = new FileReader(new File("stopwords.txt"));
            //Tokenize List A and B
            ArrayList<String> tokensA = tokenize(fileReaderA);
            // Converts the Stop Word File Reader into an ArrayList
            ArrayList<String> stopWordList = tokenize(stopWordFileReader);

            //Remove Stop Words from List A and B
            removeStopWords(tokensA, stopWordList);
            // Porter Stemming
            porterStemming(tokensA);
            outputTokens(tokensA);

            // Part B
            cristoReader = new FileReader(new File("monteCristo.txt"));
            ArrayList<String> cristoTokens = tokenize(cristoReader);
            // Question 6
            // ArrayList<String> newList = removeDuplicatesFromList(cristoTokens);
            removeStopWords(cristoTokens, stopWordList);
            porterStemming(cristoTokens);
            outputFrequency(cristoTokens);


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void removeStopWords(ArrayList<String> tokens, ArrayList<String> stopWords) {
        int length = tokens.size() - 1;
        while (length >= 0) {
            if (stopWords.contains(tokens.get(length))) {
                tokens.remove(length);
            }
            length --;
        }
    }

    // For question 6
    public static ArrayList<String> removeDuplicatesFromList(ArrayList<String> words) {
        Set<String> set = new HashSet<>();
        set.addAll(words);
        set.clear();
        return new ArrayList<String>(set);
    }

    public static ArrayList<String> tokenize(FileReader fileReaderA) {
        ArrayList<String> tokens = new ArrayList<String>();
        try {
            int character = fileReaderA.read();
            String token = "";
            while (character != -1) {
                String stringValue = Character.toString((char) character).toLowerCase();
                if (stringValue.contains(".")) {
                    stringValue = stringValue.replace(".", "");
                    token = token + stringValue;
                    character = fileReaderA.read();
                }
                else if (stringValue.equals(" ") || !stringValue.matches("[A-Za-z0-9]+")) {
                    if (token.equals("")) {
                        character = fileReaderA.read();
                    } else {
                        tokens.add(token);
                        token = "";
                        character = fileReaderA.read();
                    }
                } else {
                    token = token + stringValue;
                    character = fileReaderA.read();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tokens;
    }

    public static void porterStemming(ArrayList<String> tokens) {
        int count = 0;
        while (count < tokens.size()) {
            String curr = tokens.get(count);
            if (curr.endsWith("sses")) {
                curr = curr.substring(0, curr.length() - 2);
            }
            else if (curr.endsWith("ied")) {
                if (curr.length() > 4) {
                    curr = curr.substring(0, curr.length() - 1);
                } else {
                    curr = curr.substring(0, curr.length() - 2);
                }
            }
            else if (curr.endsWith("ies")) {
                if (curr.length() > 4) {
                    curr = curr.substring(0, curr.length() - 1);
                } else {
                    curr = curr.substring(0, curr.length() - 2);
                }
            }
            else if (curr.endsWith("us") || curr.endsWith("ss")) {
                // Do nothing
            }
            else if (curr.endsWith("s")) {
                // If token is one letter... There wasn't any documentation on how to handle single letters
                if (curr.length() < 2) {
                    // Do nothing
                } else {
                    char secondToLastLetter = curr.charAt(curr.length() - 2);
                    // Check if the letter before s is a vowel
                    if ("aeiou".indexOf(secondToLastLetter) == -1) {
                        curr = curr.substring(0, curr.length() - 1);
                    }
                }
            }
            else if (curr.endsWith("eedly")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+eedly")) {
                    curr = curr.substring(0, curr.length() - 3);
                }
            }
            else if (curr.endsWith("eed")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+eed")) {
                    curr = curr.substring(0, curr.length() - 1);
                }
            }
            else if (curr.endsWith("ed")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+ed")) {
                    curr = curr.substring(0, curr.length() - 2);
                    curr = dealWithDoubleLettersAndAtBlIzAndShortness(curr);
                }
            }
            else if (curr.endsWith("edly")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+edly")) {
                    curr = curr.substring(0, curr.length() - 4);
                    curr = dealWithDoubleLettersAndAtBlIzAndShortness(curr);
                }
            }
            else if (curr.endsWith("ing")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+ing")) {
                    curr = curr.substring(0, curr.length() - 3);
                    curr = dealWithDoubleLettersAndAtBlIzAndShortness(curr);
                }
            }
            else if (curr.endsWith("ingly")) {
                if (curr.matches(".*[aeiou]+[^aeiou]+ingly")) {
                    curr = curr.substring(0, curr.length() - 5);
                    curr = dealWithDoubleLettersAndAtBlIzAndShortness(curr);
                }
            }
            tokens.set(count, curr);
            count ++;
        }
    }

    public static String dealWithDoubleLettersAndAtBlIzAndShortness(String curr) {
        if (curr.endsWith("ll") || curr.endsWith("zz") || curr.endsWith("ss")) {
            curr = curr.substring(0, curr.length() - 1);
        }
        else if (curr.endsWith("at") || curr.endsWith("iz") || curr.endsWith("bl") || curr.length() < 4) {
            curr = curr + "e";
        }
        return curr;
    }

    public static void outputTokens(ArrayList<String> tokens) throws IOException {
        FileWriter fileWriter = new FileWriter("tokenized.txt");
        int count = 0;
        while (count < tokens.size()) {
            fileWriter.write(tokens.get(count) + "\n");
            count ++;
        }
        fileWriter.close();
    }

    public static void outputFrequency(ArrayList<String> cristo) throws IOException {
        FileWriter fileWriter = new FileWriter("terms.txt");
        HashMap<String, Integer> myMap = new HashMap<String, Integer>();
        int counter = 0;
        ArrayList<String> freqList = new ArrayList<>();
        // Convert arraylist to map to keep track of frequency. Only add unique values to new list
        while (counter < cristo.size()) {
            String value = cristo.get(counter);
            if (myMap.containsKey(value)) {
                myMap.put(value, myMap.get(value) + 1);
            } else {
                myMap.put(value, 1);
                freqList.add(value);
            }
            counter ++;
        }
        // Sort list by map values
        Collections.sort(freqList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return myMap.get(o2) - myMap.get(o1);
            }
        });

        // Output 200 most frequent words
        int count2 = 0;
        while (count2 < 200) {
            fileWriter.write(freqList.get(count2) + ": " + myMap.get(freqList.get(count2)) + "\n");
            count2 ++;
        }
        fileWriter.close();
    }
}
