package dev.shashi.agendaplus.repo;

import dev.shashi.agendaplus.db.DbConnection;
import dev.shashi.agendaplus.model.Task;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class TaskRepo {

    public static ArrayList<Task> getTasks(LocalDate date) {
        ArrayList<Task> taskList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT task_id, title, description, status FROM tasks WHERE date = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, date); // Set LocalDate directly
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int taskId = rs.getInt("task_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                boolean status = rs.getBoolean("status");
                Task task = new Task( title, description,status, date);
                task.setTaskId(taskId);
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public static int getTaskCount(LocalDate date){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT count(*) FROM tasks WHERE date = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, date);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Correctly retrieve the count from the first column
            }
            return 0; // Return 0 if no rows are found
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getDoneTaskCount(LocalDate date) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT count(*) FROM tasks WHERE date = ? AND status = TRUE";
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, date);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Retrieve the count from the first column
            }
            return 0; // Return 0 if no rows are found
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int saveTask(Task task) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int taskID = -1; // Initialize taskID to -1 to indicate failure
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO tasks (title, description, status, date) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isDone());
            pstmt.setObject(4, task.getDate());

            int affectedRows = pstmt.executeUpdate();
            System.out.println("task saved");
            if (affectedRows > 0) {
                ResultSet resultSet = pstmt.getGeneratedKeys();
                if (resultSet.next()) {
                    taskID = resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskID;
    }

    public static boolean deleteTask(int taskId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM tasks WHERE task_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTaskStatus(int taskId, boolean doneStatus) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "UPDATE tasks SET status = ? WHERE task_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, doneStatus);
            pstmt.setInt(2, taskId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public static ArrayList<LocalDate> getTaskDates(YearMonth yearMonth, LocalDate firstDayOfMonth) {
        ArrayList<LocalDate> taskDays = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT DISTINCT date FROM tasks WHERE date BETWEEN ? AND ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(firstDayOfMonth));
            pstmt.setDate(2, java.sql.Date.valueOf(yearMonth.atEndOfMonth()));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getObject("date", LocalDate.class);
                taskDays.add(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDays;
    }


    public static ArrayList<LocalDate> getTaskDates(LocalDate startOfWeek) {
        ArrayList<LocalDate> taskDays = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT DISTINCT date FROM tasks WHERE date >= ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, java.sql.Date.valueOf(startOfWeek));
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getObject("date", LocalDate.class);
                taskDays.add(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDays;
    }

    public static boolean updateTask(Task task) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, date = ? WHERE task_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setBoolean(3, task.isDone());
            pstmt.setObject(4, task.getDate());
            pstmt.setInt(5, task.getTaskId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}