package dev.shashi.agendaplus;


import dev.shashi.agendaplus.db.DbConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String formPath;

        if (true) {
            // Path to the login form if the user is found
            formPath = "/view/auth/login_form.fxml";
        } else {
            // Path to the register form if the user is not found
            formPath = "/view/auth/register_form.fxml";
        }

        // Load the appropriate form based on the user's existence in the database
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/dashboard/dashboard.fxml"))));
        stage.setTitle("Agenda PLUS+");
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();

    }

    public static boolean isUserFound() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT COUNT(*) AS userCount FROM user";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("userCount") > 0;
        }
        return false;
    }
}