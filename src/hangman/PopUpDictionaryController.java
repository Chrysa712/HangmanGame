package hangman;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopUpDictionaryController {

    @FXML
    private Label dictDetailsLabel;

    public void displayInfo(String str){
        dictDetailsLabel.setText(str);
    }

}
