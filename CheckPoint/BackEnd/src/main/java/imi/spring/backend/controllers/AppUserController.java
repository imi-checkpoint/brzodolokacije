package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.models.UserDTO;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
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
        AppUser appUser = new AppUser(user.getEmail(), user.getUsername(), user.getPassword());
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

    @GetMapping("/register")
    public String registerForm(Model model){
        model.addAttribute("appUser", new AppUser());
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String registerProcess(@RequestBody AppUser appUser, @RequestParam(name = "profile_image", required = false) MultipartFile profileImage) throws IOException {

        if(profileImage!=null && !profileImage.isEmpty()){
            appUser.setImage(profileImage.getBytes());
        }
        else {
            appUser.setImage(Files.readAllBytes(Path.of("src" + File.separator+ "main" + File.separator+ "resources" + File.separator +
                    "static" + File.separator+ "images" + File.separator+ "default-user.jpeg")));
        }

        try {
            appUserService.saveUser(appUser);
        }
        catch (BadCredentialsException badCredentialsException){
            return badCredentialsException.getMessage();
        }

        return "Successfully registered.";
    }

    @GetMapping("/getMyProfilePicture")
    @ResponseBody
    public byte[] getMyProfilePicture(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            return jwtService.getAppUserFromJWT(request).getImage();
        } catch (ServletException e) {
            throw new ServletException(e);
        }
    }

    @GetMapping("/getProfilePictureByUserId/{userId}")
    @ResponseBody
    public byte[] getProfilePictureByUserId(@PathVariable("userId") Long userId) throws IOException {
        AppUser user = appUserService.getUserById(userId);

        if(user != null && user.getImage()!=null){
            log.info("Getting picture for user with id {}", userId);
            return  user.getImage();
        }
        else{
            throw new IOException("Error getting image for that user.");
        }
    }

    @GetMapping("/changeProfilePicture")
    @ResponseBody
    public String changeProfilePicture(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "profile_image") MultipartFile profileImage) throws Exception {
        try {
            AppUser appUser = jwtService.getAppUserFromJWT(request);
            if (appUser != null && profileImage != null && !profileImage.isEmpty()) {
                appUser.setImage(profileImage.getBytes());
            }
            appUserService.updateUser(appUser);
            log.info("Successfully changed profile picture for user [{}]", appUser.getUsername());
            return "Success";
        } catch (ServletException | IOException e) {
            log.error("Error changing profile picture, received message [{}]", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/getUserId")
    @ResponseBody
    public Long getUserIdByUsername(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            AppUser appUser = jwtService.getAppUserFromJWT(request);

            log.info("Getting id for user [{}]", appUser.getUsername());

            if (appUser != null ) {
                return appUser.getId();
            }else{
                throw new IOException("User not found.");
            }
        } catch (ServletException | IOException e) {
            log.error("Error finishing request. [{}]", e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/user/info") //email and username
    @ResponseBody
    public AppUser changeUserInfo(HttpServletRequest request, @RequestBody AppUser editedUser) throws ServletException {
        try {
            AppUser appUser = jwtService.getAppUserFromJWT(request);
            return appUserService.changeEmailAndUsername(appUser, editedUser);
        } catch (ServletException e) {
            log.error("Error changing user information, received message [{}]", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    @PutMapping("/user/password")
    @ResponseBody
    public AppUser changeUserPassword(HttpServletRequest request, @RequestBody String[] passwords) throws ServletException {
        try {
            AppUser appUser = jwtService.getAppUserFromJWT(request);
            return appUserService.changeUserPassword(appUser, passwords);
        } catch (ServletException e) {
            log.error("Error changing user password, received message [{}]", e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}