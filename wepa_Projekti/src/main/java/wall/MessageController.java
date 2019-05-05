package wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@Controller
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/messages")
    public String add(Authentication authentication,
                      @RequestParam String content,
                      @RequestParam Long id,
                      @RequestHeader(value = "Referer", required = false) final String referer)
            throws MalformedURLException {
        Account account = accountRepository.getOne(id);
        if (content != null && !content.trim().isEmpty()) {
            Message msg = new Message();
            msg.setContent(content.trim());
            String username = authentication.getName();
            msg.setSender(accountRepository.findByUsername(username));
            msg.setCreateDate(LocalDateTime.now());
            msg.setRecipient(account);
            messageRepository.save(msg);
        }
        return "redirect:" + new URL(referer).getPath();
    }

    @PostMapping("/messages/like")
    public String addLike(Authentication authentication,
                          @RequestParam Long id,
                          @RequestHeader(value = "Referer", required = false) final String referer)
            throws MalformedURLException {
        Message message = messageRepository.getOne(id);
        String username = authentication.getName();
        message.getLikes().add(accountRepository.findByUsername(username));
        messageRepository.save(message);
        return "redirect:" + new URL(referer).getPath();
    }

    @PostMapping("/messages/answer")
    public String addAnswer(Authentication authentication,
                            @RequestParam String content,
                            @RequestParam Long id,
                            @RequestHeader(value = "Referer", required = false) final String referer)
            throws MalformedURLException {
        Message message = messageRepository.getOne(id);
        if (content != null && !content.trim().isEmpty()) {
            Message msg = new Message();
            msg.setContent(content.trim());
            String username = authentication.getName();
            msg.setSender(accountRepository.findByUsername(username));
            msg.setCreateDate(LocalDateTime.now());
            messageRepository.save(msg);
            message.getAnswers().add(msg);
            messageRepository.save(message);
        }
        return "redirect:" + new URL(referer).getPath();
    }
}
