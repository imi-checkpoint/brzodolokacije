package imi.spring.backend.services;

import imi.spring.backend.models.AppUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FollowersService {
    List<AppUser> getAllFollowingByUser(HttpServletRequest request) throws ServletException;

    List<AppUser> getAllFollowersPerUser(HttpServletRequest request) throws ServletException;

    String followOrUnfollowUser(HttpServletRequest request, Long userId) throws ServletException;
}
