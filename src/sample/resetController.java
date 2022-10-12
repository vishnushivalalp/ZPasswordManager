package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class resetController implements Initializable {

    @FXML
    private ChoiceBox ques1, ques2, ques3;

    @FXML
    private Button submitButton, cancelButton;

    @FXML
    private TextField ans1, ans2, ans3;

    ObservableList<String> ques = FXCollections.observableArrayList(
            "Where did you born?",
            "What is your first pet's name?",
            "What is your favourite food?",
            "What is your friend's nickname?",
            "What is your favourite book?"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ques1.getItems().addAll(ques);
        ques2.getItems().addAll(ques);
        ques3.getItems().addAll(ques);
    }

    public void goToHome() throws Exception {
        Alert alertmsg = new Alert(Alert.AlertType.CONFIRMATION);
        alertmsg.setTitle("Exit");
        alertmsg.setHeaderText("Are you sure?");
        alertmsg.setContentText("Do you want to exit?");
        Optional<ButtonType> result = alertmsg.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if(button == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage signUp = (Stage) cancelButton.getScene().getWindow();
            signUp.setScene(new Scene(root, 600, 400));
            root.requestFocus();
        }
    }

    public void submitData() throws Exception {
        String a1 = ans1.getText();
        String a2 = ans2.getText();
        String a3 = ans3.getText();

        Boolean check = checkData(a1, a2, a3);
        if (check) {
            System.out.println("Gotcha !!!");
        }
        else {
            System.out.println("Nothing's gonna happen !!!");
        }
    }

    public Boolean checkData(String a1, String a2, String a3) {
        System.out.println(a1 + " " + a2 + " " + a3);
        return false;
    }
}
