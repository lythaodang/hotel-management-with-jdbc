drop trigger if exists calc_days_insert;
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