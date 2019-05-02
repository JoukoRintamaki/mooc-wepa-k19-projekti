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
    public String getProfilePage(Model model, Authentication authentication) {
        model.addAttribute("account", accountRepository.findByUsername(authentication.getName()));
        model.addAttribute("accounts", accountRepository.findAll());
        return "friends";
    }

    /*@GetMapping("/friends")
    public String getProfilePageAndQueryResult(Model model, Authentication authentication, @RequestParam String query) {
        List<Account> accounts = accountRepository.findBynameContaining(query);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findByUsername(auth.getName());
        if(accounts.contains(account)){
            accounts.remove(account);
        }
        model.addAttribute("findFriends",accounts);
        model.addAttribute("account", accountRepository.findByUsername(authentication.getName()));
        model.addAttribute("accounts", accountRepository.findAll());
        return "friends";
    }*/

    @PostMapping("/friends/accept")
    public String acceptRequest(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        Account friendCandinate = accountRepository.getOne(id);
        user.getFriends().add(friendCandinate);
        user.getFriendRequests().remove(friendCandinate);
        accountRepository.save(user);
        friendCandinate.getFriends().add(user);
        accountRepository.save(friendCandinate);
        return "redirect:/friends";
    }

    @PostMapping("/friends/deny")
    public String deniedRequest(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account user = accountRepository.findByUsername(auth.getName());
        Account friendCandinate = accountRepository.getOne(id);
        user.getFriendRequests().remove(friendCandinate);
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