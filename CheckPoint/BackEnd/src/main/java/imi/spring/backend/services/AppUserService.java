package imi.spring.backend.services;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {
    AppUser saveUser(AppUser user);
    AppUser getUserByUsername(String username);
    AppUser getUserById(Long id);
    List<AppUser> getAllUsers();
    AppUser updateUser(AppUser user);
    String changeUserEmail(AppUser appUser, String newEmail);
    AppUser changeUserPassword(AppUser appUser, String[] passwords);
}
