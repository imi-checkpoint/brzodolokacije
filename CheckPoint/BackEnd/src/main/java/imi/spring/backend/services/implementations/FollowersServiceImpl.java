package imi.spring.backend.services.implementations;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.repositories.AppUserRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.FollowersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FollowersServiceImpl implements FollowersService {

    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @Override
    public List<AppUser> getAllFollowingByUser(Long userId) {
        AppUser user = appUserService.getUserById(userId);
        if (user != null)
            return user.getFollowingList();
        return Collections.emptyList();
    }

    @Override
    public List<AppUser> getAllFollowersPerUser(Long userId) {
        AppUser user = appUserService.getUserById(userId);
        if (user != null)
            return user.getFollowersList();
        return Collections.emptyList();
    }

    @Override
    public String followOrUnfollowUser(Long user1id, Long user2id) {
        AppUser user1 = appUserRepository.findById(user1id).orElse(null);
        AppUser user2 = appUserRepository.findById(user2id).orElse(null);
        if (user1 == null || user2 == null)
            return "User with that id does not exist!";
        if (user1.equals(user2))
            return "User can't follow himself!";

        List<AppUser> user1FollowList = user1.getFollowingList();
        if (user1FollowList.contains(user2)) { //unfollow
            user1FollowList.remove(user2);
            appUserService.updateUser(user1);
            return "Unfollow";
        }
        else { //follow
            user1FollowList.add(user2);
            appUserService.updateUser(user1);
            return "Follow";
        }
    }

    @Override
    public Integer countAllFollowingByUser(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElse(null);
        if (user == null)
            return 0;
        return user.getFollowingList().size();
    }

    @Override
    public Integer countAllFollowersPerUser(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElse(null);
        if (user == null)
            return 0;
        return user.getFollowersList().size();
    }
}
