package wall;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String getProfilePage(Model model, @PathVariable String profilename) {
        model.addAttribute("account", accountRepository.findByprofilename(profilename));
        model.addAttribute("messages", messageRepository.findAll());
        return "profile";
    }
}
