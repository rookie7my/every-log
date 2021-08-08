package everylog.web;

import everylog.web.form.BlogPostForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BlogPostController {

    @GetMapping("/new")
    public String createBlogPostForm(Model model) {
        model.addAttribute(new BlogPostForm());
        return "blogpost/form";
    }
}
