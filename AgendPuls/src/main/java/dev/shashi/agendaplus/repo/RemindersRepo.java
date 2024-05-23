package dev.shashi.agendaplus.repo;

import dev.shashi.agendaplus.db.DbConnection;
import dev.shashi.agendaplus.model.Reminder;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RemindersRepo {

    private static final String INSERT_REMINDER_SQL = "INSERT INTO reminders (task_id, taskTitle, reminderDate,reminderTime) VALUES (?, ?, ?,?)";


    public static boolean saveReminder(Reminder reminder) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(INSERT_REMINDER_SQL);
            pstmt.setInt(1, reminder.getTaskId());
            pstmt.setString(2, reminder.getTaskTitle());
            pstmt.setDate(3, new java.sql.Date(reminder.getDate().getTime()));
            pstmt.setTime(4, Time.valueOf(reminder.getReminderTime()));
            int affectedRows = pstmt.executeUpdate();
            System.out.println("remindes saved");
            return affectedRows > 0;
        } catch (SQLException e) {
            // Handle exception
            throw e;
        }
    }

    public static List<Reminder> getReminders(LocalDate filterDate) throws SQLException {
        List<Reminder> reminders = new ArrayList<>();
        String SELECT_REMINDERS_BY_DATE_SQL = "SELECT * FROM reminders WHERE reminderDate = ? AND shown = 0";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(SELECT_REMINDERS_BY_DATE_SQL);
            pstmt.setDate(1, java.sql.Date.valueOf(filterDate));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String taskTitle = rs.getString("taskTitle");
                Date date = rs.getDate("reminderDate");
                Time time = rs.getTime("reminderTime");

                reminders.add(new Reminder(taskId, taskTitle, date.toLocalDate(), time.toLocalTime()));
            }
        } catch (SQLException e) {
            // Handle exception
            throw e;
        }
        return reminders;
    }

    public static void setStatus(int taskId, boolean status) throws SQLException {
        String UPDATE_STATUS_SQL = "UPDATE reminders SET shown = ? WHERE task_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            pstmt = conn.prepareStatement(UPDATE_STATUS_SQL);
            pstmt.setBoolean(1, status);
            pstmt.setInt(2, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
            throw e;
        }
    }


}