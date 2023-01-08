from random import random
from turtledemo.planet_and_moon import G

from locust import HttpUser, task, between, run_single_user

import osmnx as ox
import networkx as nx


class MyTaskSet(HttpUser):
    # host = "http://localhost:8000"
    def createNewLocation(self):
        center = [45.26713, 19.833549]
        x = center[0] + (random() - 0.5) / 10
        y = center[1] + (random() - 0.5) / 10
        print([x, y])

    @task
    def getCurrentState(self):
        activeAvailableCars = self.client.get("/car/active-available")
        for car in activeAvailableCars.json():
            if not car["currentRide"]:
                # while True:
                print(car["currentPosition"])
                position = car["currentPosition"]["y"], car["currentPosition"]["x"]
                graph = ox.graph_from_point(position, dist=1000, network_type='drive', simplify=False)
                graph = ox.add_edge_speeds(graph)
                graph = ox.add_edge_travel_times(graph)
                orig_node = ox.nearest_nodes(graph,
                                             car["currentPosition"]["x"],
                                             car["currentPosition"]["y"])
                print(orig_node)

                destx = car["currentPosition"]["x"] + (random() - 0.5) / 200
                desty = car["currentPosition"]["y"] + (random() - 0.5) / 200
                print(destx)
                print(desty)
                dest_node = ox.nearest_nodes(graph, destx, desty)
                print(dest_node)

                shortest_route = nx.shortest_path(graph,
                                                  orig_node,
                                                  dest_node,
                                                  weight='time')

                print(shortest_route)

                print("Conversion")
                points = []
                for node in shortest_route:
                    print(graph.nodes[node])
                    latitude = graph.nodes[node]['x']
                    longitude = graph.nodes[node]['y']
                    points.append({'latitude': latitude, 'longitude': longitude})
                print(points)

                print(shortest_route)
                for i in range(len(shortest_route) - 1):
                    node1 = shortest_route[i]
                    node2 = shortest_route[i + 1]
                    edge = graph[node1][node2]  # get the edge connecting the two nodes
                    print("edge")
                    print(edge)
                    points[i]['time'] = edge[0]['length'] / 50

                print(points)

    wait_time = between(0.5, 10)
