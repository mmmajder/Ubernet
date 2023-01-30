package com.example.ubernet.service;
import com.example.ubernet.model.enums.RideState;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class RideServiceTest {

    @Autowired
    private CustomerService customerService;

    @InjectMocks
    private RideService rideService;

//    @Test
//    @DisplayName("Should return RideState.RESERVED")
//    public void shouldReturnReservedStateForCreatingRideThatIsReservationAndNumberOfPassengersIsOne() {
//        getRideStateCreateRide =
//        RideState rideState = rideService.getRideStateCreateRide(true, 1);
//
//        Post post = new Post(123L, "First Post", Instant.now(), "Test", null, null);
//
//        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test",
//                "Test User", "Test Subredit", 0);
//
//        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
//        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);
//
//        PostResponse actualPostResponse = postService.getPost(123L);
//
//        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
//        Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expectedPostResponse.getPostName());
//    }
}
