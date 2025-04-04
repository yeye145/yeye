DROP DATABASE IF EXISTS `student`;

CREATE SCHEMA `student` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;



#创建users表，用户表
CREATE TABLE `student`.`users` (
`users_id` INT NOT NULL AUTO_INCREMENT,
`users_name` VARCHAR(45) NOT NULL,
`users_password` VARCHAR(45) NOT NULL,
`isAdmin` INT NOT NULL,
PRIMARY KEY (`users_id`)
);

#创建students表，学生表
CREATE TABLE `student`.`students` (
`students_id`INT NOT NULL AUTO_INCREMENT,
`students_phoneNumber` VARCHAR(45) NOT NULL,
`students_classHadSelected` VARCHAR(45) NOT NULL,
`students_classNumber` INT NOT NULL,
`students_idNumber` VARCHAR(45) NOT NULL,
`students_gender` VARCHAR(45) NOT NULL,
`students_birthday` VARCHAR(45) NOT NULL,
`students_name` VARCHAR(45) NOT NULL,
PRIMARY KEY (`students_id`),
#约束 学生表主键 与 用户表主键，即学号 相等
FOREIGN KEY (students_id) REFERENCES users(users_id) ON UPDATE CASCADE ON DELETE CASCADE
);

#创建courses表，课程表
CREATE TABLE `student`.`courses` (
`courses_key` INT NOT NULL AUTO_INCREMENT,
`courses_name` VARCHAR(45) NOT NULL,
`courses_score` INT NOT NULL,
`courses_information` VARCHAR(45) NOT NULL,
`ifCanChoose` INT NOT NULL,
`courses_numberCanChoose` INT NOT NULL,
`courses_numberChoose` INT NOT NULL,
PRIMARY KEY (`courses_key`)
);

#创建student_courses表，选课表
CREATE TABLE `student`.`student_courses` (
`sc_key` INT NOT NULL AUTO_INCREMENT,
`sc_score` INT NOT NULL,
`sc_information` VARCHAR(45) NOT NULL,
`sc_name` VARCHAR(45) NOT NULL,
PRIMARY KEY (`sc_key`),
#约束 选课主键 与 课程主键 相等
FOREIGN KEY (sc_key) REFERENCES courses(courses_key)
);

#插入 用户信息 到 用户表中
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('1', 'student03', '333', '1');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('2', 'student04', '444', '1');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('3', 'student05', '555', '1');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('4', 'lyy', '1', '2');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('5', 'name', '0', '1');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('6', 'qqq', '2', '1');
INSERT INTO `student`.`users` (`users_id`, `users_name`, `users_password`, `isAdmin`) VALUES ('7', 'iiiii', 'aaa', '1');


#插入 学生信息 到 学生表中
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('1', '18300000003', '高速退学+爆炸艺术+线性代考+大学鹰语', '4', '445202200603031234', '男', '2006-03-03', '张三');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('2', '13600000004', '高速退学', '1', '445101200604044321', '女', '2006-04-04', '李四');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('3', '13600000005', '云里物理+高速退学', '2', '33450520050505789X', '男', '2005-05-05', '王五');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('4', '-1', '管理员', '-1', '-1', '-1', '-1', '-1');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('5', '12200007777', '云里物理+高速退学+生化危机', '3', '447007200707070077', '男', '2007-07-07', '王七');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('6', '18300000006', '高速退学+线性代考+大学鹰语', '3', '445202200603030033', '男', '2006-03-03', '王八');
INSERT INTO `student`.`students` (`students_id`, `students_phoneNumber`, `students_classHadSelected`, `students_classNumber`, `students_idNumber`, `students_gender`, `students_birthday`, `students_name`) VALUES ('7', '13600000007', '高速退学+英语四年级+线性代考+云里物理+爆炸艺术', '5', '44510120060404432X', '女', '2006-04-04', '牢九');



#插入 课程信息 到 课程表中
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('1', '高速退学', '6', '教3-101-周三12节', '1', '60', '6');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('2', '云里物理', '3', '教5-202-周五34节', '1', '60', '3');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('3', '生化危机', '4', '教3-303-周一67节', '0', '1', '1');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('4', '爆炸艺术', '2', '教1-101-周二67节', '0', '2', '2');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('5', '大学鹰语', '4', '教2-202-周四34节', '1', '60', '2');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('6', '线性代考', '4', '教4-234-周一12节', '1', '20', '3');
INSERT INTO `student`.`courses` (`courses_key`, `courses_name`, `courses_score`, `courses_information`, `ifCanChoose`, `courses_numberCanChoose`, `courses_numberChoose`) VALUES ('7', '英语四年级', '1', '教2-316-周二12节', '1', '5', '1');


#插入 课程信息 到 选课表中
INSERT INTO `student`.`student_courses` (`sc_key`, `sc_name`, `sc_score`, `sc_information`) VALUES ('1', '高速退学', '6', '教3-101-周三12节');
INSERT INTO `student`.`student_courses` (`sc_key`, `sc_name`, `sc_score`, `sc_information`) VALUES ('2', '云里物理', '3', '教5-202-周五34节');
INSERT INTO `student`.`student_courses` (`sc_key`, `sc_name`, `sc_score`, `sc_information`) VALUES ('5', '大学鹰语', '4', '教2-202-周四34节');
INSERT INTO `student`.`student_courses` (`sc_key`, `sc_name`, `sc_score`, `sc_information`) VALUES ('6', '线性代考', '4', '教4-234-周一12节');
INSERT INTO `student`.`student_courses` (`sc_key`, `sc_name`, `sc_score`, `sc_information`) VALUES ('7', '英语四年级', '1', '教2-316-周二12节');



SELECT * FROM student.users;

SELECT * FROM student.students;

SELECT * FROM student.courses;

SELECT * FROM student.student_courses;



# 更改表结构
ALTER TABLE `student`.`users` 
ADD COLUMN `email` VARCHAR(45) NULL AFTER `isAdmin`;

ALTER TABLE `student`.`students` 
ADD COLUMN `email` VARCHAR(45) NULL AFTER `students_name`;

ALTER TABLE `student`.`students` 
DROP FOREIGN KEY `students_ibfk_1`;
ALTER TABLE `student`.`students` 
CHANGE COLUMN `students_id` `id` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `students_phoneNumber` `phoneNumber` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `students_classHadSelected` `classHadSelected` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `students_classNumber` `classNumber` INT NOT NULL ,
CHANGE COLUMN `students_idNumber` `idNumber` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `students_gender` `gender` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `students_birthday` `birthday` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `students_name` `name` VARCHAR(45) NOT NULL ;
ALTER TABLE `student`.`students` 
ADD CONSTRAINT `students_ibfk_1`
  FOREIGN KEY (`id`)
  REFERENCES `student`.`users` (`users_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

ALTER TABLE `student`.`users` 
CHANGE COLUMN `users_id` `id` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `users_name` `phoneNumber` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `users_password` `password` VARCHAR(45) NOT NULL ;

ALTER TABLE `student`.`courses` 
CHANGE COLUMN `courses_key` `key` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `courses_name` `courseName` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `courses_score` `score` INT NOT NULL ,
CHANGE COLUMN `courses_information` `information` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `courses_numberCanChoose` `numberCanChoose` INT NOT NULL ,
CHANGE COLUMN `courses_numberChoose` `numberChoose` INT NOT NULL ;

ALTER TABLE `student`.`student_courses` 
CHANGE COLUMN `sc_key` `key` INT NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `sc_score` `score` INT NOT NULL ,
CHANGE COLUMN `sc_information` `information` VARCHAR(45) NOT NULL ,
CHANGE COLUMN `sc_name` `courseName` VARCHAR(45) NOT NULL ;

UPDATE `student`.`students` SET `phoneNumber` = '18300000985', `email` = 'y@y' WHERE (`id` = '4');
UPDATE `student`.`students` SET `email` = '3@3' WHERE (`id` = '1');
UPDATE `student`.`students` SET `email` = '4@4' WHERE (`id` = '2');
UPDATE `student`.`students` SET `email` = '5@5' WHERE (`id` = '3');
UPDATE `student`.`students` SET `email` = '7@7' WHERE (`id` = '5');
UPDATE `student`.`students` SET `email` = '8@8' WHERE (`id` = '6');
UPDATE `student`.`students` SET `email` = '9@9' WHERE (`id` = '7');

UPDATE `student`.`users` SET `phoneNumber` = '18300000003', `email` = '3@3' WHERE (`id` = '1');
UPDATE `student`.`users` SET `phoneNumber` = '13600000004', `email` = '4@4' WHERE (`id` = '2');
UPDATE `student`.`users` SET `phoneNumber` = '13600000005', `email` = '5@5' WHERE (`id` = '3');
UPDATE `student`.`users` SET `phoneNumber` = '12200007777', `email` = '7@7' WHERE (`id` = '5');
UPDATE `student`.`users` SET `phoneNumber` = '18300000006', `email` = '8@8' WHERE (`id` = '6');
UPDATE `student`.`users` SET `phoneNumber` = '13600000007', `email` = '9@9' WHERE (`id` = '7');
UPDATE `student`.`users` SET `phoneNumber` = '18300000985', `email` = 'y@y' WHERE (`id` = '4');

