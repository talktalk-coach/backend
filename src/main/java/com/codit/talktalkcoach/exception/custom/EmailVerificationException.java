package com.codit.talktalkcoach.exception.custom;

import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;

public class EmailVerificationException extends BusinessException {
    public EmailVerificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
