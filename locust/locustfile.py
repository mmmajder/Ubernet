from random import randrange

import requests
from locust import HttpUser, task, between

taxi_stops = [
    (45.238548, 19.848225),  # Stajaliste na keju
    (45.243097, 19.836284),  # Stajaliste kod limanske pijace
    (45.256863, 19.844129),  # Stajaliste kod trifkovicevog trga
    (45.255055, 19.810161),  # Stajaliste na telepu
    (45.246540, 19.849282)  # Stajaliste kod velike menze
]


class EveryMinute(HttpUser):
    @task
    def notify_future_reservation(self):
        self.client.put("/ride-request/notify-time-until-reservation")

    # @task
    # def check_if_driver_is_active_more_than_8_hours(self):
    #     self.client.put("/driver/deactivate-too-much-active")

    wait_time = between(5, 5)


class Reserve(HttpUser):
    @task
    def reserve(self):
        self.client.put("/ride-request/send-cars-to-reservations")

    @task
    def return_money_to_passed_reservations_that_did_not_pay_everyone(self):
        self.client.put("/ride-request/return-money")

    wait_time = between(10, 10)


class Movement(HttpUser):

    @task
    def get_current_state(self):
        response = self.client.get("/car/active-available")
        print("Response status code:", response.status_code)
        for car in response.json():
            if car["firstRide"] is None or (len(car["firstRide"]["positions"]) == 0 and car["secondRide"] is None):
                random_taxi_stop = taxi_stops[randrange(0, len(taxi_stops))]
                response = requests.get(
                    f'https://routing.openstreetmap.de/routed-car/route/v1/driving/{car["currentPosition"]["x"]},{car["currentPosition"]["y"]};{random_taxi_stop[1]},{random_taxi_stop[0]}?geometries=geojson&overview=false&alternatives=true&steps=true')
                coordinates = []

                for step in response.json()['routes'][0]['legs'][0]['steps']:
                    for coord in step['geometry']['coordinates']:
                        coordinates.append(coord)
                # print(coordinates)
                print(len(coordinates))
                print("---------------------------------------------------")
                request = {"carId": car["carId"], "positions": coordinates}

                self.client.put("/car/new-free-ride", json=request)
            else:
                print(car["carId"])

        self.client.put("/car/new-position")
        self.client.put("/driver-inconsistency/check-inconsistency")
    wait_time = between(2, 2)



