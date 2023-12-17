INSERT INTO roles (id, name) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_MODERATOR'),
    (3, 'ROLE_ADMIN');

    INSERT INTO conditions (id, conditional) VALUES
    (1, 'TODO'),
    (2, 'IN_PROGRESS'),
    (3, 'TESTING'),
    (4, 'DONE');

INSERT INTO users (username, email, password, name, surname, papaname, specialization) VALUES
    ('user', 'user@mail.ru', '$2a$10$ck8u4QFBf/EFYRdgdrYnNu9Xq.quIhwYu7xSyvenNe5HMykGWJAuy', 'John', 'Doe', 'Jr', 'JUNIOR'),
    ('user2', 'user2@mail.ru', '$2a$10$xvVc7QjySQRcdQBajrVLr.eAcqBSIfmxYElgGjuLsSefy24u5QQTm', 'Jane', 'Doe', 'Sr', 'SENIOR'),
    ('HR1', 'HR1@mail.ru', '$2a$10$0MDbMOB4qLjU6gq.oiZ6M.uc2JlmXIEp3Ql.lE92Rb7LX12YdxGLS', 'Vlad', 'Mishikhin', 'Yurievich', 'SENIOR'),
    ('user3', 'user3@mail.ru', '$2a$10$nJTOIxURpWeGFURWcWGri.9V0RVEO2W1wGuftHZS6c/EBR2jchv4m', 'Alex', 'Smith', 'PapaSmith', 'JUNIOR'),
    ('user4', 'user4@mail.ru', '$2a$10$3uVm5kNjNakctk1dbfI3ROOlKqunBAZgrISaSG/Bsjkh4dv5jZQNa', 'Emily', 'Jones', 'PapaJones', 'TEAM_LEAD'),
    ('user5', 'user5@mail.ru', '$2a$10$BziZqv8ks2gA6ZeMadJE7uB3U2MvH7onO7Q/tyKnA4IwZ9nBYEE7u', 'Bob', 'Williams', 'PapaBob', 'TEAM_LEAD'),
    ('user6', 'user6@mail.ru', '$2a$10$ozqmWVO0mBHthiZRBQuLpuiia11cMU1bArHkBM1Wooy6nhS2UP2uC', 'Eva', 'Brown', 'PapaEva', 'SENIOR'),
    ('user7', 'user7@mail.ru', '$2a$10$DH3Sx6OP.J5C0YdsTESgmuPoCE/MJzKC0i9I81F8B4T4wDAGA8QEe', 'Tom', 'Johnson', 'PapaTom', 'TEAM_LEAD');

INSERT INTO teams (  team_lead_id,name) VALUES
    (5,'Team1'),
     (6,'Boyz');
 UPDATE users
SET team_id = 2
WHERE username = 'user2';
UPDATE users
SET team_id = 2
WHERE username = 'user3';
UPDATE users
SET team_id = 2
WHERE username = 'user5';

INSERT INTO tasks (created, deadline, ended, description, priority, complexity) VALUES
    ('2023-10-19 00:00:00', '2023-10-30 00:00:00', '2023-10-28 00:00:00',  'SimpleTestTask', 0, 2),
    ('2023-10-21 00:00:00', '2023-10-31 00:00:00', '2023-10-29 00:00:00',  'AnotherTestTask', 0, 1),
    ('2023-10-23 00:00:00', '2023-10-31 00:00:00', NULL,  'DeadTask', 0, 0),
    ('2023-10-29 00:00:00', '2023-10-31 00:00:00', '2023-11-01 00:00:00',  'OverdueTask', 0, 3),
    ('2023-11-03 00:00:00', '2023-11-17 00:00:00', NULL,  'ExpiringTask', 0, 1),
    ('2023-11-06 01:54:20.829525', '2023-11-07 01:20:00', '2023-11-10 00:00:00',  'CreatingTask', 0, 2),
    ('2023-11-06 01:54:28.128396', '2023-11-07 01:20:00', '2023-11-10 00:00:00',  'CreatingTaskOne', 0, 0),
    ('2023-11-06 01:57:48.300008', '2023-11-07 02:30:00', NULL,  'CreatingTaskTwo', 0, 3),
    ('2023-11-06 23:12:10.144533', '2023-11-07 03:03:00', NULL,  'CreatingTaskThree', 0, 1),
    ('2023-11-21 21:39:21.538369', '2023-11-22 01:33:00', NULL,  'Desk Task', 0, 2),
    ('2023-11-21 21:53:13.053656', '2023-11-22 01:33:00', NULL,  'Valid Task', 0, 0),
    ('2023-11-23 13:30:56.248012', '2023-11-25 01:33:00', '2023-11-23 14:51:48.056148',  'First Completed Task', 0, 3),
    ('2023-11-23 13:31:09.963794', '2023-11-26 01:33:00', '2023-11-23 14:51:58.529288',  'Second Completed Task', 0, 1),
    ('2023-11-23 13:31:16.613907', '2023-11-26 01:33:00', '2023-11-23 14:52:12.520823',  'Third Completed Task', 0, 2),
    ('2023-11-23 17:17:09.684594', '2023-11-25 02:46:00', NULL,  'Interesting Task', 0, 3),
    ('2023-11-23 17:17:21.715764', '2023-11-25 02:50:00', NULL,  'Boring Task', 0, 0);

    INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (4, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (5, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (6, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (7, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (8, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 3);
INSERT INTO task_conditions (condition_id,task_id) VALUES
(1, 7),
    (1, 8),
    (1, 9),
    (1, 11),
    (1, 16),
    (2, 1),
    (2, 2),
    (2, 5),
    (3, 3),
    (4, 4),
    (4, 6),
    (4, 10),
    (4, 13),
    (4, 14),
    (4, 15),
    (4, 16);
INSERT INTO changes (change_time,  task_id,user_id, change_text) VALUES

    ('2023-10-25 00:00:00',  1, 1, 'started to task'),
    ('2023-10-26 00:00:00',  2, 1, 'commit to first task'),
    ('2023-10-26 21:43:08.364726',  1, 1, 'trying new mech'),
    ('2023-10-26 21:46:31.201052',  2, 1, 'testing in progress'),
    ('2023-10-27 17:59:32.451502',  2, 1, 'new functions'),
    ('2023-10-27 18:27:17.309079',  1, 1, 'some commit from service'),
    ('2023-10-27 18:28:34.46403',  2, 1, '27 число'),
    ('2023-11-06 02:01:21.381206',  1, 1, 'write commit'),
    ('2023-11-07 12:27:05.890543',  7, 1, 'Задание выбрано'),
    ('2023-11-07 12:37:41.218037',  8, 1, 'Задание выбрано'),
    ('2023-11-07 12:37:41.286057',  9, 1, 'Задание выбрано'),
    ('2023-11-21 00:10:46.816961',  10, 6, 'Задание выбрано'),
    ('2023-11-21 00:11:28.300194',  11, 6, 'Новый этап'),
    ('2023-11-23 13:32:28.055461',  15, 4, 'Задание выбрано'),
    ('2023-11-23 13:32:28.090551',  13, 4, 'Задание выбрано'),
    ('2023-11-23 13:32:28.094158',  14, 4, 'Задание выбрано'),
    ('2023-11-23 13:32:50.683797',  13, 4, 'Быстро закончил задачу'),
    ('2023-11-23 13:33:04.71676',  14, 4, 'Выполнил сразу задание'),
    ('2023-11-23 13:33:13.018058',  15, 4, 'Моментальное выполнение'),
    ('2023-11-23 14:51:48.056148',  13, 4, 'конец'),
    ('2023-11-23 14:51:58.529288',  14, 4, 'финиш'),
    ('2023-11-23 14:52:12.520823',  15, 4, 'финальные правки'),
    ('2023-11-23 17:16:08.645808',  4, 5, 'Задание выбрано'),
    ('2023-11-23 17:16:08.684479',  3, 5, 'Задание выбрано'),
    ('2023-11-23 18:31:13.800028',  10, 6, 'ordinary commit'),
    ('2023-11-23 18:31:31.255198',  10, 6, 'completed task'),
    ('2023-11-23 18:36:55.156899',  16, 7, 'Задание выбрано'),
    ('2023-11-23 18:37:14.781576',  16, 7, 'выполняю boring task'),
    ('2023-11-23 18:37:27.326779',  16, 7, 'быстро докрутил тесты'),
    ('2023-11-23 18:37:33.391285',  16, 7, 'закончил'),
    ('2023-11-23 18:38:34.33335',  6, 8, 'Задание выбрано'),
    ('2023-11-23 18:39:06.280749',  6, 8, 'неочень оригинальный коммит'),
    ('2023-11-23 18:39:16.179323',  6, 8, 'покончил с задачей');
INSERT INTO notifications (user_id, message, type, timestamp) VALUES
    (1, 'New message received', 'MESSAGE_RECEIVED', '2023-01-03 15:30:00'),
    (2, 'Task update notification', 'TASK_UPDATE', '2023-01-04 09:45:00'),
    (1, 'Task completion notification', 'TASK_UPDATE', '2023-01-05 12:00:00'),
    (2, 'New message received', 'MESSAGE_RECEIVED', '2023-01-06 09:15:00'),
    (1, 'Task update notification', 'TASK_UPDATE', '2023-01-07 13:30:00'),
    (2, 'Task completion notification', 'TASK_UPDATE', '2023-01-08 16:45:00'),
    (1, 'New message received', 'MESSAGE_RECEIVED', '2023-01-09 10:30:00'),
    (2, 'Task update notification', 'TASK_UPDATE', '2023-01-10 14:00:00'),
    (1, 'Task completion notification', 'TASK_UPDATE', '2023-01-11 09:30:00'),
    (2, 'New message received', 'MESSAGE_RECEIVED', '2023-01-12 11:45:00'),
    (1, 'Task update notification', 'TASK_UPDATE', '2023-01-13 15:00:00'),
    (2, 'Task completion notification', 'TASK_UPDATE', '2023-01-14 10:15:00'),
    (1, 'New message received', 'MESSAGE_RECEIVED', '2023-01-15 12:30:00');
