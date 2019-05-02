package hellodbauthentication;

import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/messages")
    public String view(Model model) {
        model.addAttribute("messages", messageRepository.findAll());
        return "messages";
    }

    @PostMapping("/messages")
    public String add(@RequestParam String content) {
        if (content != null && !content.trim().isEmpty()) {

            Message msg = new Message();
            msg.setContent(content.trim());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            msg.setSender(accountRepository.findByUsername(username));
            msg.setCreateDate(LocalDateTime.now());
            messageRepository.save(msg);
        }
        return "redirect:/messages";
    }

    @GetMapping("/{profilename}")
    public String getProfilePage(Model model, @PathVariable String profilename) {
        model.addAttribute("account",accountRepository.findByprofilename(profilename));
        model.addAttribute("messages", messageRepository.findAll());
        return "wall";
    }
}
