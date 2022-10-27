package imi.spring.backend;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.services.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	CommandLineRunner run(AppUserService appUserService){
		return args -> {
			appUserService.saveUser(new AppUser(null,"user1@gmail.com", "user1", "user1"));
			appUserService.saveUser(new AppUser(null,"user2@gmail.com", "user2", "user2"));
			appUserService.saveUser(new AppUser(null,"user3@gmail.com", "user3", "user3"));
			appUserService.saveUser(new AppUser(null,"user4@gmail.com", "user4", "user4"));
		};
	}

}
