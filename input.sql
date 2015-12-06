DROP DATABASE IF EXISTS hoteldb;
CREATE DATABASE hoteldb;
USE hoteldb;

DROP TABLE IF EXISTS user;
CREATE TABLE user (
	username VARCHAR(12) NOT NULL, 
	password VARCHAR(20) NOT NULL, 
	firstName VARCHAR(15) NOT NULL, 
	lastName VARCHAR(15) NOT NULL,
	age INT NOT NULL,
	gender ENUM ('M', 'F', 'D'),
    userRole ENUM ('Customer', 'Manager', 'Receptionist', 'Room Service'),
	question VARCHAR(50) NOT NULL,
	answer VARCHAR(30) NOT NULL,
	PRIMARY KEY (username)
);

DROP TABLE IF EXISTS room;
CREATE TABLE room (
	roomId INT(10) NOT NULL AUTO_INCREMENT,
	costPerNight DOUBLE(10,2) NOT NULL,
	roomType VARCHAR(20) NOT NULL,
	PRIMARY KEY (roomId)
);

DROP TABLE IF EXISTS reservation;
CREATE TABLE reservation (
	reservationId INT(10) NOT NULL AUTO_INCREMENT,
	roomId INT(10) NOT NULL,
	customer VARCHAR(12) NOT NULL,
	startDate DATE NOT NULL,
	endDate DATE NOT NULL,
	numOfDays INT(10) NOT NULL,
	totalCost DOUBLE(10,2),
	keyReturned BOOLEAN NOT NULL DEFAULT FALSE,
	paid BOOLEAN NOT NULL DEFAULT FALSE,
	canceled BOOLEAN NOT NULL DEFAULT FALSE,
    updatedOn TIMESTAMP NOT NULL ON UPDATE current_timestamp,
	PRIMARY KEY (reservationId),
	FOREIGN KEY (roomId) references room (roomId) ON DELETE CASCADE,
	FOREIGN KEY (customer) references user (username) ON DELETE CASCADE
);

DROP TABLE IF EXISTS roomservice;
CREATE TABLE roomservice (
	taskId INT(10) NOT NULL AUTO_INCREMENT,
	task VARCHAR(250) NOT NULL,
	roomId INT(10) NOT NULL,
    completedBy VARCHAR(12),
	reservationId INT(10) NOT NULL,
	time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	cost DOUBLE(10,2) NOT NULL,
    updatedOn TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (taskId),
    FOREIGN KEY (completedBy) references user (username) ON DELETE CASCADE,
	FOREIGN KEY (roomId) references room (roomId) ON DELETE CASCADE,
	FOREIGN KEY (reservationId) references reservation (reservationId) ON DELETE CASCADE
);

DROP TABLE IF EXISTS complaint;
CREATE TABLE complaint (
	complaintId INT(10) NOT NULL AUTO_INCREMENT,
	customer VARCHAR(12) NOT NULL,
	complaint VARCHAR(100) NOT NULL,
	time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	resolvedBy VARCHAR(12),
	solution VARCHAR(100),
    updatedOn TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (complaintId),
	FOREIGN KEY (customer) references user (username) ON DELETE CASCADE,
	FOREIGN KEY (resolvedBy) references user (username) ON DELETE CASCADE
);

use hoteldb;
insert into room (costPerNight, roomType)
values (40, "The Shack 1"), (40, "The Shack 2"), (40, "The Shack 3"), (40, "The Shack 4"), (40, "The Shack 5"), 
(60, "Little Hut 1"), (60, "Little Hut 2"), (60, "Little Hut 3"), (60, "Little Hut 4"), (60, "Little Hut 5"), 
(80, "Modern 1"), (80, "Modern 2"), (80, "Modern 3"), (80, "Modern 4"), (80, "Modern 5"),
(100, "Luxury 1"), (100, "Luxury 2"), (100, "Luxury 3"),(100, "Luxury 4"),(100, "Luxury 5"), (200, "Presidential Suite");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("cust1id", "cust1pwd", "cust1_fn", "cust1_ln", 28, "F", "customer", "What was my high school mascot?", "falcon");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("cust2id", "cust2pwd", "cust2_fn", "cust2_ln", 50, "M", "customer", "What is my mother's maiden name?", "jones");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("manager", "managerpwd", "manager_fn", "manager_ln", 18, "M", "manager", "Favorite city", "Paris");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("receptionist", "receptionistpwd", "receptionist_fn", "receptionist_ln", 65, "F", "receptionist", "First pet's name", "Ginger");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("roomservice", "roomservicepwd", "roomservice_fn", "roomservice_ln", 50, "M", "room service", "What was my first job?", "receptionist");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("kunsuC", "12345678", "kun", "su", 24, "M", "customer", "What is my first name?", "kun");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("kunsuM", "12345678", "kun", "su", 24, "M", "manager", "What is my first name?", "kun");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("kunsuR", "12345678", "kun", "su", 24, "M", "receptionist", "What is my first name?", "kun");

insert into user (userName, password, firstName, lastName, age, gender, userRole, question, answer)
values ("kunsuS", "12345678", "kun", "su", 24, "M", "room service", "What is my first name?", "kun");

DROP PROCEDURE IF EXISTS archiveAll;
DELIMITER %%;
CREATE PROCEDURE archiveAll
(IN cutoffDate TIMESTAMP)
LANGUAGE SQL
DETERMINISTIC
COMMENT 'SP TO HANDLE ARCHIVE'
BEGIN
	-- HANDLER FOR ANY SQL EXCEPTION
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	
	-- START THE TRANSACTION
	START TRANSACTION;
		-- ARCHIVE RESERVATION TABLE
		INSERT INTO archive_reservation(reservationId,roomId,customer,startDate,endDate,numOfDays,
		                                totalCost,keyReturned,paid,canceled,updatedOn)
		SELECT reservationId,roomId,userId,startDate,endDate,numOfDays,totalCost,keyReturned,paid,canceled,updatedOn
		FROM reservation 
		WHERE DATE(updatedOn) <= cutoffDate; 
       		
		-- ARCHIVE ROOMSERVICE TABLE
		INSERT INTO archive_roomservice(taskId,task,userId,roomId,reservationId,time,cost,updatedOn)
		SELECT taskId,task,userId,roomId,reservationId,time,cost,updatedOn
		FROM roomservice
		WHERE DATE(updatedOn) <= cutoffDate;
		
		-- ARCHIVE COMPLAINT TABLE 
		INSERT INTO archive_complaint(complaintId,userId,complaint,time,resolvedBy,solution,updatedOn)
		SELECT complaintId,userId,complaint,time,resolvedBy,solution,updatedOn
		FROM complaint
		WHERE DATE(updatedOn) <= cutoffDate;
       
		-- DELETE OLD ROWS FROM RESERVATION, ROOMSERVICE, AND COMPLAINT TABLES
		DELETE FROM RESERVATION WHERE DATE(updatedOn) <= cutoffDate;
		DELETE FROM ROOMSERVICE WHERE DATE(updatedOn) <= cutoffDate;
		DELETE FROM COMPLAINT WHERE DATE(updatedOn) <= cutoffDate;
	COMMIT;
END;

DELIMITER %%;
create trigger calc_days_insert
before insert on reservation
for each row
begin
	if (datediff(new.endDate, new.startDate) > 60)
	then signal sqlstate '10002' set message_text = 'Reservation cannot be greater than 60 days';	
    else
	set new.numOfDays = datediff(new.endDate, new.startDate);
    end if;
    
    if (select distinct room.roomId 
			from room left outer join reservation on room.roomId = reservation.roomId 
			where (new.startdate = reservation.startdate 
            or new.startdate = reservation.enddate
            or new.enddate = reservation.startdate
            or new.enddate = reservation.enddate
            or (reservation.startdate < new.enddate and reservation.enddate > new.startdate)
            or (new.startdate < reservation.startdate and new.enddate > reservation.startdate)
            or (new.startdate < reservation.enddate and new.enddate > reservation.enddate)) and new.roomid = reservation.roomid)
	then signal sqlstate '10002' set message_text = 'Room is taken';
    end if;
    
	set new.totalCost = (select costPerNight from room where roomId = new.roomId) * new.numOfDays;
end;

DELIMITER %%;
create trigger calc_days_update
before update on reservation
for each row
begin
	if (datediff(new.endDate, new.startDate) > 60)
	then signal sqlstate'10002' set message_text = 'Reservation cannot be greater than 60 days';	
    else
	set new.numOfDays = datediff(new.endDate, new.startDate);
    end if;
end;

DELIMITER %%;
create trigger updateReservationCost
after insert on roomservice
for each row
begin
	update reservation
    set reservation.totalCost = reservation.totalCost + roomservice.cost
    where reservation.reservationId = roomservice.reservationId;
end;

DELIMITER %%;
CREATE TRIGGER check_account_creation
BEFORE INSERT ON USER
FOR EACH ROW
BEGIN 
	IF CHAR_LENGTH(NEW.USERNAME) < 6
	THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Username must be at least 6 characters';
	END IF;
	
	IF CHAR_LENGTH(NEW.PASSWORD) < 8
	THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Password must be at least 8 characters';
	END IF;
	
	IF CHAR_LENGTH(NEW.FIRSTNAME) < 1
	THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'First name cannot be empty';
	END IF;
	
	IF CHAR_LENGTH(NEW.LASTNAME) < 1
	THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Last name cannot be empty';
	END IF;
    
    IF NEW.AGE < 18
    THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Must be at least 18 years of age';
    END IF;
    
    IF CHAR_LENGTH(NEW.QUESTION) < 10
    THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Question must be at least 10 characters';
    END IF;
    
    IF CHAR_LENGTH(NEW.ANSWER) < 5
    THEN SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Answer must be at least 5 characters';
    END IF;
END; 

DELIMITER ;
INSERT into reservation (roomId, customer, startDate, endDate, numOfDays)
values (2, "cust1id", "2015-11-20", "2015-11-25", 5);
INSERT into reservation (roomId, customer, startDate, endDate, numOfDays)
values (3, "cust1id", "2015-12-20", "2015-12-25", 5);
INSERT into reservation (reservationId, roomId, customer, startDate, endDate, numOfDays)
values (5, 4, "kunsuC", "2015-12-20", "2015-12-25", 5);

INSERT into complaint (customer, complaint, time, resolvedBy, solution)
values ("kunsuC", "this is the first complaint", "2015-12-05", null, null);
INSERT into complaint (customer, complaint, time, resolvedBy, solution)
values ("kunsuC", "this is the second complaint", "2015-12-05", "KunsuM", "the second solution");

INSERT into roomservice (task, roomId, completedBy, reservationId, time, cost)
values ("Breakfast", 4, null, 5, "2015-12-05", 20);
INSERT into roomservice (task, roomId, completedBy, reservationId, time, cost)
values ("Lunch", 4, null, 5, "2015-12-05", 20);

SELECT * FROM complaint;
SELECT * FROM reservation;
SELECT * FROM room;
SELECT * FROM user;


LOAD DATA LOCAL INFILE 'complaint.txt' INTO TABLE complaint;
LOAD DATA LOCAL INFILE 'reservation.txt' INTO TABLE reservation;
LOAD DATA LOCAL INFILE 'room.txt' INTO TABLE room;
LOAD DATA LOCAL INFILE 'user.txt' INTO TABLE user;

SELECT * FROM complaint;
SELECT * FROM reservation;
SELECT * FROM room;
SELECT * FROM user;
