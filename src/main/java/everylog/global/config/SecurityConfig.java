package everylog.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/settings/**"
                        ,"/private-blog-posts"
                        ,"/new"
                        ,"/@{username}/blog-posts/{blogPostId}/edit"
                        ,"/@{username}/blog-posts/{blogPostId}/settings").authenticated()
                .mvcMatchers("/"
                        , "/sign-up"
                        , "/api/**"
                        , "/@{username}"
                        , "/@{username}/about"
                        , "/@{username}/blog-posts/{blogPostId}/{blogPostTitle}").permitAll();

        http.formLogin()
                .loginPage("/login")
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
