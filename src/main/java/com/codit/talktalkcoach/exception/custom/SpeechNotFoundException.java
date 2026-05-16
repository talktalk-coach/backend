package com.codit.talktalkcoach.exception.custom;

import com.codit.talktalkcoach.exception.BusinessException;
import com.codit.talktalkcoach.exception.ErrorCode;

public class SpeechNotFoundException extends BusinessException {
    public SpeechNotFoundException() {
        super(ErrorCode.SPEECH_NOT_FOUND);
    }
}
