package everylog.domain.account.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntroductionForm {

    private String introduction;

    public IntroductionForm(String introduction) {
        this.introduction = introduction;
    }
}
