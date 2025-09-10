-- ✅ LOCAL INFILE 허용
SET GLOBAL local_infile = 1;

-- ✅ FK 체크 해제 (데이터 순서 꼬일 경우 대비)
SET FOREIGN_KEY_CHECKS = 0;

-- ========================
-- 1. USERS
-- ========================
LOAD DATA LOCAL INFILE 'C:/Project25.08/baseball_data_generator/users.csv'
INTO TABLE users
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id, email, password, nickname, role, status);

-- ========================
-- 2. POSTS
-- ========================
LOAD DATA LOCAL INFILE 'C:/Project25.08/baseball_data_generator/posts.csv'
INTO TABLE posts
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(id, user_id, team_id, title, content, is_hidden);

-- ✅ FK 체크 다시 켜기
SET FOREIGN_KEY_CHECKS = 1;

-- ✅ LOCAL INFILE 다시 OFF
SET GLOBAL local_infile = 0;
