DROP TABLE IF EXISTS user_progress;
DROP TABLE IF EXISTS live_ops_event;
DROP TABLE IF EXISTS invitation;
DROP TABLE IF EXISTS collaboration;

CREATE TABLE if not exists user_progress (
    id INT PRIMARY KEY AUTO_INCREMENT,
    coin DOUBLE DEFAULT 2000,
    level_at INT DEFAULT 1,
    helium DOUBLE DEFAULT 0,
    --testGroup TINYINT DEFAULT 0 -- O : A, 1 : B
    test_group VARCHAR(255) DEFAULT 'A'
);

CREATE TABLE if not exists live_ops_event (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_type TINYINT DEFAULT 0, -- O : POP_THE_BALLOON
    name VARCHAR(255) DEFAULT 'Pop The Balloon Event',
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE if not exists invitation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status TINYINT DEFAULT 0, -- O : PENDING, 1 : APPROVED, 2 : REJECTED
    sender_user_id INT NOT NULL,
    receiver_user_id INT DEFAULT NULL,
    event_id INT NOT NULL,
    FOREIGN KEY (sender_user_id) REFERENCES user_progress(id),
    FOREIGN KEY (receiver_user_id) REFERENCES user_progress(id),
    FOREIGN KEY (event_id) REFERENCES live_ops_event(id)
);

CREATE TABLE if not exists collaboration (
    invitation_id INT PRIMARY KEY,
    status TINYINT DEFAULT 0, -- O : IN_PROGRESS, 1 : SUSPENDED
    sender_user_helium_contribution DOUBLE DEFAULT 0,
    receiver_user_helium_contribution DOUBLE DEFAULT 0,
    FOREIGN KEY (invitation_id) REFERENCES invitation(id)
);

INSERT INTO user_progress (coin, level_at, helium, test_group)
VALUES
(6000, 60, 150, 'A'),
(3000, 30, 0, 'B'),
(10000, 100, 270, 'A'),
(6000, 60, 150, 'B'),
(2000, 1, 0, 'A'),
(4500, 45, 0, 'B'),
(4000, 40, 0, 'A'),
(8000, 80, 100, 'B'),
(75000, 75, 50, 'A'),
(2000, 20, 0, 'B'),
(4000, 20, 0, 'A'),
(15000, 90, 350, 'B');

INSERT INTO live_ops_event ()
VALUES
();