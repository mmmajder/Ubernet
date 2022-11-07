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

    # @task
    # def getCurrentState(self):
    #     response = self.client.get("/car/active-available")
    #     print("Response status code:", response.status_code)
    #     # print("Response content:", response.content)
    #     for car in response.json():
    #         print(car["driverEmail"])
    #     # print(res.json())

    @task
    def getCurrentState(self):
        response = self.client.get("/car/active-available")
        print("Response status code:", response.status_code)
        center = [45.267136, 19.833549]
        for car in response.json():
            if (car["destinations"][0]["x"] == car["currentPosition"]["x"]) and \
                    (car["destinations"][0]["y"] == car["currentPosition"]["y"]):
                print("yea")
                new_destination = {"carId": 1, "newDestinations": [{"y": center[0] + (random() - 0.5) / 10,
                                                                    "x": center[1] + (random() - 0.5) / 10}]}
                self.client.put("/car/new-destination", json=new_destination)

    # @task
    # def updatePositions(self):
        

    wait_time = between(0.5, 10)

# if __name__ == "__main__":
#     run_single_user(MyTaskSet)

# class MyLocust(locust.HttpUser):
#     task_set = MyTaskSet
#
#     min_wait = 900
#     max_wait = 1100
#
#     host = 'http://localhost:8000'

# @task
# def hello_world(self):
#     self.client.post("/auth/login", {"email": "driver@gmail.com", "password": "driver"})

# i = 0
#
# @task
# def create(self):
#     self.i += 1
#     email = "m" + str(self.i) + "@gmail.com"
#     print(email)
#     req = {
#         "isDeleted": False,
#         "name": "namce",
#         "surname": "c",
#         "email": "d@gmail.com",
#         "userRole": "CUSTOMER",
#         "enabled": True,
#         "city": "city",
#         "phoneNumber": "phoneNumber",
#         "password": "customer"
#     }
#     print(req)
#     self.client.post("/auth/register", json=req)
