package everylog.domain.account.exception;

import everylog.global.error.exception.BusinessException;
import everylog.global.error.exception.ErrorResult;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(ErrorResult errorResult) {
        super(errorResult);
    }
}
