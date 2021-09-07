package everylog.global.config;

import everylog.domain.blogpost.controller.interceptor.BlogPostEditAuthorizationCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final BlogPostEditAuthorizationCheckInterceptor blogPostEditAuthorizationCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blogPostEditAuthorizationCheckInterceptor)
                .addPathPatterns("/@{username}/blog-posts/{blogPostId}/edit"
                        ,"/@{username}/blog-posts/{blogPostId}/settings");
    }
}
