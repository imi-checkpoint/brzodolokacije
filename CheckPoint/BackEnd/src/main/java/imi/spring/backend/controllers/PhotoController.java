package imi.spring.backend.controllers;

import imi.spring.backend.models.mongo.Photo;
import imi.spring.backend.services.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/photos")
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping
    public String photos(){
        return "uploadPhoto";
    }

    @PostMapping("/add")
    public String addPhoto(@RequestParam("postId") Long postId, @RequestParam("order") Integer order, @RequestParam("photo") MultipartFile image) throws IOException {
        String id = photoService.addPhoto(postId, order, image);
        log.info("Added image to MongoDB with id {}", id);
        return "redirect:/photos/" + postId;
    }

    @GetMapping("/{postId}")
    public String getPhoto(@PathVariable Long postId, Model model) {
        List<String> encodedPhotos = photoService.getEncodedPhotos(postId);

        model.addAttribute("postId", postId);
        model.addAttribute("photos", encodedPhotos);

        return "photos";
    }

    @GetMapping("/photoByPostIdAndOrder")
    @ResponseBody
    public Photo getPhotoByPostIdAndOrder(@RequestParam("postId") Long postId, @RequestParam("order") Integer order) throws IOException {
        return photoService.getPhotoByPostIdAndOrder(postId, order);
    }
}