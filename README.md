![logo](https://github.com/mmmajder/Ubernet/assets/91467463/2a878cf2-3bc2-4685-a5fe-9b2dd32bf024)
# Ubernet

Web Uber app. Preview app images in app-screenshots folder.

## Technologies
- Spring Boot
- Angular
- Locust script (for generating random movement of uber cars)

## Features
- Unauthorised
  - Login
  - Registration
  - Map preview with estimation of price of ride
- Admin
  -  CRUD drivers and customers
  -  Live chat with customers
  -  Preview of active cars on map
- Driver
  - Has displayed shortest route from his position to start location of ride
  - Has route which he/she has to follow during the ride
  - Has ability to have up to 2 sequential rides ordered in advance
  - Has ability to cancel ride if customer did not apper
  - Has abilitu to cancel ride in progress if there is health/technical problem
  - When driver is not occupied/reserved, car is roaming around the city
- Customer
  - Request imediate ride
  - Reserve ride for advance
  - Preview history of rides
  - Preview of currently active cars and their positions on map
  - Token payment using paypal sandbox
  - Receive notifications about assigning drivers, ride cancellations, and the distance of the car
  - Use live customer support
  - Rate ride, leave comment on ride/driver
  - Mark favorite route
  - Select multiple checkpoints for ride, adding other users to ride, choosing car type
  - Accept/decline/issue split fair
  - Edit profile
  - Ability to report driver for not following desired ride path
  - Reports for specific time range   

## Installation Instructions
- Clone repo
- Front-end setup
  - position inside frontend folder
  - enter npm install in terminal to install dependencies
  - after that enter ng serve to start front-end applicaton
  - open browser and enter [localhost:4200](http://localhost:4200/). You should see home screen
- Back-end setup
  - start application by running backend\src\main\java\com\example\ubernet\UbernetApplication.java
  - back-end should run on [localhost:8000](http://localhost:8000/)
- Locust script
  - position inside locust folder
  - run in terminal locust
  - open browser and enter [localhost:8089](http://localhost:8089/). You should see locust screen, enter 3 for number of users, and [localhost:8000](http://localhost:8000/) for host

## Contributors
- Aleksa Stanivuk SW29/2019
- Milan Ajder SW31/2019
- Anđela Mišković SW33/2019
