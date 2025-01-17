INSERT INTO authority VALUES(1, "ROLE_ADMIN");
INSERT INTO authority VALUES(2, "ROLE_USER");

INSERT INTO user VALUES(1, '2016-01-01', '2016-02-03', 'admin@localhost.com', b'1', 'Administrator', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'admin');
INSERT INTO user VALUES(2, '2016-01-07', '2016-01-15', 'user1@localhost.com', b'1', 'Bob', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user1');
INSERT INTO user VALUES(3, '2016-01-18', '2016-01-18', 'user2@localhost.com', b'1', 'Alice', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user2');
INSERT INTO user VALUES(4, '2016-01-19', '2016-01-30', 'user3@localhost.com', b'1', 'Alex', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user3');
INSERT INTO user VALUES(5, '2016-02-06', '2016-03-07', 'user4@localhost.com', b'1', 'Peter', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user4');
INSERT INTO user VALUES(6, '2016-02-12', '2016-02-15', 'user5@localhost.com', b'1', 'Eric', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user5');
INSERT INTO user VALUES(7, '2016-02-14', '2016-04-03', 'user6@localhost.com', b'1', 'John', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user6');
INSERT INTO user VALUES(8, '2016-03-11', '2016-03-12', 'user7@localhost.com', b'1', 'Bobby', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user7');
INSERT INTO user VALUES(9, '2016-03-20', '2016-03-25', 'user8@localhost.com', b'1', 'Daniel', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user8');
INSERT INTO user VALUES(10, '2016-03-27', '2016-04-02', 'user9@localhost.com', b'0', 'Berry', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user9');
INSERT INTO user VALUES(11, '2016-04-03', '2016-04-07', 'user10@localhost.com', b'0', 'Andy', '$2a$10$2OgstxRgNX02h58I8J6sJeyjwy8R7JGrxdKzvOvh92cs04jr0F6/O', 'user10');

-- admin --
INSERT INTO permission VALUES(1, 1);
INSERT INTO permission VALUES(1, 2);
-- user1 --
INSERT INTO permission VALUES(2, 1);
INSERT INTO permission VALUES(2, 2);
-- user2 --
INSERT INTO permission VALUES(3, 2);
-- user3 --
INSERT INTO permission VALUES(4, 2);
-- user4 --
INSERT INTO permission VALUES(5, 2);
-- user5 --
INSERT INTO permission VALUES(6, 1);
INSERT INTO permission VALUES(6, 2);
-- user6 --
INSERT INTO permission VALUES(7, 1);
INSERT INTO permission VALUES(7, 2);
-- user7 --
INSERT INTO permission VALUES(8, 2);
-- user8 --
INSERT INTO permission VALUES(9, 1);
INSERT INTO permission VALUES(9, 2);
-- user9 --
INSERT INTO permission VALUES(10, 2);
-- user10 --
INSERT INTO permission VALUES(11, 1);
INSERT INTO permission VALUES(11, 2);

INSERT INTO leaderboard VALUES(1, '2016-01-01', '2016-01-01', 0, 1500, 0, 1);
INSERT INTO leaderboard VALUES(2, '2016-01-07', '2016-01-07', 0, 1500, 0, 2);
INSERT INTO leaderboard VALUES(3, '2016-01-18', '2016-01-18', 0, 1500, 0, 3);
INSERT INTO leaderboard VALUES(4, '2016-01-19', '2016-01-19', 0, 1500, 0, 4);
INSERT INTO leaderboard VALUES(5, '2016-02-06', '2016-02-06', 0, 1500, 0, 5);
INSERT INTO leaderboard VALUES(6, '2016-02-12', '2016-02-12', 0, 1500, 0, 6);
INSERT INTO leaderboard VALUES(7, '2016-02-14', '2016-02-14', 0, 1500, 0, 7);
INSERT INTO leaderboard VALUES(8, '2016-03-11', '2016-03-11', 0, 1500, 0, 8);
INSERT INTO leaderboard VALUES(9, '2016-03-20', '2016-03-20', 0, 1500, 0, 9);
INSERT INTO leaderboard VALUES(10, '2016-03-27', '2016-03-27', 0, 1500, 0, 10);
INSERT INTO leaderboard VALUES(11, '2016-04-03', '2016-04-03', 0, 1500, 0, 11);
