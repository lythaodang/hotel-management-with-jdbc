DELIMITER %%;
create trigger calc_days_insert
before insert on reservation
for each row
begin
	if (datediff(new.endDate, new.startDate) > 60)
	then signal sqlstate'10002' set message_text = 'Reservation cannot be greater than 60 days';	
    else
	set new.numOfDays = datediff(new.endDate, new.startDate);
    end if;
    
	set new.totalCost = (select costPerNight from room where roomId = new.roomId) * new.numOfDays;
end;