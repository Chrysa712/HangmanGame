package hangman;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopUpRoundsController {

    @FXML
    private Label roundsLabel;

    public void displayRounds(String str){
        if(str == "") roundsLabel.setText("No Games Played!");
        else roundsLabel.setText(str);
    }

}
