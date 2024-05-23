package dev.shashi.agendaplus.dboperations;


import dev.shashi.agendaplus.db.DbConnection;
import dev.shashi.agendaplus.model.Atachment;
import dev.shashi.agendaplus.model.Reminder;
import dev.shashi.agendaplus.model.Task;
import dev.shashi.agendaplus.repo.RemindersRepo;
import dev.shashi.agendaplus.repo.TaskRepo;
import dev.shashi.agendaplus.repo.AtachmentsRepo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class CalendarOperations {




    public static boolean trySaveTest(Task task, ArrayList<Atachment> atachments, Reminder reminder) throws SQLException {
        boolean result = false;


        Connection conn = DbConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false); // disable auto-commit
            int taskId = TaskRepo.saveTask(task);
            if (taskId != -1) {
                boolean allAttachmentsSaved = true;
                for (Atachment atachment : atachments) {
                    atachment.setTaskId(taskId);
                    if (!AtachmentsRepo.saveAtachments(atachment)) {
                        allAttachmentsSaved = false;
                        break;
                    }
                }
                if (allAttachmentsSaved) {
                    reminder.setTaskId(taskId);
                    if (RemindersRepo.saveReminder(reminder)) {
                        conn.commit(); // commit all changes
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        } finally {
            conn.setAutoCommit(true); // re-enable auto-commit
        }

        return result;
    }
}