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