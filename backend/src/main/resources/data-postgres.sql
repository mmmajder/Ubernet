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
       (false, true, '2021-12-12', null),
       (false, true, '2021-12-12', null),
       (false, true, '2021-12-12', null),
       (false, true, '2021-12-12', null);

insert into admin (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id)
values ('admin@gmail.com', 'Adminville', false, false, 'Admin',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 123456', 0, 'Adminic', 1);

insert into user_auth_roles(user_auth_id, roles_id)
values (1, 1),
       (1, 4);
-- insert into USER_ROLE (user_id, role_id) values
--     (CURRVAL('userSeqGen'), 1);
-- insert into user_auth_roles (user_auth_id, roles_id) values
--     ()

insert into driver_daily_activity (deleted, is_active, last_time_set_active, total_duration)
values (false, true, '2022-10-24 00:00', 0),
       (false, true, '2022-10-24 00:00', 0),
       (false, true, '2022-10-24 00:00', 0);

insert into car_type (allows_baby, allows_pet, deleted, name, price_for_type)
values (true, true, false, 'R8 AUDI', 200),
       (true, true, false, 'Golf 2', 500),
       (true, true, false, 'Fiat Multipla', 100);

insert into position (deleted, y, x)
values (false, 45.267136, 19.833549),
       (false, 45.267136, 19.843549);
--        (false, 45.277136, 19.833549),
--        (false, 45.277136, 19.813549),
--        (false, 45.287136, 19.833549),
--        (false, 45.287136, 19.863549);

insert into car (deleted, is_available, car_type_id, position_id)
values (false, true, 1, 1);
--        (false, true, 2, 3, 4),
--        (false, true, 3, 5, 6);

insert into car_destinations(car_id, destinations_id)
values (1, 2);

insert into driver (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id,
                    driver_daily_activity_id, car_id)
values ('driver@gmail.com', 'Driverville', false, false, 'Driver',
        '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 2, 1, 1);
--        ('driver2@gmail.com', 'Driverville', false, false, 'Driver2',
--         '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 3, 2, 2),
--        ('driver3@gmail.com', 'Driverville', false, false, 'Driver3',
--         '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 4, 3, 3);

insert into user_auth_roles(user_auth_id, roles_id)
values (2, 2),
       (2, 4);

insert into customer (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id)
values ('customer@gmail.com', 'Customville', false, false, 'Customer',
        '$2a$10$2Mtev/q1qqNoSn39O7194eZVLBEvgM2dKzjkO0NUWETNKUYY9R/RO', '064 433456', 2, 'Customic', 5);

insert into user_auth_roles(user_auth_id, roles_id)
values (3, 3),
       (3, 4);

-- insert into USER_ROLE (user_id, role_id) values
--     (CURRVAL('userSeqGen'), 3);

update car
set driver_email='driver@gmail.com'
where id = 1;
-- update car set driver_email='driver2@gmail.com' where id=2;
-- update car set driver_email='driver3@gmail.com' where id=3;

-- insert into user_auth_roles(user_auth_id, roles_id)
--
-- values (1, 1),
--        (1, 4),
--        (2, 2),
--        (2, 4);

insert into user_auth(deleted, is_enabled, last_password_set, verification_code)
values (false, true, '2023-12-12', 'c98hzb4daIQhsT0bBRfsE4njlCswQ2DjljQxDIcUDZ8ry0c9wX0404zAkt8x0laI');

insert into customer (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id, payment_credentials)
values ('petar@gmail.com', 'Petrovgrad', false, false, 'Petar',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 654321', 2, 'Petrovic', 2, 'parepare');

-- insert into user_auth_roles(user_auth_id, roles_id)
-- values (2, 3),
--        (2, 4);

insert into message(id, client_email, admin_email, is_sent_by_admin, time, content, is_deleted)
values (1, 'petar@gmail.com', null, false, '2022-05-21 11:15', 'Helloooo, the taxi driver is kidnapping me. Please help.', false),
       (2, 'petar@gmail.com', 'admin@gmail.com', true, '2022-05-21 11:25', 'Oh no.', false);

insert into payment (deleted, is_accepted_payment, total_price)
values (false, true, 500);

insert into route (deleted, price, time)
values (false, 500, 1200);

insert into ride (actual_end, actual_start, deleted, reservation_time, scheduled_start, driver_email, payment_id,
                  route_id)
values ('2022-10-27 12:00', '2022-10-31 11:00', false, '2022-10-31 10:00', '2031-10-24 11:00', 'driver@gmail.com', 1,
        1);

update payment
set ride_id=1
where id = 1;

insert into ride_customers(ride_id, customers_email)
values (1, 'customer@gmail.com');
