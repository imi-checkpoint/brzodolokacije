package imi.spring.backend.services.implementations;

import imi.spring.backend.models.AppUser;
import imi.spring.backend.repositories.AppUserRepository;
import imi.spring.backend.services.AppUserService;
import imi.spring.backend.services.FollowersService;
import imi.spring.backend.services.JWTService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FollowersServiceImpl implements FollowersService {

    private final AppUserRepository appUserRepository;
    private final JWTService jwtService;
    private final AppUserService appUserService;

    @Override
    public List<AppUser> getAllFollowingByUser(HttpServletRequest request) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        return user.getFollowingList();
    }

    @Override
    public List<AppUser> getAllFollowersPerUser(HttpServletRequest request) throws ServletException {
        AppUser user = jwtService.getAppUserFromJWT(request);
        return user.getFollowersList();
    }

    @Override
    public String followOrUnfollowUser(HttpServletRequest request, Long userId) throws ServletException {
        AppUser user1 = jwtService.getAppUserFromJWT(request);
        AppUser user2 = appUserRepository.findById(userId).orElse(null);
        if (user2 == null)
            return "User with that id does not exist!";
        if (user1.equals(user2))
            return "User can't follow himself!";

        List<AppUser> user1FollowList = user1.getFollowingList();
        if (user1FollowList.contains(user2)) { //unfollow
            user1FollowList.remove(user2);
            user1.setFollowingList(user1FollowList);
            appUserService.updateUser(user1);
            return "Unfollow";
        }
        else { //follow
            user1FollowList.add(user2);
            user1.setFollowingList(user1FollowList);
            appUserService.updateUser(user1);
            return "Follow";
        }
    }
}
