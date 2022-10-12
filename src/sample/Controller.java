package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Controller{

    private static int var, check, userID;
    public static String currentUserDB;

    String loginUsername, loginPassword;
    String newNameUser, newEmailId, newUserName, newPassword, newRetypePass, newUrl, newRemark;

    Encrypt_Decrypt ed = new Encrypt_Decrypt();
    homeDBConnection connectNow = new homeDBConnection();
    Connection databaselink = connectNow.getConnection();

    @FXML
    Button signinButton, signupButton, resetButton, signUpButton,
            cancelButton, retrieveButton, editButton, editUserButton;

    @FXML
    TextField uname, newName, newEmail, newUser, newPass,
            reTypeNewPass, addName, addEmail, addUser, addPass, addRetypePass,
            addUrl, addRemark, oldEmail, oldUsername, oldUrl, oldRemark,
            newURL, newREMARK, newUsername, remEmail, remUser, remUrl, remRemark,
            currName, currEmail, currUser, currPass, remPass, oldPass;

    @FXML
    PasswordField pword, oldRetypePass, newPassWord, newReTypePass, remRetypePass;

    public void handleSignIn() {
        loginUsername = uname.getText();
        loginPassword = pword.getText();
        if(loginUsername.isEmpty() || loginPassword.isEmpty()) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Please enter valid details");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        }
        else {
            validateLogin(loginUsername, loginPassword);
        }
    }

    public void goToSignUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage signUp = (Stage) signupButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void goToReset() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("reset.fxml"));
        Stage reset = (Stage) resetButton.getScene().getWindow();
        reset.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }


    public void handleSignUp() throws Exception {
        newNameUser = newName.getText();
        newEmailId = newEmail.getText();
        newUserName = newUser.getText();
        newPassword = newPass.getText();
        newRetypePass = reTypeNewPass.getText();
        if (newNameUser.isEmpty() || newEmailId.isEmpty() ||
                newUserName.isEmpty() || newPassword.isEmpty() || newRetypePass.isEmpty()) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Please fill out the form");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        }
        else if (!newPassword.equals(newRetypePass)) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Passwords doesn't match");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        } else {
            String encrypted_pass = ed.encrypt(newPassword);
            validateSignUp(newNameUser, newEmailId, newUserName, encrypted_pass);
        }
    }

    public void createNewDatabase(String newUserDatabase) {
        String newDBQuery = "CREATE TABLE " + newUserDatabase + "(ID int not null auto_increment primary key, " +
                " EMAIL varchar(50), " + " USERNAME varchar(50), " + " PASSWORD varchar(150), " +
                " URL varchar(50)," + " REMARK varchar(50))";
        try {
            Statement statement = databaselink.createStatement();
            statement.executeUpdate(newDBQuery);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDBName() {
        return currentUserDB;
    }

    public void validateLogin(String loginUsername, String loginPassword) {
        String loginQuery = "SELECT * FROM USERDATABASE WHERE USERNAME = '" + loginUsername + "'";
        String User, Pass;
        Boolean checkValue = false;
        try {
            Statement statement = databaselink.createStatement();
            ResultSet resultSet = statement.executeQuery(loginQuery);
            while (resultSet.next()) {
                userID = resultSet.getInt(1);
                User = resultSet.getString(4);
                Pass = resultSet.getString(5);
                String checkPass = ed.decrypt(Pass);
                if ((User.equals(loginUsername)) && checkPass.equals(loginPassword)) {
                    checkValue = true;
                    currentUserDB = loginUsername;
                }
            }
            if (checkValue == true) {
                Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
                Stage logIn = (Stage) signupButton.getScene().getWindow();
                logIn.setScene(new Scene(root, 600, 400));
                root.requestFocus();
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Sign up required");
                alertmsg.setContentText("User not found");
                alertmsg.showAndWait();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateSignUp(String newNameUser, String newEmailId, String newUserName, String newPassword) {
        String signUpQuery = "INSERT INTO USERDATABASE(LNAME, EMAIL, USERNAME, PASSWORD) " +
                "VALUES('" + newNameUser + "','" +newEmailId + "','" + newUserName + "','" + newPassword + "')";
        try {
            Statement statement = databaselink.createStatement();
            int result = statement.executeUpdate(signUpQuery);
            if (result != 0) {
                createNewDatabase(newUserName);
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Success");
                alertmsg.setHeaderText("Successfully created account");
                alertmsg.setContentText("Sign in to proceed");
                alertmsg.showAndWait();
                check = 1;
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.ERROR);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Error occurred");
                alertmsg.setContentText("Try again");
                alertmsg.showAndWait();
            }
            if (check == 1) {
                newName.setText("");
                newEmail.setText("");
                newUser.setText("");
                newPass.setText("");
                reTypeNewPass.setText("");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleAddData() throws Exception {
        newEmailId = addEmail.getText();
        newUserName = addUser.getText();
        newPassword = addPass.getText();
        newRetypePass = addRetypePass.getText();
        newUrl = addUrl.getText();
        newRemark = addRemark.getText();
        if (newEmailId.isEmpty() || newUserName.isEmpty()
                || newPassword.isEmpty() || newRetypePass.isEmpty() || newUrl.isEmpty() || newRemark.isEmpty()) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Please fill out the form");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        }
        else if (!newPassword.equals(newRetypePass)) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Passwords doesn't match");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        } else {
            String encrypt_pass = ed.encrypt(newPassword);
            validateAddData(newEmailId, newUserName, encrypt_pass, newUrl, newRemark);
        }
    }

    private void validateAddData(String newEmailId, String newUserName, String newPassword, String newUrl, String newRemark) throws IOException{
        String signUpQuery = "INSERT INTO "+  currentUserDB + "(EMAIL, USERNAME, PASSWORD, URL, REMARK) " +
                "VALUES('" + newEmailId + "','" + newUserName + "','" + newPassword + "','" +
                newUrl + "','" + newRemark + "')";
        try {
            Statement statement = databaselink.createStatement();
            int result = statement.executeUpdate(signUpQuery);
            idIncrement();
            if (result != 0) {
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Success");
                alertmsg.setHeaderText("Successfully updated Data");
                alertmsg.setContentText("Proceed to homepage");
                alertmsg.showAndWait();
                check = 1;
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.ERROR);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Error occurred");
                alertmsg.setContentText("Try again");
                alertmsg.showAndWait();
            }
            if (check == 1) {
                addEmail.setText("");
                addUser.setText("");
                addPass.setText("");
                addRetypePass.setText("");
                addUrl.setText("");
                addRemark.setText("");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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

    public void goToSignIn() throws IOException {
        Alert alertmsg = new Alert(Alert.AlertType.CONFIRMATION);
        alertmsg.setTitle("Exit");
        alertmsg.setHeaderText("Are you sure?");
        alertmsg.setContentText("Do you wanna leave?");
        Optional<ButtonType> result = alertmsg.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if(button == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage signUp = (Stage) signinButton.getScene().getWindow();
            signUp.setScene(new Scene(root, 600, 400));
            root.requestFocus();
        }
    }

    public void goToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        Stage signUp = (Stage) cancelButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void goToEditPage() throws Exception {
        String query = "SELECT * FROM " + currentUserDB + " WHERE ID = '" + var + "'";
        try {
            ResultSet resultSet = databaselink.createStatement().executeQuery(query);
            while (resultSet.next()) {
                String pass = resultSet.getString(4);
                String decypt_pass = ed.decrypt(pass);
                oldEmail.setText(resultSet.getString(2));
                oldUsername.setText(resultSet.getString(3));
                oldPass.setText(decypt_pass);
                oldRetypePass.setText(decypt_pass);
                oldUrl.setText(resultSet.getString(5));
                oldRemark.setText(resultSet.getString(6));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void setVariable(int x) {
        var = x;
    }

    public void handleEditData() throws Exception {
        newEmailId = newEmail.getText();
        newUserName = newUsername.getText();
        newPassword = newPassWord.getText();
        newRetypePass = newReTypePass.getText();
        newUrl = newURL.getText();
        newRemark = newREMARK.getText();
        if (newEmailId.isEmpty() || newUserName.isEmpty()
                || newPassword.isEmpty() || newRetypePass.isEmpty() || newUrl.isEmpty() || newRemark.isEmpty()) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Please fill out the form");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        }
        else if (!newPassword.equals(newRetypePass)) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Passwords doesn't match");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        } else {
            String encrypt_pass = ed.encrypt(newPassword);
            validateEditData(newEmailId, newUserName, encrypt_pass, newUrl, newRemark);
        }
    }

    public void validateEditData(String newEmailId, String newUserName, String newPassword, String newUrl, String newRemark) {
        String editDataQuery = "UPDATE " + currentUserDB + " SET EMAIL = '" + newEmailId + "', " +
                "USERNAME = '" + newUserName + "', PASSWORD = '" + newPassword + "', " +
                "URL = '" + newUrl + "', REMARK = '" + newRemark + "'  where ID = '" + var + "'";
        try {
            Statement statement = databaselink.createStatement();
            int result = statement.executeUpdate(editDataQuery);
            if (result != 0) {
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Success");
                alertmsg.setHeaderText("Successfully updated data");
                alertmsg.setContentText("Proceed to homepage");
                alertmsg.showAndWait();
                check = 1;
                goToHome();
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.ERROR);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Error occurred.\nCrosscheck ID");
                alertmsg.setContentText("Try again");
                alertmsg.showAndWait();
            }
            if (check == 1) {
                newEmail.setText("");
                newUsername.setText("");
                newPassWord.setText("");
                newReTypePass.setText("");
                newURL.setText("");
                newREMARK.setText("");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateRemoveData() {
        Boolean set = false;
        String removeDataQuery = "DELETE FROM " + currentUserDB + " WHERE ID = '" + var + "'";
        try {
            Statement statement = databaselink.createStatement();
            int result = statement.executeUpdate(removeDataQuery);
            idIncrement();
            if (result != 0) {
                set = true;
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Success");
                alertmsg.setHeaderText("Successfully removed Data");
                alertmsg.setContentText("Proceed to homepage");
                alertmsg.showAndWait();
                goToHome();
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.ERROR);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Error occurred.\nCrosscheck ID");
                alertmsg.setContentText("Try again");
                alertmsg.showAndWait();
            }
            if (set == true) {
                homeDBController hDBC = new homeDBController();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void idIncrement() {
        String idIncrementQuery = "ALTER TABLE " + currentUserDB + " AUTO_INCREMENT = 1";
        try {
            Statement statement = databaselink.createStatement();
            statement.executeUpdate(idIncrementQuery);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RetrieveRemoveData() throws Exception {
        String query = "SELECT * FROM " + currentUserDB + " WHERE ID = '" + var + "'";
        try {
            Connection connectdb = connectNow.getConnection();
            ResultSet resultSet = connectdb.createStatement().executeQuery(query);
            while (resultSet.next()) {
                String pass = resultSet.getString(4);
                String decrypt_pass = ed.decrypt(pass);
                remEmail.setText(resultSet.getString(2));
                remUser.setText(resultSet.getString(3));
                remPass.setText(decrypt_pass);
                remRetypePass.setText(decrypt_pass);
                remUrl.setText(resultSet.getString(5));
                remRemark.setText(resultSet.getString(6));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void goToEditUser() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("editUserData.fxml"));
        Stage signUp = (Stage) editUserButton.getScene().getWindow();
        signUp.setScene(new Scene(root, 600, 400));
        root.requestFocus();
    }

    public void validateEditUserData(String newnameuser, String newemailid, String newusername, String newpassword) {
       String editUserDataQuery = "UPDATE USERDATABASE SET LNAME = '" + newnameuser +
               "', EMAIL = '" + newemailid + "', USERNAME = '" + newusername + "', PASSWORD = '" + newpassword +
               "' WHERE ID = " + userID;
       String dbRenameQuery = "RENAME TABLE " + currentUserDB + " TO " + newusername;

        try {
            Statement statement = databaselink.createStatement();
            int result = statement.executeUpdate(editUserDataQuery);
            if (result != 0) {
                Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
                alertmsg.setTitle("Success");
                alertmsg.setHeaderText("Successfully updated data");
                alertmsg.setContentText("Proceed to homepage");
                alertmsg.showAndWait();
                goToHome();
            }
            else {
                Alert alertmsg = new Alert(Alert.AlertType.ERROR);
                alertmsg.setTitle("Error");
                alertmsg.setHeaderText("Error occurred.");
                alertmsg.setContentText("Try again");
                alertmsg.showAndWait();
            }
            statement.executeUpdate(dbRenameQuery);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editUserData() throws Exception {
        newNameUser = addName.getText();
        newEmailId = addEmail.getText();
        newUserName = addUser.getText();
        newPassword = addPass.getText();
        newRetypePass = addRetypePass.getText();
        if (newNameUser.isEmpty() || newEmailId.isEmpty() || newUserName.isEmpty()
                || newPassword.isEmpty() || newRetypePass.isEmpty()) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Please fill out the form");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        }
        else if (!newPassword.equals(newRetypePass)) {
            Alert alertmsg = new Alert(Alert.AlertType.INFORMATION);
            alertmsg.setTitle("Missing Information");
            alertmsg.setHeaderText("Passwords doesn't match");
            alertmsg.setContentText("Fields are mandatory");
            alertmsg.showAndWait();
        } else {
            String encrypt_pass = ed.encrypt(newPassword);
            validateEditUserData(newNameUser, newEmailId, newUserName, encrypt_pass);
        }
    }

    public void retrieveUserData() throws Exception {
        String retrieveUserQuery = "SELECT * FROM USERDATABASE WHERE USERNAME = '" + currentUserDB + "'";
        try {
            Connection connectdb = connectNow.getConnection();
            ResultSet resultSet = connectdb.createStatement().executeQuery(retrieveUserQuery);
            while (resultSet.next()) {
                String pass = resultSet.getString(5);
                String decrypt_pass = ed.decrypt(pass);
                currName.setText(resultSet.getString(2));
                currEmail.setText(resultSet.getString(3));
                currUser.setText(resultSet.getString(4));
                currPass.setText(decrypt_pass);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}