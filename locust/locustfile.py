from random import random

import locust
from locust import HttpUser, task, between, run_single_user
import json

class MyTaskSet(HttpUser):
    # host = "http://localhost:8000"
    def createNewLocation(self):
        center = [45.26713, 19.833549]
        x = center[0] + (random() - 0.5) / 10
        y = center[1] + (random() - 0.5) / 10
        print([x, y])

    @task
    def getCurrentState(self):
        response = self.client.get("/car/active-available")
        print("Response status code:", response.status_code)
        for car in response.json():
            if (car["destinations"][0]["x"] == car["currentPosition"]["x"]) and \
                    (car["destinations"][0]["y"] == car["currentPosition"]["y"]):
                new_destination = {"carId": car["carId"], "newDestinations": [{"y": car["destinations"][-1]["y"] + (random() - 0.5) / 200,
                                                                    "x": car["destinations"][-1]["x"] + (random() - 0.5) / 200}]}
                self.client.put("/car/new-destination", json=new_destination)

    wait_time = between(0.5, 10)