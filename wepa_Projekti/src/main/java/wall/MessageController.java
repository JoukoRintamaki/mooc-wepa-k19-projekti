package wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

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
        return "redirect:/";
    }
}
