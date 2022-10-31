package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.UserDTO;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class AppUserController {
    private final AppUserService appUserService;
    private final JWTService jwtService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers(){
        return ResponseEntity.ok().body(appUserService.getAllUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody UserDTO user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        AppUser appUser = new AppUser(null, user.getEmail(), user.getUsername(), user.getPassword(), null, null);
        return ResponseEntity.created(uri).body(appUserService.saveUser(appUser));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{
            AppUser appUser = jwtService.getAppUserFromJWT(request);
            String accessToken = jwtService.createNewJWT(request, appUser.getUsername());
            String refreshToken = request.getHeader(AUTHORIZATION).substring("Bearer".length());

            jwtService.returnJWTokens(response, accessToken, refreshToken);
        }catch (Exception exception){
            jwtService.tokenErrorResponse(response, exception);
        }

    }


}
