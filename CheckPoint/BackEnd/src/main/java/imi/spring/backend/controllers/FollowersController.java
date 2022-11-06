package imi.spring.backend.controllers;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.services.FollowersService;
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

    /* Koga sve dati korisnik prati? */
    @GetMapping("/following")
    @ResponseBody
    public List<AppUser> getAllFollowingByUser(HttpServletRequest request) throws ServletException {
        return followersService.getAllFollowingByUser(request);
    }

    /* Ko sve prati datog korisnika? */
    @GetMapping("/followers")
    @ResponseBody
    public List<AppUser> getAllFollowersPerUser(HttpServletRequest request) throws ServletException {
        return followersService.getAllFollowersPerUser(request);
    }

    @PostMapping("/follow_or_unfollow/{userId}")
    @ResponseBody
    public String followOrUnfollowUser(HttpServletRequest request, @PathVariable Long userId) throws ServletException {
        return followersService.followOrUnfollowUser(request, userId);
    }

}
