package hangman;

/**
 * This is a class used to handle the dictionary files of the hangman game.
 * @author Chrysa Rizeakou
 */

import java.io.*;
import java.util.Scanner;

public class HandleFiles {

    /**
     * Creates the dictionary file hangman_DICTIONARY-XXX.txt
     * @param ID The XXX part of the dictionary's name.
     */
    public static void CreateFile (String ID) {
        try {

            //File myObj = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab", "hangman_DICTIONARΥ-" + ID +".txt");
            File myObj = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File overwritten.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Removes blank lines from a hangman_DICTIONARY-XXX.txt file
     * @param ID The XXX part of the dictionary's name.
     */
    public static void RemoveBlankLine(String ID){
        try{
            //File myObj = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-" + ID +".txt");
            File myObj = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
            //File myObj2 = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-help.txt");
            File myObj2 = new File("medialab\\hangman_DICTIONARΥ-help.txt");
            Scanner myReader = new Scanner(myObj);
            FileWriter myWriter = new FileWriter(myObj2);
            //PrintWriter myWriter = new PrintWriter(myObj2);

            while (myReader.hasNext()) {
                String line = myReader.nextLine();
                if (!line.isEmpty()) {
                    myWriter.write(line);
                    //myWriter.newLine();
                    myWriter.write("\n");
                }
            }
            myReader.close();
            myWriter.close();
            myObj.delete();
            myObj2.renameTo(myObj);
        }catch (FileNotFoundException e){
            System.out.println("An file not found error occurred.");
            e.printStackTrace();
        }
        catch (IOException ex){
            System.out.println("An IO exception error occurred.");
            ex.printStackTrace();
        }
    }

    /**
     * Writes to a hangman_DICTIONARY-XXX.txt file
     * @param ID The XXX part of the dictionary's name.
     * @param data The data written to the file.
     */
    public static void WriteFile(String ID, String data) {
        try {
            FileWriter myWriter = new FileWriter("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
            data = data.replaceAll("\\s+",System.getProperty("line.separator"));
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the dictionary's words. Dictionary is a file named hangman_DICTIONARY-XXX.txt
     * @param ID The XXX part of the dictionary's name.
     * @return A String[] containing the dictionary's words.
     * @throws FileNotFoundException
     */
    public static String[] ReadFile(String ID) throws FileNotFoundException{
        String[] res = new String[0];
        //try {
        StringBuilder res2 = new StringBuilder();
        File myObj = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            res2.append(data);
            res2.append(" ");
        }
        myReader.close();
        res = res2.toString().split("\\s+");

        return res;
    }

    /**
     * Deletes a dictionary. Dictionary is a file named hangman_DICTIONARY-XXX.txt
     * @param ID The XXX part of the dictionary's name.
     */
    public static void DeleteFile(String ID) {
        //File myObj = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-" + ID +".txt");
        File myObj = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    /**
     * Checks if a dictionary named hangman_DICTIONARY-XXX.txt exists
     * @param ID The XXX part of the dictionary's name.
     * @return True if that dictionary exists. False if not.
     */
    public static boolean FileExists(String ID) {
        //File file = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-" + ID +".txt");
        File file = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
        return file.exists();
    }


}
