package imi.spring.backend;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.Location;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.LocationService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(AppUserService appUserService,LocationService locationService){
		return args -> {
			appUserService.saveUser(new AppUser("user1@gmail.com", "user1", "user1"));
			appUserService.saveUser(new AppUser("user2@gmail.com", "user2", "user2"));
			appUserService.saveUser(new AppUser("user3@gmail.com", "user3", "user3"));
			appUserService.saveUser(new AppUser("user4@gmail.com", "user4", "user4"));
			locationService.saveLocation(new Location("Eiffel Tower", 0.0, 0.0));
			locationService.saveLocation(new Location("Stonehenge", 0.0, 0.0));
			locationService.saveLocation(new Location("Leaning Tower of Pisa", 0.0, 0.0));
		};
	}

}
