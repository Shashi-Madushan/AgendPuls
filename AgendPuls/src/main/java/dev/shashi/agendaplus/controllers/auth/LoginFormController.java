package dev.shashi.agendaplus.controllers.auth;

import dev.shashi.agendaplus.db.DbConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginFormController {
    @FXML
    public AnchorPane loginWindow;
    @FXML
    public TextField unameTxtFiled;
    @FXML
    public PasswordField pwdTxtFiled;
    private Stage stage;

    @FXML
    public void initialize() {
        Platform.runLater(() -> unameTxtFiled.requestFocus());
        unameTxtFiled.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                pwdTxtFiled.requestFocus();
            }
        });

        pwdTxtFiled.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                logInBtnOnAction(new ActionEvent());
            }
        });
    }

    public void logInBtnOnAction(ActionEvent actionEvent) {
        String inputUserName = unameTxtFiled.getText();
        String inputPassword = pwdTxtFiled.getText();

        try {
            checkCredential(inputUserName, inputPassword);
        } catch (SQLException | IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void checkCredential(String userName, String pw) throws SQLException, IOException {
        String sql = "SELECT password FROM user WHERE userName = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, userName);
        ResultSet resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            String dbPw = resultSet.getString("password");
            if (pw.equals(dbPw)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "Sorry! Password is incorrect!").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Sorry! User Name can't be found!").show();
        }
    }

    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard/dashboard.fxml"));

        Scene scene = new Scene(rootNode);

        stage = (Stage) loginWindow.getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("Agenda PLUS+");
    }

    public void fogotPwdBtnOnAction(ActionEvent actionEvent) {
        // Implementation for forgot password action
    }
}