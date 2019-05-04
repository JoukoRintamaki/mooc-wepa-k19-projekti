package wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/{profilename}")
    public String getProfilePage(Model model, Authentication authentication, @PathVariable String profilename) {
        model.addAttribute("currentUser", accountRepository.findByUsername(authentication.getName()));
        Account account = accountRepository.findByprofilename(profilename);
        model.addAttribute("account", account);
        Pageable pageable = PageRequest.of(0, 25, Sort.by("createDate").descending());
        model.addAttribute("messages", messageRepository.findAllByRecipient(account, pageable));
        return "profile";
    }
}