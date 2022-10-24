insert into Role (name)
VALUES ('ROLE_ADMIN');/*1*/
insert into Role (name)
VALUES ('ROLE_DRIVER');/*2*/
insert into Role (name)
VALUES ('ROLE_CUSTOMER');/*3*/
insert into Role (name)
VALUES ('ROLE_USER');

insert into user_auth(deleted, is_enabled, last_password_set, verification_code)
values (false, true, '2021-12-12', null),
       (false, true, '2021-12-12', null);

insert into admin (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id)
values ('admin@gmail.com', 'Adminville', false, false, 'Admin',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 123456', 0, 'Adminic', 1);

insert into driver (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id)
values ('driver@gmail.com', 'Driverville', false, false, 'Driver',
        '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 1);

insert into user_auth_roles(user_auth_id, roles_id)
values (1, 1),
       (1, 4),
       (2, 2),
       (2, 4);