package com.example.ubernet.controller;

import com.example.ubernet.dto.FavoriteRouteItem;
import com.example.ubernet.dto.FavoriteRouteRequest;
import com.example.ubernet.service.FavoriteRouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/favoriteRoute", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteRouteController {

    private final FavoriteRouteService favoriteRouteService;

    @PostMapping("/addToFavoriteRoutes")
    public void addToFavoriteRoutes(@RequestBody FavoriteRouteRequest favoriteRouteRequest) {
        this.favoriteRouteService.addToFavoriteRoutes(favoriteRouteRequest);
    }

    @PostMapping("/removeFromFavoriteRoutes")
    public void removeFromFavoriteRoutes(@RequestBody FavoriteRouteRequest favoriteRouteRequest) {
        this.favoriteRouteService.removeFromFavoriteRoutes(favoriteRouteRequest);
    }

    @GetMapping("/getFavoriteRoutes/{customerEmail}")
    public List<FavoriteRouteItem> getFavoriteRoutes(@PathVariable String customerEmail) {
        return this.favoriteRouteService.getFavoriteRoutes(customerEmail);
    }

    @PostMapping("/isRouteFavorite")
    public boolean isRouteFavorite(@RequestBody FavoriteRouteRequest favoriteRouteRequest) {
        return this.favoriteRouteService.ifRouteFavorite(favoriteRouteRequest);
    }
}
