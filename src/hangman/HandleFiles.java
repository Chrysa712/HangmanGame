package hangman;

import java.io.*;
import java.util.Scanner;

public class HandleFiles {

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

    // Function to remove blank lines from a file
    // NOT WORKING YET
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

    public static void DeleteFile(String ID) {
        //File myObj = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-" + ID +".txt");
        File myObj = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    public static boolean FileExists(String ID) {
        //File file = new File("C:\\Users\\chriz\\Desktop\\multimedia_project2021-2022\\medialab","hangman_DICTIONARΥ-" + ID +".txt");
        File file = new File("medialab\\hangman_DICTIONARΥ-" + ID + ".txt");
        return file.exists();
    }


}
