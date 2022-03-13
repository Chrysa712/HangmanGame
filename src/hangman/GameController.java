package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;
//import JsonReader.*;


public class GameController implements Initializable {

    private String word;
    private StringBuilder secretWord;
    private  int[] hitPositions;
    private  boolean[] visible;
    private  int mistakes = 0;
    private  int points = 0;
    private  int hits = 0;
    private  int attempts = 0;
    private  String letNposSuggested="";
    private  boolean pressedSolution = false;
    private Image image = new Image("medialab/images/hangman0.png");
    //---
    //private String dictDetails;
    //private Queue<String> RoundInfo = new PriorityQueue<>();
    private LinkedList<String> RoundInfo = new LinkedList<String>();
    private String oneRound;


    //FXML variables from MenuExit------------
    //FXML variables from GameFile-------------
    @FXML
    private BorderPane InitialScreen;
    @FXML
    private MenuBar menuBarLabel;
    @FXML
    private Label avail_words_dict;
    @FXML
    private Button btnGO1;
    @FXML
    private TextField fieldGuessLetter;
    @FXML
    private TextField fieldGuessPosition;
    @FXML
    private ImageView imgLabel;
    @FXML
    private Label labelPoints;
    @FXML
    private Label percentageLabel;
    @FXML
    private Label wordLabel;
    @FXML
    private Label mistakesLabel;
    @FXML
    private Label selectedDictLabel;
    @FXML
    private Label displayMsgLabel;
    @FXML
    private Label probListsLabel;
    @FXML
    private Label lettersGuessedLabel;


    //FXML funcs for GameFile------------------------------------

    @FXML
    void exitAppClicked(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("You're about to exit!");
        alert.setContentText("Do you really want to exit?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) InitialScreen.getScene().getWindow();
            //System.out.println("MYKONOOOOOS");
            stage.close();
        }
    }

    @FXML
    void startAppClicked(ActionEvent event) throws FileNotFoundException, InvalidRangeException {
        //selectedDictLabel.setText(dictId);
        try {
            startUtil();
        }catch (NullPointerException e) {
            displayMsgLabel.setText("Try loading a Dictionary first");
        }catch (FileNotFoundException e){
            displayMsgLabel.setText("Try loading a Dictionary first");
        }catch (Exception e){
            displayMsgLabel.setText(e.toString());
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    void createAppClicked(ActionEvent event) throws IOException {

        AnchorPane root2 = (AnchorPane) FXMLLoader.load(getClass().getResource("PopUpCreate.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage = new Stage();
        stage.setTitle("Create");
        stage.setScene(scene2);
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.showAndWait();
        stage.show();
        closeScreen();
    }

    @FXML
    void loadAppClicked(ActionEvent event) throws IOException {

        AnchorPane root2 = (AnchorPane) FXMLLoader.load(getClass().getResource("PopUpLoad.fxml"));
        Scene scene2 = new Scene(root2);
        Stage stage = new Stage();
        stage.setTitle("Load");
        stage.setScene(scene2);
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.showAndWait();
        stage.show();
        closeScreen();
    }

    @FXML
    void dictionaryDetsClicked(ActionEvent event) throws IOException {
        try {
            String lol = calculateDictDetails(returnDict2(selectedDictLabel.getText().toString()));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUpDictionary.fxml"));
            Parent root = loader.load();

            PopUpDictionaryController popUpDictionaryController = loader.getController();
            popUpDictionaryController.displayInfo(lol);

            //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dictionary Details");
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (FileNotFoundException e){
            displayMsgLabel.setText("Try loading or creating a Dictionary first.");
        }catch (Exception e){
            displayMsgLabel.setText(e.toString());
        }
    }

    @FXML
    void roundsDetsClicked(ActionEvent event) {
        try {
            String lol="";
            Iterator iterator = RoundInfo.iterator();
            while (iterator.hasNext()) {
                lol += (iterator.next() + "\n ");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUpRounds.fxml"));
            Parent root = loader.load();

            PopUpRoundsController popUpRoundsController = loader.getController();
            popUpRoundsController.displayRounds(lol);

            //Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Round Details");
            //stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (Exception e){
            displayMsgLabel.setText(e.toString());
        }
    }

    @FXML
    void solutionDetsClicked(ActionEvent event) {
        wordLabel.setText(word);
        displayMsgLabel.setText("You lost :( \nThe hidden word is "+word+".\nPress Start from Application menu to play again.");
        image = new Image("medialab/images/hangman6.png");
        imgLabel.setImage(image);
        oneRound += "Selected word: "+word+" - #Mistakes = "+mistakes+" - Winner: Computer";
        fixQueue(RoundInfo, oneRound);
        pressedSolution = true;
    }

    @FXML
    void btnGO1clicked(ActionEvent event) {
        if(word == null) {
            fieldGuessLetter.clear();
            fieldGuessPosition.clear();
        }
        else {
            playTurn();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mistakes = 0;
        hits = 0;
        points = 0;
        attempts = 0;
        hitPositions = new int[0];
        pressedSolution = false;
        //RoundInfo = new PriorityQueue<>();
        RoundInfo = new LinkedList<>();

        imgLabel.setImage(image);
        fieldGuessLetter.clear();
        fieldGuessPosition.clear();
        displayMsgLabel.setText("After loading/creating a Dictionary (Application --> Load / Application --> Create), press Start (Application --> Start) to start a new game.");
        probListsLabel.setText("");
        lettersGuessedLabel.setText("");

    }

    public void startUtil() throws FileNotFoundException, InvalidRangeException {
        //initialize global variables
        mistakes = 0;
        hits = 0;
        points = 0;
        attempts = 0;
        letNposSuggested = "";
        pressedSolution = false;
        //done = true;
        image = new Image("medialab/images/hangman0.png");
        oneRound = "";

        //fix javafx variables
        fieldGuessLetter.clear();
        fieldGuessPosition.clear();
        percentageLabel.setText("?");
        displayMsgLabel.setText("");
        probListsLabel.setText("");
        wordLabel.setText("");
        lettersGuessedLabel.setText("");
        labelPoints.setText(String.valueOf(points));
        mistakesLabel.setText(String.valueOf(mistakes));
        imgLabel.setImage(image);
        setUpGame(returnDict2(selectedDictLabel.getText().toString()));
    }

    public void displaySelectedDict(String selDict){
        selectedDictLabel.setText(selDict);
    }

    public void closeScreen(){
        Stage stage = (Stage) InitialScreen.getScene().getWindow();
        stage.close();
    }

    public static String[] returnDict(String dictID, String OL_ID) throws InvalidCountException, InvalidRangeException, UndersizeException, UnbalancedException, IOException {

        //if (!HandleFiles.FileExists(dictID)) {
        HandleFiles.CreateFile(dictID);
        String helper = JsonReader.createWordsOfDict(OL_ID);
        HandleFiles.WriteFile(dictID, helper);
        HandleFiles.RemoveBlankLine(dictID);
        //}
        return HandleFiles.ReadFile(dictID);
    }

    public static String[] returnDict2(String dictID) throws FileNotFoundException {
        return HandleFiles.ReadFile(dictID);
    }

    private static Boolean BelongsInSubset(int[] HitPositions, String ChosenWord, String OtherWord) {
        int appropriateLength = ChosenWord.length();
        int otherLength = OtherWord.length();
        if (appropriateLength != otherLength) return false;

        boolean allZero = true;
        for (int i = 0; i < HitPositions.length; i++) {
            if (HitPositions[i] != 0) {
                allZero = false;
                break;
            }
        }
        if (allZero) return allZero;
        boolean flag = false;
        for (int i = 0; i < HitPositions.length; i++) {
            if (HitPositions[i] == 1 && OtherWord.charAt(i) == ChosenWord.charAt(i)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //the probability for a specific character is Prob(char Y) = #WordsInTheSubsetThatHaveCharYInSpace / #WordsInSubset
    //#WordsInSubset = WordsWithSameLengthAsTheChosenOne && WordsThatHaveCommonRevealedCharsWithTheChosen
    public static float[] CreateProbabilityListForXEmptySpace(int[] hitPositions, String word, String[] WORDS, Integer space) {

        //create alphabet array
        //char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
        //create frequency array for letters and initialize it with 0
        float[] freq = new float[26];
        for (int i = 0; i < 26; i++) freq[i] = 0;

        //traverse the words in the dictionary, see if they belong in the subset, fix subset counter
        //also fix the frequency array
        List<String> newWords = new ArrayList<>(Arrays.asList(WORDS));
        Iterator<String> itr = newWords.iterator();
        int subsetlen = 0;
        while (itr.hasNext()) {
            String helper = itr.next();
            if (BelongsInSubset(hitPositions, word, helper)) {
                freq[helper.charAt(space) - 65]++;
                subsetlen++;
            }
        }

        //calculate the probability
        for (int i = 0; i < freq.length; i++) {
            freq[i] = freq[i] / (subsetlen);
        }

        return freq;
    }

    public static HashMap<Character, Float> sortByValue(HashMap<Character, Float> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Character, Float>> list = new LinkedList<Map.Entry<Character, Float>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Character, Float>>() {
            public int compare(Map.Entry<Character, Float> o1, Map.Entry<Character, Float> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Collections.reverse(list);

        // put data from sorted list to hashmap
        HashMap<Character, Float> temp = new LinkedHashMap<Character, Float>();
        for (Map.Entry<Character, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public String calculateDictDetails (String[] WORDS) {
        try{
            int numOfWords = WORDS.length;
            int sixCount = 0;
            int sevenNineCount = 0;
            int tenCount = 0;
            String ret = "";
            for (int i=0; i<numOfWords; i++){
                if (WORDS[i].length()==6) sixCount++;
                else{
                    if (WORDS[i].length()>=10) tenCount++;
                    else sevenNineCount++;
                }
            }
            ret += ("Percentage of 6-letter words in current dictionary " + (float)sixCount/numOfWords*100+"%\n\n");
            ret += ("Percentage of 7_9-letter words in current dictionary " + (float)sevenNineCount/numOfWords*100+"%\n\n");
            ret += ("Percentage of 10_or_more-letter words in current dictionary " + (float)tenCount/numOfWords*100+"%\n");
            return ret;
        }catch (NullPointerException e){
            return "Try loading a dictionary first";
        }
        catch (Exception e){
            return e.toString();
        }
    }

    public void fixQueue (LinkedList q, String s){
        if(q.size()<5) q.addFirst(s);
        else{
            q.removeLast();
            q.addFirst(s);
        }
    }

    public void setUpGame(String[] WORDS) throws InvalidRangeException {

        if (WORDS[0] == "") throw new InvalidRangeException();
        int numOfWords = WORDS.length;
        avail_words_dict.setText(String.valueOf(numOfWords));

        word = WORDS[new Random().nextInt(WORDS.length)];
        int wordLength = word.length();
        secretWord = new StringBuilder();
        secretWord.append("_".repeat(wordLength));
        wordLabel.setText(String.valueOf(secretWord));
        //hangmanLives = new ArrayList<>();

        //calculate and show initial probability lists
        //lists to print---------------------------------------------------------------------------
        visible = new boolean[word.length()];
        HashMap<Character, Float> listToPrint = new HashMap<Character, Float>();
        HashMap<Character, Float> sortedList2 = new HashMap<Character, Float>();
        hitPositions = new int[wordLength];
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
        String str = "";
        for (int i = 0; i < word.length(); i++) {
            //System.out.println(Arrays.toString(hitPositions));
            float[] list = CreateProbabilityListForXEmptySpace(hitPositions, word, WORDS, i);
            //Create tuple list [(A,prob(A)), (B,prob(B)), ...]
            //sort it based on the probabilities and then print the first word.length() elements
            for (int j = 0; j < list.length; j++) {
                listToPrint.put(alphabet[j], list[j]);
            }

            str += "For position " + (i + 1) + " --> ";
            //System.out.print("list for position " + (i + 1) + " is --> ");// + sortByValue(listToPrint));
            HashMap<Character, Float> sortedList = sortByValue(listToPrint);
            sortedList2 = sortedList;
            int count = 0;
            Iterator<Character> itr = sortedList.keySet().iterator();
            Character letter;
            while (itr.hasNext() && count < 5) {
                letter = itr.next();
                //System.out.print(letter + ":" + sortedList.get(letter) + ", ");
                str += (letter + ":" + sortedList.get(letter) + " ");
                count++;
            }
            //System.out.println("");
            str += "\n";
        }
        probListsLabel.setText(str);
        //-------------------------------------------------------------------------------

    }

    public void playTurn() {

        labelPoints.setText(String.valueOf(points));
        attempts++;

        float probabilityHelpForPoints;
        try {
            if (pressedSolution) throw new IllegalArgumentException();
            boolean done = true;

            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
            List<Character> alBe = new ArrayList<>();
            for (char ch : alphabet) alBe.add(ch);

            //calculate and show probability lists START
            //lists to print---------------------------------------------------------------------------
            HashMap<Character, Float> listToPrint = new HashMap<Character, Float>();
            HashMap<Character, Float> [] sortedList2a = new HashMap[word.length()];
            String str = "";
            for (int i = 0; i < word.length(); i++) {
                //if condition to show only non found letters probabilities
                if (hitPositions[i] == 0) {
                    float[] list = CreateProbabilityListForXEmptySpace(hitPositions, word, returnDict2(selectedDictLabel.getText().toString()), i);
                    //Create tuple list [(A,prob(A)), (B,prob(B)), ...]
                    //sort it based on the probabilities and then print the first word.length() elements
                    for (int j = 0; j < list.length; j++) {
                        listToPrint.put(alphabet[j], list[j]);
                    }

                    str += "For position " + (i + 1) + " --> ";
                    //System.out.print("list for position " + (i + 1) + " is --> ");// + sortByValue(listToPrint));
                    HashMap<Character, Float> sortedLista = sortByValue(listToPrint);
                    sortedList2a[i] = sortedLista;
                    int count = 0;
                    Iterator<Character> itr = sortedLista.keySet().iterator();
                    Character letter;
                    while (itr.hasNext() && count < 5) {
                        letter = itr.next();
                        //System.out.print(letter + ":" + sortedList.get(letter) + ", ");
                        str += (letter + ":" + sortedLista.get(letter) + " ");
                        count++;
                    }
                    //System.out.println("");
                    str += "\n";
                }
            }
            probListsLabel.setText(str);
            //calculate and show probability lists END-------------------------------------------------

            //Letter and position Input --------------

            char chr = fieldGuessLetter.getText().toUpperCase().toCharArray()[0];

            if(!Character.isLetter(chr)) throw new Exception();

            int pos = Integer.parseInt(fieldGuessPosition.getText())-1;

            letNposSuggested += (chr+" - " + (pos+1) +"  ");

            //while (hitPositions[pos] == 1) {
            if (hitPositions[pos] == 1){
                displayMsgLabel.setText("Choose a position that isn't already visible.");
                return;
            }
            //Letter and position Input (end)--------------

            //CHECK IF WE HAVE A HIT (START)----------------
            boolean hit = false;

            if (word.charAt(pos) == chr && !visible[pos]) {
                visible[pos] = true;
                hit = true;
                hits++;
                //hitPositions from [0,0,0,0,...] becomes [0,0,1,0,...]
                hitPositions[pos] = 1;
            }

            if (hit) {
                //Point system: Prob>=0.6 --> 5, 0.6>Prob>=0.4 --> 10, 0.4>Prob>=0.25 --> 15, 0.25>Prob>=0 --> 30
                probabilityHelpForPoints = sortedList2a[pos].get(chr);
                if (probabilityHelpForPoints < 0.25) {
                    points += 30;
                } else {
                    if (probabilityHelpForPoints < 0.4)
                        points += 15;
                    else {
                        if (probabilityHelpForPoints < 0.6)
                            points += 10;
                        else {
                            points += 5;
                        }
                    }
                }
                displayMsgLabel.setText("Hit! \nPoints now:" + String.valueOf(points));
                labelPoints.setText(String.valueOf(points));
            } else {
                ++mistakes;
                //Point system: points -= 15 for every mistake, never<0
                points -= 15;
                if (points < 0) points = 0;
                displayMsgLabel.setText("Missed! Mistake " + String.valueOf(mistakes) + " out of 6\nPoints now: " + String.valueOf(points));
                labelPoints.setText(String.valueOf(points));
            }
            String outWord = "";
            for (int i = 0; i < word.length(); ++i) {
                if (visible[i]) {
                    outWord += word.charAt(i);
                } else {
                    //System.out.print("_");
                    outWord += "_";
                }
            }
            wordLabel.setText(outWord);
            percentageLabel.setText( String.valueOf((((float) hits / attempts) *100.0) + "%"));
            mistakesLabel.setText(String.valueOf(mistakes));
            //System.out.println("Hits/attempts = "+ (hits/attempts) +"   "+ (hits/attempts));
            if(mistakes == 6){
                displayMsgLabel.setText("You lost :( \nThe hidden word is "+word+".\nPress Start from Application menu to play again.");
                wordLabel.setText(word);
                oneRound += "Selected word: "+word+" - #Mistakes = "+mistakes+" - Winner: Computer";
                fixQueue(RoundInfo, oneRound);
                //return;
            }

            for (int i = 0; i < word.length(); ++i) {
                if (!visible[i]) done = false;
            }
            if(done){
                displayMsgLabel.setText("You won! :)\nYour points are "+points+" !\nPress Start from Application menu to play again.");
                oneRound += "Selected word: "+word+" - #Mistakes = "+mistakes+" - Winner: Player";
                fixQueue(RoundInfo, oneRound);
            }

            lettersGuessedLabel.setText(letNposSuggested);
            image = new Image("medialab/images/hangman"+mistakes+".png");
            imgLabel.setImage(image);
            //imgLabel.setImage(Image.fromPlatformImage(hangmanLives[mistakes]));

            //CHECK IF WE HAVE A HIT (END)----------------

            //calculate and show probability lists START
            //lists to print---------------------------------------------------------------------------
            HashMap<Character, Float> listToPrint2 = new HashMap<Character, Float>();
            HashMap<Character, Float> sortedList2 = new HashMap<Character, Float>();
            String str2 = "";
            for (int i = 0; i < word.length(); i++) {
                //if condition to show only non found letters probabilities
                if (hitPositions[i] == 0) {
                    //System.out.println(Arrays.toString(hitPositions));
                    float[] list = CreateProbabilityListForXEmptySpace(hitPositions, word, returnDict2(selectedDictLabel.getText().toString()), i);
                    //Create tuple list [(A,prob(A)), (B,prob(B)), ...]
                    //sort it based on the probabilities and then print the first word.length() elements
                    for (int j = 0; j < list.length; j++) {
                        listToPrint2.put(alphabet[j], list[j]);
                    }

                    str2 += "For position " + (i + 1) + " --> ";
                    //System.out.print("list for position " + (i + 1) + " is --> ");// + sortByValue(listToPrint));
                    HashMap<Character, Float> sortedList = sortByValue(listToPrint2);
                    sortedList2 = sortedList;
                    int count2 = 0;
                    Iterator<Character> itr = sortedList.keySet().iterator();
                    Character letter2;
                    while (itr.hasNext() && count2 < 5) {
                        letter2 = itr.next();
                        //System.out.print(letter + ":" + sortedList.get(letter) + ", ");
                        str2 += (letter2 + ":" + sortedList.get(letter2) + " ");
                        count2++;
                    }
                    //System.out.println("");
                    str2 += "\n";
                }
            }
            probListsLabel.setText(str2);
            //calculate and show probability lists END-------------------------------------------------
        }catch (ArrayIndexOutOfBoundsException ex){
            displayMsgLabel.setText("Give a proper position\n");
        }catch (NumberFormatException ex){
            displayMsgLabel.setText("Provide a position\n");
        }catch(IllegalArgumentException ex){
            displayMsgLabel.setText("Hey hey, no cheating :p Press start from application menu to start a new game.");
        }catch (Exception e){
            //StringWriter sw = new StringWriter();
            //e.printStackTrace(new PrintWriter(sw));
            //String exceptionAsString = sw.toString();
            displayMsgLabel.setText("Provide a letter from A-Z");
            System.out.println(e);
        }
    }

}
