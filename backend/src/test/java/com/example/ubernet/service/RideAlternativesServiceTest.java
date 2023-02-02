package com.example.ubernet.service;

import com.example.ubernet.dto.InstructionDTO;
import com.example.ubernet.dto.LatLngDTO;
import com.example.ubernet.dto.LeafletRouteDTO;
import com.example.ubernet.exception.BadRequestException;
import com.example.ubernet.model.*;
import com.example.ubernet.repository.PathAlternativeRepository;
import com.example.ubernet.repository.RideAlternativesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RideAlternativesServiceTest {

    @Mock
    private RideService rideService;
    @Mock
    private PathAlternativeRepository pathAlternativeRepository;
    @Mock
    private RideAlternativesRepository rideAlternativesRepository;
    @Captor
    private ArgumentCaptor<PathAlternative> pathAlternativeArgumentCaptor;
    @Captor
    private ArgumentCaptor<RideAlternatives> rideAlternativesArgumentCaptor;
    @InjectMocks
    private RideAlternativesService rideAlternativesService;

    private final Long RIDE_ID = 1L;
    @Test
    @DisplayName("Should throw BadRequestException when ride does not exist for provided id")
    public void shouldThrowBadRequestExceptionWhenRideWithProvidedIdDoesNotExist() {
        Mockito.when(rideService.findById(RIDE_ID)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> rideAlternativesService.createAlternativesForRide(RIDE_ID, any()));
    }

    @Test
    @DisplayName("Should create ride alternatives")
    public void shouldCreateRideAlternatives() {
        Ride ride = new Ride();
        Mockito.when(rideService.findById(RIDE_ID)).thenReturn(ride);
        List<List<LeafletRouteDTO>> alternatives = createAlternatives();
        for (List<LeafletRouteDTO> list : alternatives) {
            for (LeafletRouteDTO leafletRouteDTO : list) {
                Mockito.when(rideService.createCurrentRide(leafletRouteDTO.getCoordinates(), leafletRouteDTO.getInstructions())).thenReturn(new CurrentRide());
            }
        }
        rideAlternativesService.createAlternativesForRide(RIDE_ID, alternatives);
        verify(pathAlternativeRepository, times(alternatives.size())).save(pathAlternativeArgumentCaptor.capture());
        verify(rideAlternativesRepository, times(1)).save(rideAlternativesArgumentCaptor.capture());
    }

    private List<List<LeafletRouteDTO>> createAlternatives() {
        List<LeafletRouteDTO> path1 = new ArrayList<>();
        path1.add(createLeafletRoute("name1"));
        path1.add(createLeafletRoute("name2"));
        List<LeafletRouteDTO> path2 = new ArrayList<>();
        path2.add(createLeafletRoute("name3"));
        path2.add(createLeafletRoute("name4"));
        return List.of(path1, path2);
    }

    private LeafletRouteDTO createLeafletRoute(String name) {
        LeafletRouteDTO leafletRouteDTO = new LeafletRouteDTO();
        leafletRouteDTO.setName(name);
        leafletRouteDTO.setCoordinates(getCoordinatesLatLng());
        leafletRouteDTO.setInstructions(getInstructions());
        return leafletRouteDTO;
    }

    private List<LatLngDTO> getCoordinatesLatLng() {
        List<LatLngDTO> coords = new ArrayList<>();
        coords.add(new LatLngDTO(45.25624, 19.84421));
        coords.add(new LatLngDTO(45.25627, 19.84418));
        coords.add(new LatLngDTO(45.25648, 19.8439));
        coords.add(new LatLngDTO(45.25651, 19.84385));   // change of instruction
        coords.add(new LatLngDTO(45.25651, 19.84385));
        coords.add(new LatLngDTO(45.2565, 19.84384));
        coords.add(new LatLngDTO(45.25638, 19.84369));
        coords.add(new LatLngDTO(45.25623, 19.8435));    // change of instruction
        coords.add(new LatLngDTO(45.25623, 19.8435));
        return coords;
    }

    private List<InstructionDTO> getInstructions() {
        List<InstructionDTO> instructions = new ArrayList<>();
        instructions.add(new InstructionDTO(41.2, 16.0, "Његошева"));
        instructions.add(new InstructionDTO(42.2, 6, "Пролаз Милоша Хаџића"));
        instructions.add(new InstructionDTO(0, 0, "Пролаз Милоша Хаџића"));
        return instructions;
    }

}
