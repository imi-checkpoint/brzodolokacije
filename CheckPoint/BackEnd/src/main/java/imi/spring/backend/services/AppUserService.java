package imi.spring.backend.services;

import imi.spring.backend.models.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    AppUser saveUser(AppUser user);
    AppUser getUserByUsername(String username);
    List<AppUser> getAllUsers();
}
