package dev.shashi.agendaplus.controllers.auth;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import dev.shashi.agendaplus.db.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFormController {


    @FXML
    private PasswordField confPwdTxtFiled1;

    @FXML
    private AnchorPane loginWindow;

    @FXML
    private PasswordField pwdTxtFiled;

    @FXML
    private JFXButton registerBtn;



    @FXML
    private TextField secuQuesAnswerTxtFiled;

    @FXML
    private TextField unameTxtFiled;
    String userName;
    String password;
    String confPassword;
    int secQuestionNo;
    String secQuesAnswer;
    @FXML
    private JFXComboBox<String> secQuesDropDown;

    @FXML
    public void initialize() {
        // Populate the security questions dropdown
        ObservableList<String> securityQuestions = FXCollections.observableArrayList(
                "What is your mother's maiden name?",
                "What was the name of your first pet?",
                "What was the make of your first car?"
        );
        secQuesDropDown.setItems(securityQuestions);
    }



    @FXML
    void regiterBtnOnAction(ActionEvent event) {
        userName = unameTxtFiled.getText();
        password = pwdTxtFiled.getText();
        confPassword = confPwdTxtFiled1.getText();
        secQuestionNo = secQuesDropDown.getSelectionModel().getSelectedIndex();
        secQuesAnswer = secuQuesAnswerTxtFiled.getText();

        System.out.println(userName);
        System.out.println(password);
        System.out.println(confPassword);
        if (userName.isEmpty() && password.isEmpty() && confPassword.isEmpty() && secQuesAnswer.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are empty. Please fill in the details.").show();
        } else {
            if (!password.equals(confPassword) || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Passwords are not matching or are empty.").show();
                unameTxtFiled.clear();
                pwdTxtFiled.clear();
                confPwdTxtFiled1.clear();
            } else {
                try {
                    boolean isSaved = saveUser(userName, password, secQuestionNo, secQuesAnswer);
                    if (isSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "User saved!").show();
                        navigateToTheDashboard();
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
    }

    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard/dashboard.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.loginWindow.getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("Agenda PLUS+");
    }

    private boolean saveUser(String userName, String password, int secQuestionNo, String secQuesAnswer) throws SQLException {
        String sql = "INSERT INTO user VALUES(?, ?, ?,?)";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setObject(1,userName);
        pstmt.setObject(2,password);
        pstmt.setObject(3,secQuestionNo);
        pstmt.setObject(4,secQuesAnswer);

        return pstmt.executeUpdate() >0;

    }

}
