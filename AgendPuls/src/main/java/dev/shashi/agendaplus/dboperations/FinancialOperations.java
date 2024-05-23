package dev.shashi.agendaplus.dboperations;

import dev.shashi.agendaplus.db.DbConnection;
import dev.shashi.agendaplus.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FinancialOperations {
    public ObservableList<Transaction> getAllTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = "SELECT t.transactionId, t.amount, t.type, t.date, t.description, t.categoryId, c.name AS categoryName FROM transactions t JOIN categories c ON t.categoryId = c.id";
        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transactionId"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("type"),
                        rs.getDate("date"),
                        rs.getString("description"),
                        rs.getInt("categoryId")

                );
                transaction.setCategoryName(rs.getString("categoryName"));
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void createTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transactionId, amount, type, date, description, categoryId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setInt(1, transaction.getTransactionId());
            pstm.setBigDecimal(2, transaction.getAmount());
            pstm.setInt(3, transaction.getType());
            pstm.setDate(4, new java.sql.Date(transaction.getDate().getTime()));
            pstm.setString(5, transaction.getDescription());
            pstm.setInt(6, transaction.getCategoryId());

            pstm.executeUpdate();
        }
    }

    public static void updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET amount = ?, type = ?, date = ?, description = ?, categoryId = ? WHERE transactionId = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setBigDecimal(1, transaction.getAmount());
        pstm.setInt(2, transaction.getType());
        pstm.setDate(3, new java.sql.Date(transaction.getDate().getTime()));
        pstm.setString(4, transaction.getDescription());
        pstm.setInt(5, transaction.getCategoryId());
        pstm.setInt(6, transaction.getTransactionId());

        pstm.executeUpdate();
    }

    public static void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transactionId = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setInt(1, transactionId);

        int rowsAffected = pstm.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Transaction deleted successfully.");
        } else {
            System.out.println("No Transaction found with ID: " + transactionId);
        }
    }
}
