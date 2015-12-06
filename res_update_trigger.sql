drop trigger if exists calc_days_update;
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