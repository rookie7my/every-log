package everylog.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class ShortIntroductionForm {

    @Length(max=100)
    private String shortIntroduction;

    public ShortIntroductionForm(String shortIntroduction) {
        this.shortIntroduction = shortIntroduction;
    }
}
