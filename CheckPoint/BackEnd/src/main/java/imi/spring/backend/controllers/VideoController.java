package imi.spring.backend.controllers;

import imi.spring.backend.models.mongo.Video;
import imi.spring.backend.services.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public String videos(){
        return "uploadVideo";
    }

    @PostMapping("/add")
    public String addVideo(@RequestParam("postId") Long postId, @RequestParam("file") MultipartFile file) throws IOException {
        String id = videoService.addVideo(postId, file);
        log.info("Video has been added for post with id {} and videoId is {}", postId, id);
        return "redirect:/videos/" + postId;
    }

    @GetMapping("/{postId}")
    public String getVideos(@PathVariable Long postId, Model model) throws Exception {

        Integer size = videoService.getVideos(postId).size();

        model.addAttribute("postId", postId);
        model.addAttribute("size", size);

        return "videos";
    }

    @GetMapping("/watchVideo")
    public String getActualVideo(@RequestParam Long postId,@RequestParam Integer order, Model model) {

        model.addAttribute("url", "/videos/stream?postId=" + postId + "&order="+ order);

        return "actualVideo";
    }

    @GetMapping("/stream")
    public void streamVideo(@RequestParam Long postId,@RequestParam Integer order, HttpServletResponse response) throws Exception {
        Video video = videoService.getVideos(postId).get(order);
        FileCopyUtils.copy(video.getInputStream(), response.getOutputStream());
    }


}
