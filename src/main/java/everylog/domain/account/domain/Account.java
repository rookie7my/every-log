package everylog.domain.account.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String shortIntroduction;

    private String introduction;

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void updateShortIntroduction(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updatePassword(String encodePassword) {
        this.password = encodePassword;
    }

    public boolean matchUsername(String username) {
        return this.username.equals(username);
    }
}
