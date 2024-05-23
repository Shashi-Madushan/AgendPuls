package dev.shashi.agendaplus.repo;

import dev.shashi.agendaplus.db.DbConnection;
import dev.shashi.agendaplus.model.Atachment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtachmentsRepo {

    public static boolean saveAtachments(Atachment atachment) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "INSERT INTO attachments (file_path ,task_id ) VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, atachment.getFilePath());
            pstmt.setInt(2, atachment.getTaskId());
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Atachment saved");
            result = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static List<Atachment> getAtachments(int taskId) {
        List<Atachment> atachments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "SELECT * FROM attachments WHERE task_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, taskId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int atachmentId = rs.getInt("attachment_id");
                String filePath = rs.getString("file_path");
                Atachment atachment = new Atachment(taskId, filePath);
                atachment.setAtachmentId(atachmentId);
                atachments.add(atachment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return atachments;
    }

    public static boolean deleteAttachmentById(int attachmentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        try {
            conn = DbConnection.getInstance().getConnection();
            String sql = "DELETE FROM attachments WHERE attachment_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, attachmentId);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Attachment deleted");
            result = affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
