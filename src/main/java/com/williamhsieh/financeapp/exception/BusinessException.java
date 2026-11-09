package com.williamhsieh.financeapp.exception;

import org.springframework.http.HttpStatus;

public class BusinessException
    extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public BusinessException(
        HttpStatus status,
        String code,
        String message
    ) {
        super(message);

        if (status == null) {
            throw new IllegalArgumentException(
                "HTTP 狀態不可為 null"
            );
        }

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                "錯誤代碼不可為空"
            );
        }

        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
