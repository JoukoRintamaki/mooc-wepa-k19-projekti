package wall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class DefaultController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("*")
    public String handleDefault(@ModelAttribute Account Account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken)
            return "index";
        else {
            log.error(accountRepository.findByUsername(authentication.getName()).getProfilename());
            return "redirect:" + accountRepository.findByUsername(authentication.getName()).getProfilename();
        }
    }

    @PostMapping("/")
    public String add(@Valid @ModelAttribute Account account, BindingResult bindingResult
    ) throws UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "/";
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setProfilename(
                URLEncoder.encode(
                        account
                                .getProfilename()
                                .toLowerCase()
                                .trim()
                                //https://stackoverflow.com/questions/7046915/how-can-i-replace-all-special-characters-in-a-url-using-a-java-regular-expressi
                                .replaceAll("[^a-zA-Z0-9/]", "-")
                        ,
                        StandardCharsets.UTF_8.name()
                ).replace('%', '-')
        );
        accountRepository.save(account);
        return "redirect:/";
    }
}

