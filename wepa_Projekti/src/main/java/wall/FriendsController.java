package wall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FriendsController {
    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/friends")
    public String getProfilePageAndQueryResult(Model model,
                                               Authentication authentication,
                                               @RequestParam(required = false) String query) {
        if (query != null && !query.isEmpty()) {
            List<Account> accounts = accountRepository.findBynameIgnoreCaseContaining(query);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Account account = accountRepository.findByUsername(auth.getName());
            if (accounts.contains(account)) {
                accounts.remove(account);
            }
            model.addAttribute("findFriends", accounts);
        }
        model.addAttribute("account", accountRepository.findByUsername(authentication.getName()));
        model.addAttribute("accounts", accountRepository.findAll());
        return "friends";
    }

    @PostMapping("/friends")
    public String acceptRequest(Authentication authentication, @RequestParam Long id, @RequestParam String action) {
        Account user = accountRepository.findByUsername(authentication.getName());
        Account friendRequester = accountRepository.getOne(id);
        if (action.equals("accept")) {
            user.getFriends().add(friendRequester);
            friendRequester.getFriends().add(user);
            accountRepository.save(friendRequester);
        }
        user.getFriendRequests().remove(friendRequester);
        accountRepository.save(user);
        return "redirect:/friends";
    }

    @PostMapping("/friends/request")
    public String request(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        Account friendCandinate = accountRepository.getOne(id);
        friendCandinate.getFriendRequests().put(user, LocalDateTime.now());
        accountRepository.save(friendCandinate);
        return "redirect:/friends";
    }

    @PostMapping("/friends/remove")
    public String remove(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        Account friend = accountRepository.getOne(id);
        user.getFriends().remove(friend);
        accountRepository.save(user);
        friend.getFriends().remove(user);
        accountRepository.save(friend);
        return "redirect:/friends";
    }
}