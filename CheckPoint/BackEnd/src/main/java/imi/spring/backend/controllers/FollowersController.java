package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.services.FollowersService;
import imi.spring.backend.services.JWTService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping(path = "/follow_list")
public class FollowersController {
    private final FollowersService followersService;
    private final JWTService jwtService;

    /* Koga sve dati korisnik prati? */
    @GetMapping("/following/{userId}")
    @ResponseBody
    public List<AppUser> getAllFollowingByUser(@PathVariable Long userId) {
        return followersService.getAllFollowingByUser(userId);
    }

    /* Ko sve prati datog korisnika? */
    @GetMapping("/followers/{userId}")
    @ResponseBody
    public List<AppUser> getAllFollowersPerUser(@PathVariable Long userId) {
        return followersService.getAllFollowersPerUser(userId);
    }

    @PostMapping("/follow_or_unfollow/{userId}")
    @ResponseBody
    public String followOrUnfollowUser(HttpServletRequest request, @PathVariable Long userId) throws ServletException {
        AppUser userFromJWT = jwtService.getAppUserFromJWT(request);
        if (userFromJWT != null)
            return followersService.followOrUnfollowUser(userFromJWT.getId(), userId);
        return "Invalid user!";
    }

    @GetMapping("/following/count/{userId}")
    @ResponseBody
    public Integer countAllFollowingByUser(@PathVariable Long userId) {
        return followersService.countAllFollowingByUser(userId);
    }

    @GetMapping("/followers/count/{userId}")
    @ResponseBody
    public Integer countAllFollowersPerUser(@PathVariable Long userId) {
        return followersService.countAllFollowersPerUser(userId);
    }
}
