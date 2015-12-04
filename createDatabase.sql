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
	customer VARCHAR(12) NOT NULL,
	roomId INT(10) NOT NULL,
    completedBy VARCHAR(12),
	reservationId INT(10) NOT NULL,
	time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	cost DOUBLE(10,2) NOT NULL,
    updatedOn TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (taskId),
    FOREIGN KEY (completedBy) references user (username) ON DELETE CASCADE,
	FOREIGN KEY (customer) references user (username) ON DELETE CASCADE,
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