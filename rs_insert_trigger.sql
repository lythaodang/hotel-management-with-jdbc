drop trigger if exists updateReservationCost;
DELIMITER %%;
create trigger updateReservationCost
after insert on roomservice
for each row
begin
	update reservation
    set reservation.totalCost = reservation.totalCost + cost
    where reservation.reservationId = reservationId;
end;