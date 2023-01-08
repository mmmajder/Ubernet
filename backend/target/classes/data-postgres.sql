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
       (false, true, '2021-12-12', null),
       (false, true, '2021-12-12', null);

insert into admin (email, city, deleted, blocked, name, password, phone_number, role, surname, user_auth_id)
values ('admin@gmail.com', 'Adminville', false, false, 'Admin',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 123456', 0, 'Adminic', 1),
       ('admin111@gmail.com', 'Adminville', false, false, 'Admin',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 123456', 0, 'Adminic', 1);

insert into user_auth_roles(user_auth_id, roles_id)
values (1, 1),
       (1, 4);

insert into driver_daily_activity (deleted, is_active, last_time_set_active, total_duration)
values (false, true, '2022-10-24 00:00', 0),
       (false, true, '2022-10-24 00:00', 0),
       (false, true, '2022-10-24 00:00', 0);

insert into car_type (deleted, name, price_for_type)
values (false, 'Cabrio', 200),
       (false, 'Jeep', 500),
       (false, 'Van', 100);

insert into position (deleted, y, x)
values (false, 45.267136, 19.833549),
       (false, 45.267136, 19.843549),
       (false, 45.275136, 19.833549),
       (false, 45.275136, 19.843549),
       (false, 45.287136, 19.833549),
       (false, 45.287136, 19.843549);


-- insert into driver (email, city, deleted, is_blocked, name, password, phone_number, role, surname, user_auth_id,
insert into car (deleted, is_available, car_type_id, position_id, allows_baby, allows_pet, plates, name)
values (false, true, 1, 1, true, true, 'BMW', 'NS2FAST4U'),
       (false, true, 2, 3, true, true, 'Audi', 'NS-GLAMOC1'),
       (false, true, 3, 5, true, true, 'Yugo', 'BTACAB1312');

insert into driver (email, city, deleted, blocked, name, password, phone_number, role, surname, user_auth_id,
                    driver_daily_activity_id, car_id)
values ('driver@gmail.com', 'Driverville', false, false, 'Driver',
        '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 2, 1, 1),
       ('driver2@gmail.com', 'Driverville', false, false, 'Driver2',
        '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 3, 2, 2),
       ('driver3@gmail.com', 'Driverville', false, false, 'Driver3',
        '$2a$10$gyVv5jxxWVZRfUYlcbewoePW1wpaOjwFkolJhhg5fvmeHScQYom0q', '064 123546', 1, 'Driveric', 4, 3, 3);

insert into user_auth_roles(user_auth_id, roles_id)
values (2, 2),
       (2, 4),
       (3, 2),
       (3, 4),
       (4, 2),
       (4, 4);

insert into customer (email, city, deleted, blocked, name, password, phone_number, role, surname, user_auth_id,
                      number_of_tokens)
values ('customer@gmail.com', 'Customville', false, false, 'Customer',
        '$2a$10$2Mtev/q1qqNoSn39O7194eZVLBEvgM2dKzjkO0NUWETNKUYY9R/RO', '064 433456', 2, 'Customic', 5, 100.0),
       ('petar@gmail.com', 'Petrovgrad', false, false, 'Petar',
        '$2a$10$tnplXdStY6t7kOqqKssMYedAGjJ0T3OJH2BxeT81c1YrDqOUvHLD6', '064 654321', 2, 'Petrovic', 2, 150.0);

insert into user_auth_roles(user_auth_id, roles_id)
values (5, 3),
       (5, 4),
       (6, 3),
       (6, 4);

update car
set driver_email='driver@gmail.com'
where id = 1;
update car
set driver_email='driver2@gmail.com'
where id = 2;
update car
set driver_email='driver3@gmail.com'
where id = 3;

insert into user_auth(deleted, is_enabled, last_password_set, verification_code)
values (false, true, '2023-12-12', 'c98hzb4daIQhsT0bBRfsE4njlCswQ2DjljQxDIcUDZ8ry0c9wX0404zAkt8x0laI');

insert into message(id, client_email, admin_email, is_sent_by_admin, time, content, is_deleted)
values (99998, 'petar@gmail.com', null, false, '2022-05-21 11:15',
        'Helloooo, the taxi driver is kidnapping me. Please help.', false),
       (99999, 'petar@gmail.com', 'admin@gmail.com', true, '2022-05-21 11:25', 'Oh no.', false),
       (999997, 'customer@gmail.com', null, false, '2022-06-25 11:15',
        'This service is so fun to use.', false),
       (999998, 'customer@gmail.com', null, false, '2022-06-25 11:16',
        'I dont have any questions. I just wanted to let you know what I think:))', false),
       (999999, 'customer@gmail.com', 'admin@gmail.com', true, '2022-06-26 11:25', 'Ok thx.', false);

insert into payment (deleted, is_accepted_payment, total_price)
values (false, true, 500),
       (false, true, 500),
       (false, false, 500);

insert into route (deleted, price, time)
values (false, 500, 1200),
       (false, 500, 1200),
       (false, 500, 1200);

-- insert into ride_denial (deleted, reason, ride_id)
-- values (false, 'Eto tako', 1);

insert into ride (actual_end, actual_start, deleted, reservation_time, scheduled_start, driver_email, payment_id,
                  route_id, ride_state)
values ('2022-10-27 12:00', '2022-10-27 11:00', false, '2022-10-27 10:00', '2031-10-27 11:00', 'driver@gmail.com', 1,
        1, 3),
       ('2022-10-30 12:00', '2022-10-30 11:00', false, '2022-10-30 10:00', '2031-10-30 11:00', 'driver@gmail.com', 1,
        1, 3),
       ('2022-10-30 12:00', '2022-10-30 11:00', false, '2022-10-30 10:00', '2031-10-30 11:00', 'driver@gmail.com', 1,
        1, 3),
       ('2022-10-30 12:00', '2022-10-30 11:00', false, '2022-10-30 10:00', '2031-10-30 11:00', 'driver@gmail.com', 1,
        1, 3);

update payment
set ride_id=1
where id = 1;

update payment
set ride_id=2
where id = 2;

insert into ride_customers(ride_id, customers_email)
values (1, 'customer@gmail.com'),
    (2, 'petar@gmail.com'),
    (3, 'petar@gmail.com'),
    (4, 'customer@gmail.com'),
    (4, 'petar@gmail.com');

insert into review (comment, rating, customer_email)
values ('Bravo majstore', 4, 'customer@gmail.com'),
       ('Bravo majstoreee', 5, 'customer@gmail.com');

insert into ride_driver_reviews (ride_id, driver_reviews_id)
values (1, 1);

insert into ride_car_reviews (ride_id, car_reviews_id)
values (1, 1);

insert into comment(user_email, admin_email, time, content)
values ('customer@gmail.com', 'admin@gmail.com', '2022-10-27 12:00', 'Ovaj vozac je mnogo hot'),
       ('customer@gmail.com', 'admin@gmail.com', '2022-10-27 13:00', 'AHAHHAHAHHAHAH nije'),
       ('customer@gmail.com', 'admin@gmail.com', '2022-10-27 14:00', 'AHHAHAHHAHA e pa jeste'),
       ('customer@gmail.com', 'admin@gmail.com', '2022-10-28 12:00', 'ok.')
