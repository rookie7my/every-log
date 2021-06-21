package everylog.controller;

import everylog.domain.Account;
import everylog.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("sign-up form 요청")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up-form"))
                .andExpect(model().attributeExists("signUpForm"));

    }

    @DisplayName("sign-up 처리-정상입력")
    @Test
    @Transactional
    void processSignUpWithCorrectInput() throws Exception {
        String username = "testUser";
        String email = "test@test.com";
        String rawPassword = "12345678";
        mockMvc.perform(post("/sign-up")
                .param("username", username)
                .param("email", email)
                .param("password", rawPassword)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Account account = accountRepository.findByUsername(username);
        assertThat(account).isNotNull();
        assertThat(account.getUsername()).isEqualTo(username);
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(passwordEncoder.matches(rawPassword, account.getPassword())).isTrue();
    }

    @DisplayName("sign-up 처리-입력오류")
    @Test
    void processSignUpWithIncorrectInput() throws Exception {
        String username = "test#User?";
        String email = "test-email.com";
        String rawPassword = "12345678901234567890123456789012345678901234567890";
        mockMvc.perform(post("/sign-up")
                .param("username", username)
                .param("email", email)
                .param("password", rawPassword)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(view().name("account/sign-up-form"));

        Account account = accountRepository.findByUsername(username);
        assertThat(account).isNull();
    }

}