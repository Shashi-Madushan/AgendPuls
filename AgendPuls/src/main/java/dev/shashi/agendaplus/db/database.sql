-- Create database
CREATE DATABASE agendapro;

-- Use database
USE agendapro;

-- Create tables
CREATE TABLE user (
                      userName varchar(15) NOT NULL,
                      password varchar(10) DEFAULT NULL,
                      seQueNumber tinyint(1) DEFAULT NULL,
                      seQueAnswer varchar(20) DEFAULT NULL,
                      PRIMARY KEY (userName)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE categories (
                            category_id int NOT NULL AUTO_INCREMENT,
                            name varchar(255) DEFAULT NULL,
                            PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE financialtransactions (
                                       transaction_id int NOT NULL AUTO_INCREMENT,
                                       amount decimal(10,2) DEFAULT NULL,
                                       type tinyint(1) DEFAULT NULL,
                                       date date DEFAULT NULL,
                                       description text,
                                       category_id int DEFAULT NULL,
                                       PRIMARY KEY (transaction_id),
                                       KEY category_id (category_id),
                                       CONSTRAINT financialtransactions_ibfk_1 FOREIGN KEY (category_id) REFERENCES categories (category_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tasks (
                       task_id int NOT NULL AUTO_INCREMENT,
                       title varchar(255) DEFAULT NULL,
                       description text,
                       status tinyint(1) DEFAULT NULL,
                       date date DEFAULT NULL,
                       PRIMARY KEY (task_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE notes (
                       note_id int NOT NULL AUTO_INCREMENT,
                       content text,
                       transaction_id int DEFAULT NULL,
                       task_id int DEFAULT NULL,
                       PRIMARY KEY (note_id),
                       KEY transaction_id (transaction_id),
                       KEY task_id (task_id),
                       CONSTRAINT notes_ibfk_1 FOREIGN KEY (transaction_id) REFERENCES financialtransactions (transaction_id) ON DELETE CASCADE ON UPDATE CASCADE,
                       CONSTRAINT notes_ibfk_2 FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE reminders (
                           reminder_id int NOT NULL AUTO_INCREMENT,
                           task_id int DEFAULT NULL,
                           taskTitle varchar(255) DEFAULT NULL,
                           reminderDate date DEFAULT NULL,
                           reminderTime time DEFAULT NULL,
                           shown tinyint(1) DEFAULT '0',
                           PRIMARY KEY (reminder_id),
                           KEY task_id (task_id),
                           CONSTRAINT reminders_ibfk_1 FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE attachments (
                             attachment_id int NOT NULL AUTO_INCREMENT,
                             file_path varchar(255) DEFAULT NULL,
                             task_id int DEFAULT NULL,
                             PRIMARY KEY (attachment_id),
                             KEY task_id (task_id),
                             CONSTRAINT attachments_ibfk_1 FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE taskattachments (
                                 task_id int NOT NULL,
                                 attachment_id int NOT NULL,
                                 PRIMARY KEY (task_id, attachment_id),
                                 KEY attachment_id (attachment_id),
                                 CONSTRAINT taskattachments_ibfk_1 FOREIGN KEY (task_id) REFERENCES tasks (task_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT taskattachments_ibfk_2 FOREIGN KEY (attachment_id) REFERENCES attachments (attachment_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;