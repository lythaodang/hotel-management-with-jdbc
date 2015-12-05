DELIMITER %%;
create trigger updateReservationCost
after insert on roomservice
for each row
begin
	update reservation
    set reservation.totalCost = reservation.totalCost + roomservice.cost
    where reservation.reservationId = roomservice.reservationId;
end;