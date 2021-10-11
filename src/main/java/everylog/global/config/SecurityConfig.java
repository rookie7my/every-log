package everylog.global.config;

import everylog.global.security.CustomAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        , "/private-blog-posts"
                        , "/new"
                        , "/@{username}/blog-posts/{blogPostId}/edit"
                        , "/@{username}/blog-posts/{blogPostId}/settings").authenticated()
                .mvcMatchers("/"
                        , "/sign-up"
                        , "/@{username}"
                        , "/@{username}/about"
                        , "/@{username}/blog-posts/{blogPostId}/{blogPostTitle}").permitAll()
                .mvcMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                .mvcMatchers("/api/comments/**").authenticated();

        http.exceptionHandling()
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint("/login"));

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
