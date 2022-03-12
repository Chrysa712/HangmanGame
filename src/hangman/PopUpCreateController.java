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

public class PopUpCreateController {

    @FXML
    private TextField OLIDtoCreateLabel;
    @FXML
    private TextField dictIDtoCreateLabel;
    @FXML
    private Label outCreateLabel;
    @FXML
    private Button popupCreateSubmitBtn;
    @FXML
    void pressedSubmit(ActionEvent event) {
        String dictid = dictIDtoCreateLabel.getText().toString();
        String olid = OLIDtoCreateLabel.getText().toString();
        //dict = returnDict(dictid, olid).toString();
        AnchorPane pane;
        try{
            String[] dict = GameController.returnDict(dictid, olid);
            outCreateLabel.setText("File created: hangman_DICTIONARΥ-"+ dictid +".txt");

            //new code
            JsonReader.wordCountCheck(dict);
            JsonReader.balanceCheck(dict);
            //end of new code

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameFile.fxml"));
            Parent root = loader.load();
            GameController gameController = loader.getController();
            gameController.displaySelectedDict(dictid);

            //...
            Stage stagepopup = (Stage) popupCreateSubmitBtn.getScene().getWindow();
            //stagepopup.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Medialab Hangman");
            stage.show();

        }catch (UndersizeException e) {
            outCreateLabel.setText("Undersized Dictionary.\nProvide another OLID\n");
            HandleFiles.DeleteFile(dictid);
        }catch (UnbalancedException e) {
            outCreateLabel.setText("Unbalanced Dictionary.\nProvide another OLID\n");
            HandleFiles.DeleteFile(dictid);
        }catch (ClassCastException e){
            outCreateLabel.setText("The given OLID does not have a json with fields {___, 'description':{..., 'value':'...', ...}, ___} ");
            HandleFiles.DeleteFile(dictid);
        }catch (FileNotFoundException e){
            outCreateLabel.setText("No dictionary with OLID:"+ olid+" exists.\nProvide another OLID\n");
            HandleFiles.DeleteFile(dictid);
        }catch (Exception e){
            System.out.println(e.toString());
            outCreateLabel.setText(e.toString());
            //StringWriter sw = new StringWriter();
            //e.printStackTrace(new PrintWriter(sw));
            //String exceptionAsString = sw.toString();
            //outCreateLabel.setText(exceptionAsString);
        }

    }

}
