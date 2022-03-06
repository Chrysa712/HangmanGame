package hangman;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class Game {
    //final InputStream in, final OutputStream out, final String[] WORDS

    private static Boolean BelongsInSubset(int[] HitPositions, String ChosenWord, String OtherWord){
        int appropriateLength = ChosenWord.length();
        int otherLength = OtherWord.length();
        if (appropriateLength != otherLength) return false;

        boolean allZero = true;
        for (int i=0; i<HitPositions.length; i++) {
            if (HitPositions[i] != 0) {
                allZero = false;
                break;
            }
        }
        if(allZero) return allZero;
        boolean flag = false;
        for (int i=0; i<HitPositions.length; i++){
            if (HitPositions[i]==1 && OtherWord.charAt(i) == ChosenWord.charAt(i)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    //the probability for a specific character is Prob(char Y) = #WordsInTheSubsetThatHaveCharYInSpace / #WordsInSubset
    //#WordsInSubset = WordsWithSameLengthAsTheChosenOne && WordsThatHaveCommonRevealedCharsWithTheChosen
    public static float[] CreateProbabilityListForXEmptySpace(int[] hitPositions, String word, String[] WORDS, Integer space){

        //create alphabet array
        //char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
        //create frequency array for letters and initialize it with 0
        float[] freq = new float[26];
        for(int i=0; i<26; i++) freq[i] = 0;

        //traverse the words in the dictionary, see if they belong in the subset, fix subset counter
        //also fix the frequency array
        List<String> newWords = new ArrayList<>(Arrays.asList(WORDS));
        Iterator<String> itr = newWords.iterator();
        int subsetlen = 0;
        while(itr.hasNext()){
            String helper = itr.next();
            if (BelongsInSubset(hitPositions, word, helper) /*&& !helper.equals(word)*/) {
                freq[helper.charAt(space) - 65]++;
                subsetlen++;
            }
        }

        //calculate the probability
        //System.out.println(Arrays.toString(freq));
        for(int i=0; i<freq.length; i++){
            freq[i] = freq[i]/(subsetlen/*+1*/);
        }
        //System.out.println(Arrays.toString(freq));

        return freq;
    }


    public static String[] returnDict() throws InvalidCountException, InvalidRangeException, UndersizeException, UnbalancedException, IOException {
        System.out.println("Provide the Dictionary ID");
        Scanner s = new Scanner(System.in);
        String dictID = s.nextLine();
        //try {
        if (!HandleFiles.FileExists(dictID)) {
            HandleFiles.CreateFile(dictID);
            System.out.println("Provide the OL ID");
            String OL_ID = s.nextLine();
            String helper = JsonReader.createWordsOfDict(OL_ID);
            HandleFiles.WriteFile(dictID, helper);
            HandleFiles.RemoveBlankLine(dictID);
        }
        return HandleFiles.ReadFile(dictID);
       // }
        //catch (UnbalancedException e){ HandleFiles.DeleteFile(dictID);}
        //catch (UndersizeException o){HandleFiles.DeleteFile(dictID);}
        //return HandleFiles.ReadFile(dictID);
    }

    public static HashMap<Character, Float> sortByValue(HashMap<Character, Float> hm){
        // Create a list from elements of HashMap
        List<Map.Entry<Character, Float> > list = new LinkedList<Map.Entry<Character, Float> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Character, Float> >() {
            public int compare(Map.Entry<Character,Float> o1, Map.Entry<Character,Float> o2)
            {
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

    public static void startGame(final InputStream input, final OutputStream output, String[] WORDS) throws InvalidRangeException { //}public void exec() {

//        try{
//            JsonReader.lessThan6Check(String.valueOf(WORDS));
//        }catch (InvalidRangeException e){
//            System.out.println("The dict must be deleted");
//        }

        if (WORDS[0]=="") throw new InvalidRangeException();

        String word = WORDS[new Random().nextInt(WORDS.length)];
        boolean[] visible = new boolean[word.length()];
        int mistakes = 0;
        int points = 0;
        float probabilityHelpForPoints;
        try (final PrintStream out = new PrintStream(output)) {
            final Iterator<String> scanner = new Scanner(input);
            boolean done = true;
            /**/
            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
            List<Character> alBe = new ArrayList<>();
            for(char ch: alphabet)  alBe.add(ch);
            int[] hitPositions = new int[word.length()];
            /**/
            while (mistakes < 6) {
                done = true;
                for (int i = 0; i < word.length(); ++i) {
                    if (!visible[i]) {
                        done = false;
                    }
                }
                if (done) {
                    break;
                }

                HashMap<Character, Float> listToPrint = new HashMap<Character, Float>();
                HashMap<Character,Float> sortedList2 = new HashMap<Character,Float>();
                char letter2;
                out.println("Choose a letter for a specific position based on the lists:");
                //calculate and show probability lists
                for (int i=0; i<word.length(); i++){
                    //if condition to show only non found letters probabilities
                    if(hitPositions[i]==0) {
                        //System.out.println(Arrays.toString(hitPositions));
                        float[] list = CreateProbabilityListForXEmptySpace(hitPositions, word, WORDS, i);

                        //Create tuple list [(A,prob(A)), (B,prob(B)), ...]
                        //sort it based on the probabilities and then print the first word.length() elements
                        for(int j=0; j<list.length; j++){
                            listToPrint.put(alphabet[j], list[j]);
                        }
                        out.print("list for letter in position " + (i + 1) + " is --> ");// + sortByValue(listToPrint));
                        HashMap<Character,Float> sortedList = sortByValue(listToPrint);
                        sortedList2 = sortedList;
                        int count = 0;
                        Iterator<Character> itr = sortedList.keySet().iterator();
                        Character letter;
                        while (itr.hasNext() && count < 5) {
                            letter = itr.next();
                            System.out.print(letter + ":" + sortedList.get(letter) +", ");
                            count++;
                        }
                        System.out.println("");
                    }
                }

                char chr = scanner.next().charAt(0);
                chr = Character.toUpperCase(chr);
                letter2 = chr;
                //list.remove(Character.valueOf(chr));

                out.println("Choose the position:");
                Scanner sc1 = new Scanner(System.in);
                int pos = sc1.nextInt()-1;
                while (hitPositions[pos] == 1 ){
                    out.println("Give position that isn't already visible:");
                    pos = sc1.nextInt()-1;
                }

                boolean hit = false;
                //for (int i = 0; i < word.length(); ++i) {
                if (word.charAt(pos) == chr && !visible[pos]) {
                    visible[pos] = true;
                    hit = true;
                    //hitPositions from [0,0,0,0,...] becomes [0,0,1,0,...]
                    hitPositions[pos]=1;
                }
                //}
                if (hit) {
                    out.print("Hit!\n");
                    //Point system: Prob>=0.6 --> 5, 0.6>Prob>=0.4 --> 10, 0.4>Prob>=0.25 --> 15, 0.25>Prob>=0 --> 30
                    probabilityHelpForPoints = sortedList2.get(letter2);
                    if(probabilityHelpForPoints<0.25)
                        points += 30;
                    else{
                        if (probabilityHelpForPoints<0.4)
                            points += 15;
                        else {
                            if (probabilityHelpForPoints < 0.6)
                                points += 10;
                            else {
                                points += 5;
                            }
                        }
                    }
                    System.out.println("points now "+points);
                } else {
                    out.printf(
                            "Missed, mistake #%d out of %d\n",
                            mistakes + 1, 6
                    );
                    ++mistakes;
                    //Point system: points -= 15 for every mistake never<0
                    points -= 15;
                    if (points<0) points=0;
                }
                out.append("The word: ");
                for (int i = 0; i < word.length(); ++i) {
                    if (visible[i]) {
                        out.print(word.charAt(i));
                    } else {
                        out.print("_");
                    }
                }
                out.append("\n\n");
            }
            if (done) {
                out.print("You won!\nYour points are "+points);
            } else {
                out.print("You lost.\nThe hidden word is "+word);
            }
        }
    }

    public static void Start() throws InvalidCountException, InvalidRangeException, UndersizeException, UnbalancedException, IOException {
        startGame(System.in, System.out, returnDict());
    }

}
