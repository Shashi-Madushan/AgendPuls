package dev.shashi.agendaplus.reminders;

import dev.shashi.agendaplus.controllers.HomeController;
import dev.shashi.agendaplus.model.Reminder;
import dev.shashi.agendaplus.repo.RemindersRepo;
import javafx.application.Platform;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderManager {
    private static LocalDate today = LocalDate.now();


    public static List<Reminder> reminders = new ArrayList<>();

    public static HomeController homeController;

    public static void initialize() {
        loadRemindersFromDatabase();
    }

    private static void loadRemindersFromDatabase() {
        try {
            reminders = RemindersRepo.getReminders(today);
            System.out.println("Number of reminders loaded: " + reminders.size());
            scheduleReminders();
        } catch (SQLException e) {
            System.err.println("Failed to load reminders from database: " + e.getMessage());
        }
    }

    private static void scheduleReminders() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        for (Reminder reminder : reminders) {
            if (!reminder.isShown()) { // Check if the reminder has already been shown
                long delay = calculateDelay(reminder.getDate().toLocalDate(), reminder.getReminderTime());
                scheduler.schedule(() -> {
                    showAlert("Reminder: " + reminder.getTaskTitle(), "You have a reminder!");
                    reminder.setShown(true);
                    reminders.remove(reminder);
                    Platform.runLater(() -> {
                        homeController.setRemindersListView();
                    });

                    try {
                        RemindersRepo.setStatus(reminder.getTaskId(), true);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, delay, TimeUnit.MILLISECONDS);
            }
        }
    }

    private static void showAlert(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static long calculateDelay(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        long delay = java.time.Duration.between(now.atDate(today), time.atDate(date)).toMillis();
        return delay > 0 ? delay : 0;
    }

    public static List<Reminder> getReminders() {
        return reminders;
    }

}