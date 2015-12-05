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

SELECT * FROM user;