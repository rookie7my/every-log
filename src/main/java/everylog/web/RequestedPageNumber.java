package everylog.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestedPageNumber {

    private Integer page;

    public boolean isPageValid() {
        return getZeroIndexedPageNumber() >= 0;
    }

    public int getZeroIndexedPageNumber() {
        if (page == null)
            return 0;
        return page - 1;
    }
}