package imi.spring.backend.services;

import imi.spring.backend.models.AppUser;

import java.util.List;

public interface FollowersService {
    List<AppUser> getAllFollowingByUser(Long userId);

    List<AppUser> getAllFollowersPerUser(Long userId);

    String followOrUnfollowUser(Long user1id, Long user2id);

    Integer countAllFollowingByUser(Long userId);

    Integer countAllFollowersPerUser(Long userId);

    List<AppUser> getFollowingByUsername(Long userId, String username);

    List<AppUser> getFollowersByUsername(Long userId, String username);
}
