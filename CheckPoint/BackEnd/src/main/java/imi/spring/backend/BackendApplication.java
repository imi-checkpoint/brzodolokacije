package imi.spring.backend;

import com.tinify.Tinify;
import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Location;
import imi.spring.backend.models.Post;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.FollowersService;
import imi.spring.backend.services.LocationService;
import imi.spring.backend.services.PostService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
		ReactiveSecurityAutoConfiguration.class,
		SecurityFilterAutoConfiguration.class}
)
public class BackendApplication {

	public static void main(String[] args) {

		Tinify.setKey("p0k4LdgS2SXVWKQ72xMXStyVbm08GmTL");
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(AppUserService appUserService, LocationService locationService, PostService postService, FollowersService followersService){
		return args -> {
			appUserService.saveUser(new AppUser("user1@gmail.com", "user1", "user1"));
			appUserService.saveUser(new AppUser("user2@gmail.com", "user2", "user2"));
			appUserService.saveUser(new AppUser("user3@gmail.com", "user3", "user3"));
			appUserService.saveUser(new AppUser("user4@gmail.com", "user4", "user4"));

			locationService.saveLocation(new Location("Eiffel Tower", 48.858093, 2.294694));
			locationService.saveLocation(new Location("Stonehenge", 51.1740, -1.8224));
			locationService.saveLocation(new Location("Leaning Tower of Pisa", 43.7230159, 10.3966321974895));
			locationService.saveLocation(new Location("Machu Picchu", -13.163068, -72.545128));
			locationService.saveLocation(new Location("The Grand Canyon", 36.056595, -112.125092));
			locationService.saveLocation(new Location("Golden Gate Bridge", 37.8185, -122.4738));

			/*
			postService.savePost(new Post("The repainting campaign is an important event in the life of the monument and takes on a truly mythical nature."),
					1L, 1L);
			postService.savePost(new Post("The beams of light, directed from the bottom towards the top, illuminate the Eiffel Tower from the inside of its structure.t"),
					2L, 1L);
			postService.savePost(new Post("Experience the unforgettable atmosphere of the Stone Circle and follow in the footsteps of the prehistoric people who lived here 4,000 years ago as you walk among the Neolithic houses."),
					1L, 2L);
			postService.savePost(new Post("The Stone Circle is a masterpiece of engineering, and building it would have taken huge effort from hundreds of well-organised people using only simple tools and technologies. Visit Stonehenge to find out more about this iconic symbol of Britain."),
					3L, 2L);
			postService.savePost(new Post("The Pisa tower is one of the four buildings that make up the cathedral complex in Pisa."),
					1L, 3L);
			postService.savePost(new Post("One of Italy's signature sights, leaning a startling 3.9 degrees off the vertical. The 58m-high tower, officially the Duomo's campanile (bell tower), took almost 200 years to build, but was already listing when it was unveiled in 1372."),
					3L, 3L);
			*/

			postService.savePost("The repainting campaign is an important event in the life of the monument and takes on a truly mythical nature.",
					1L, 1L);
			postService.savePost("The beams of light, directed from the bottom towards the top, illuminate the Eiffel Tower from the inside of its structure.t",
					2L, 1L);
			postService.savePost("Experience the unforgettable atmosphere of the Stone Circle and follow in the footsteps of the prehistoric people who lived here 4,000 years ago as you walk among the Neolithic houses.",
					1L, 2L);
			postService.savePost("The Stone Circle is a masterpiece of engineering, and building it would have taken huge effort from hundreds of well-organised people using only simple tools and technologies. Visit Stonehenge to find out more about this iconic symbol of Britain.",
					3L, 2L);
			postService.savePost("The Pisa tower is one of the four buildings that make up the cathedral complex in Pisa.",
					1L, 3L);
			postService.savePost("One of Italy's signature sights, leaning a startling 3.9 degrees off the vertical. The 58m-high tower, officially the Duomo's campanile (bell tower), took almost 200 years to build, but was already listing when it was unveiled in 1372.",
					3L, 3L);

			followersService.followOrUnfollowUser(1L, 2L);
			followersService.followOrUnfollowUser(1L, 3L);
			followersService.followOrUnfollowUser(1L, 4L);
			followersService.followOrUnfollowUser(2L, 3L);
			followersService.followOrUnfollowUser(2L, 4L);
			followersService.followOrUnfollowUser(3L, 1L);
			followersService.followOrUnfollowUser(3L, 4L);
			followersService.followOrUnfollowUser(4L, 2L);
			followersService.followOrUnfollowUser(4L, 3L);
		};
	}

}
