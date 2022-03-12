package hangman;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

import java.util.*;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    // Function to remove duplicates from a String[]
    public static String removeDuplicates(String[] list)  throws InvalidCountException{
        //convert String array to LinkedHashSet to remove duplicates
        LinkedHashSet<String> lhSetWords = new LinkedHashSet<String>(Arrays.asList(list));

        //join the words again by space
        StringBuilder sbTemp = new StringBuilder();
        int index = 0;

        for (String s : lhSetWords) {

            if (index > 0) {
                sbTemp.append(" ");
            }
            sbTemp.append(s);
            index++;
        }

        String str = sbTemp.toString();

        return str;
    }

    public static void duplicatesCheck(String[] list)  throws InvalidCountException{
        int all = list.length;
        boolean duplicatesExist = false;
        for (int i = 0; i < list.length; i++) {
            for (int j = i + 1 ; j < list.length; j++) {
                if (list[i].equals(list[j])) {
                    duplicatesExist = true;
                    break;
                }
            }
        }
        if (duplicatesExist) throw new InvalidCountException();
    }

    // Function to remove words with less than 6 letters from a String[]
    public static String removeLessThan6(String[] list)  throws InvalidRangeException{

        //convert String array to LinkedHashSet to remove duplicates
        LinkedHashSet<String> lhSetWords = new LinkedHashSet<String>( Arrays.asList(list) );

        //join the words again by space
        StringBuilder sbTemp = new StringBuilder();
        int index = 0;

        for(String s : lhSetWords){

            if(index > 0 && s.length() >= 6) {
                sbTemp.append(" ");
                sbTemp.append(s);
            }
            index++;
        }

        String str = sbTemp.toString();

        return str;
    }

    // Function to check that every word has >= 6 letters
    public static void lessThan6Check(String[] list)  throws InvalidRangeException{
        int all = list.length;
        boolean lessThan6 = false;
        for(String s : list){
            if(s.length() < 6) {
                lessThan6 = true;
                break;
            }
        }

        if (lessThan6) throw new InvalidRangeException();
    }

    // Function to check that a String has >= 20 words
    public static void wordCountCheck(String[] list)  throws UndersizeException{
        //return list.length() >= 20;
        if (list.length < 20) throw new UndersizeException();
    }


    // Function to check that a String has at least 20% 9-or-more-letters-words
    public static void balanceCheck(String[] list)  throws UnbalancedException{

        int all = list.length;
        int count = 0;
        for(String s : list){
            if(s.length() >= 9) count++;
        }
        float avg = (float) count / (float) all;

        if (avg < 0.2) throw new UnbalancedException();
        //return avg >= 0.2;
    }

    // Function to remove special characters from a String
    public static String removeSpecialChars(String str) {
        str = str.toUpperCase();
        str = str.replace(",","");
        str = str.replace(".","");
        str = str.replace("'","");
        str = str.replace("-","");
        str = str.replace("_","");
        str = str.replace(";","");
        str = str.replace("?","");
        str = str.replace("/","");
        str = str.replace("*","");
        str = str.replace("]","");
        str = str.replace("[","");
        str = str.replace("(","");
        str = str.replace(")","");
        str = str.replace(":","");
        str = str.replaceAll("\\w*\\d\\w* *", "");
        str = str.replace("\"","");

        return str;
    }

    public static String createWordsOfDict(String ol) throws IOException, InvalidCountException, InvalidRangeException, UndersizeException, UnbalancedException, UnbalancedException {
        //OL453936W   OL27448W
        String urlstring = "https://openlibrary.org/works/"+ ol + ".json";
        JSONObject json = readJsonFromUrl(urlstring);
        JSONObject desc = (JSONObject) json.get("description");

        String value = (String) desc.get("value");
        value = removeSpecialChars(value);
        String[] words  = value.split("\\s+");

        String lol = removeDuplicates(words);
        String[] wordsNew = lol.split("\\s+");
        String lol2 = removeLessThan6(wordsNew);
        String[] wordsNew2 = lol2.split("\\s+");

        wordCountCheck(wordsNew2);
        balanceCheck(wordsNew2);

        return lol2;
    }

}