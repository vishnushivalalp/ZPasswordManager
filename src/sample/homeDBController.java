package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class homeDBController implements Initializable {

    int var, checkId;
    Controller obj = new Controller();
    homeDBConnection connectnow = new homeDBConnection();

    @FXML
    Button addData, signOutButton, exitButton, editDataButton, removeDataButton, accButton, editUserButton;

    @FXML
    private TableView<homeDatas> homeTable;

    @FXML
    private TableColumn<homeDatas, Integer> id;

    @FXML
    private TableColumn<homeDatas, String> tusername;

    @FXML
    private TableColumn<homeDatas, String> email;

    @FXML
    private TableColumn<homeDatas, String> tpassword;

    @FXML
    private TableColumn<homeDatas, String> url;

    @FXML
    private TableColumn<homeDatas, String> remark;

    ObservableList<homeDatas> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String dbName = obj.getDBName();
        String query = "SELECT * FROM " + dbName;
        try {
            Connection connectdb = connectnow.getConnection();
            ResultSet resultSet = connectdb.createStatement().executeQuery(query);
            while (resultSet.next()) {
                list.add(new homeDatas(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("email"), resultSet.getString("password"),
                        resultSet.getString("url"),
                        resultSet.getString("remark")));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        id.setCellValueFactory(new PropertyValueFactory<homeDatas, Integer>("id"));
        tusername.setCellValueFactory(new PropertyValueFactory<homeDatas, String>("username"));
        email.setCellValueFactory(new PropertyValueFactory<homeDatas, String>("email"));
        tpassword.setCellValueFactory(new PropertyValueFactory<homeDatas, String>("password"));
        url.setCellValueFactory(new PropertyValueFactory<homeDatas, String>("url"));
        remark.setCellValueFactory(new PropertyValueFactory<homeDatas, String>("remark"));
        homeTable.setItems(list);
    }

    public void goToAddData() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addData.fxml"));
        Stage signUp = (Stage) addData.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void handleExit() {
        Alert alertmsg = new Alert(Alert.AlertType.CONFIRMATION);
        alertmsg.setTitle("Exit");
        alertmsg.setHeaderText("Are you sure?");
        alertmsg.setContentText("Do you want to exit?");
        Optional<ButtonType> result = alertmsg.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if(button == ButtonType.OK) {
            Platform.exit();
        }
    }

    public void goToLoginPage() throws IOException {
        Alert alertmsg = new Alert(Alert.AlertType.CONFIRMATION);
        alertmsg.setTitle("Exit");
        alertmsg.setHeaderText("Are you sure?");
        alertmsg.setContentText("Do you want to signout?");
        Optional<ButtonType> result = alertmsg.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if(button == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage signUp = (Stage) exitButton.getScene().getWindow();
            signUp.setScene(new Scene(root, 600, 400));
            root.requestFocus();
        }
    }

    public void goToEditData() throws IOException {
        Boolean isNumeric = true;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Data");
        dialog.setHeaderText("ID Confirmation");
        dialog.setContentText("Please enter the ID:");
        Optional<String> result = dialog.showAndWait();
        try {
            var = Integer.parseInt(result.get());
            obj.setVariable(var);
        }catch (NumberFormatException e) {
            isNumeric = false;
        }

        if (!isNumeric) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Error");
            alertmsg.setHeaderText("ID Is a Numeric Value");
            alertmsg.setContentText("Please Enter a Valid ID");
            alertmsg.showAndWait();
        }
        else {
             Boolean test = checkData();
             if (test) {
                 displayEditPage();
             }
            else {
                 Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                 alertmsg.setTitle("Error");
                 alertmsg.setHeaderText("Wrong ID");
                 alertmsg.setContentText("Please enter a valid ID");
                 alertmsg.showAndWait();
             }
        }
    }

    public boolean checkData() {
        Controller ctrl = new Controller();
        Connection databaselink = connectnow.getConnection();
        String idCheckQuery = "SELECT ID FROM " + ctrl.currentUserDB + " WHERE ID = " + var;
        try {
            Statement statement = databaselink.createStatement();
            ResultSet resultSet = statement.executeQuery(idCheckQuery);
            while (resultSet.next()) {
                checkId = resultSet.getInt(1);
                if (checkId == var) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void displayEditPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("editData.fxml"));
        Stage signUp = (Stage) editDataButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void goToRemoveData() throws IOException{
        Boolean isNumeric = true;
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Data");
        dialog.setHeaderText("ID Confirmation");
        dialog.setContentText("Please enter the ID:");
        Optional<String> result = dialog.showAndWait();
        try {
            var = Integer.parseInt(result.get());
            obj.setVariable(var);
        }catch (NumberFormatException e) {
            isNumeric = false;
        }

        if (!isNumeric) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Error");
            alertmsg.setHeaderText("ID Is a Numeric Value");
            alertmsg.setContentText("Please Enter a Valid ID");
            alertmsg.showAndWait();
        }
        else {
            Boolean test = checkData();
            if (test) {
                displayRemovePage();
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Wrong ID");
                alertmsg.setContentText("Please enter a valid ID");
                alertmsg.showAndWait();
            }
        }
    }

    public void displayRemovePage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("removeData.fxml"));
        Stage signUp = (Stage) removeDataButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void goToAccount() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("account.fxml"));
        Stage signUp = (Stage) accButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }
}