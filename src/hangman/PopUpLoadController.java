package hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class PopUpLoadController {

    @FXML
    private TextField dictIDtoLoadLabel;

    @FXML
    private Label outLoadLabel;

    @FXML
    private Button popupLoadSubmitBtn;

    @FXML
    public void pressedSubmit2(ActionEvent event) {
        String dictid = dictIDtoLoadLabel.getText().toString();
        AnchorPane pane;
        try{
            String[] dict = GameController.returnDict2(dictid);//.toString();
            outLoadLabel.setText("File to be used: hangman_DICTIONARΥ-"+ dictid +".txt");

            //new code
            JsonReader.duplicatesCheck(dict);
            JsonReader.lessThan6Check(dict);
            JsonReader.wordCountCheck(dict);
            JsonReader.balanceCheck(dict);
            //end of new code

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameFile.fxml"));
            Parent root = loader.load();
            GameController gameController = loader.getController();
            gameController.displaySelectedDict(dictid);

            //...
            Stage stagepopup = (Stage) popupLoadSubmitBtn.getScene().getWindow();
            //stagepopup.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Medialab Hangman");
            stage.show();


        } catch (FileNotFoundException e){
            outLoadLabel.setText("The dictionary with ID '"+dictid+"' does not exist");
        } catch (InvalidCountException e){
            outLoadLabel.setText("The dictionary with ID '"+dictid+"' contains duplicates.\nProvide another OLID");
        } catch (InvalidRangeException e){
            outLoadLabel.setText("The dictionary with ID '"+dictid+"' contains words with less than 6 letters.\nProvide another OLID");
        } catch (UndersizeException e) {
            outLoadLabel.setText("The dictionary with ID '"+dictid+"' is Undersized (has less than 20 words).\nProvide another OLID\n");
        } catch (UnbalancedException e) {
            outLoadLabel.setText("The dictionary with ID '"+dictid+"' is Unbalanced.\nProvide another OLID\n");
        } catch (Exception e){
            System.out.println(e);
            outLoadLabel.setText(e.toString());
            //StringWriter sw = new StringWriter();
            //e.printStackTrace(new PrintWriter(sw));
            //String exceptionAsString = sw.toString();
            //outLoadLabel.setText(exceptionAsString);
        }
    }

}
