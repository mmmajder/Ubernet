insert into Role (name)
VALUES ('ROLE_ADMIN');/*1*/
insert into Role (name)
VALUES ('ROLE_DRIVER');/*2*/
insert into Role (name)
VALUES ('ROLE_CUSTOMER');/*3*/
insert into Role (name)
VALUES ('ROLE_USER');

insert into user_auth(deleted, is_enabled, is_password_reset, last_password_set, verification_code)
values (false, true, false, '2023-12-12', 'c98hzb4daIQhsT0bBRfsE4njlCswQ2DjljQxDIcUDZ8ry0c9wX0404zAkt8x0laI');

insert into admin (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id)
values ('admin@gmail.com', 'Adminville', false, false, 'Admin',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 123456', 0, 'Adminic', 1);

insert into user_auth_roles(user_auth_id, roles_id)
values (1, 1),
       (1, 4);


insert into user_auth(deleted, is_enabled, is_password_reset, last_password_set, verification_code)
values (false, true, false, '2023-12-12', 'c98hzb4daIQhsT0bBRfsE4njlCswQ2DjljQxDIcUDZ8ry0c9wX0404zAkt8x0laI');

insert into customer (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id, payment_credentials)
values ('petar@gmail.com', 'Petrovgrad', false, false, 'Petar',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 654321', 2, 'Petrovic', 2, 'parepare');

insert into user_auth_roles(user_auth_id, roles_id)
values (2, 3),
       (2, 4);

insert into message(id, client_email, admin_email, is_sent_by_admin, time, content, is_deleted)
values (1, 'petar@gmail.com', null, false, '2022-05-21 11:15', 'Helloooo, the taxi driver is kidnapping me. Please help.', false);
values (2, 'petar@gmail.com', 'admin@gmail.com', true, '2022-05-21 11:25', 'Oh no.', false);