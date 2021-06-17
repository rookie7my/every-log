package everylog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AccountController {

    @GetMapping("/@{name}")
    public String accountHome(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "account/home";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "account/sign-up-form";
    }

}
