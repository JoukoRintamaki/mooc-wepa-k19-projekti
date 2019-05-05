package wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class PhotoController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/photos")
    public String getPhotos(Model model, Authentication authentication) {
        model.addAttribute("currentUser", accountRepository.findByUsername(authentication.getName()));
        return "/photos";
    }

    @PostMapping("/photos")
    public String addPhoto(Authentication authentication,
                           @RequestParam("file") MultipartFile multipartFile,
                           @RequestParam String description) throws IOException {
        Account currentUser = accountRepository.findByUsername(authentication.getName());
        Photo photo = new Photo();
        photo.setContent(multipartFile.getBytes());
        photo.setDescription(description);
        photoRepository.save(photo);
        if (currentUser.photoAlbumSizeUnderTen()) {
            currentUser.getPhotos().add(photo);
        }
        accountRepository.save(currentUser);
        return "redirect:/photos";
    }

    @PostMapping("/photos/action")
    public String photoAction(Authentication authentication,
                              @RequestParam Long id,
                              @RequestParam String action) {
        Account currentUser = accountRepository.findByUsername(authentication.getName());
        if (Objects.equals(action, "remove")) {
            currentUser.getPhotos().remove(photoRepository.getOne(id));
            if (currentUser.getProfilePhoto() != null && currentUser.getProfilePhoto().getId() == id) {
                currentUser.setProfilePhoto(null);
            }
            accountRepository.save(currentUser);
            return "redirect:/photos";
        } else if (Objects.equals(action, "set")) {
            currentUser.setProfilePhoto(photoRepository.getOne(id));
            accountRepository.save(currentUser);
            return "redirect:/photos";
        }
        return "redirect:/photos";
    }

    @GetMapping(path = "/photos/{id}", produces = "images/png")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return photoRepository.getOne(id).getContent();
    }

    @PostMapping("/photo/like")
    public String addLike(Authentication authentication,
                          @RequestParam Long id,
                          @RequestHeader(value = "Referer", required = false) final String referer
    ) throws MalformedURLException {
        Photo photo = photoRepository.getOne(id);
        String username = authentication.getName();
        photo.getLikes().add(accountRepository.findByUsername(username));
        photoRepository.save(photo);
        return "redirect:" + new URL(referer).getPath();
    }

    @PostMapping("/photo/comment")
    public String addLike(Authentication authentication,
                          @RequestParam String content,
                          @RequestParam Long id,
                          @RequestHeader(value = "Referer", required = false) final String referer
    ) throws MalformedURLException {
        Photo photo = photoRepository.getOne(id);
        if (content != null && !content.trim().isEmpty()) {
            Message msg = new Message();
            msg.setContent(content.trim());
            String username = authentication.getName();
            msg.setSender(accountRepository.findByUsername(username));
            msg.setCreateDate(LocalDateTime.now());
            messageRepository.save(msg);
            photo.getComments().add(msg);
            photoRepository.save(photo);
        }
        return "redirect:" + new URL(referer).getPath();
    }
}
