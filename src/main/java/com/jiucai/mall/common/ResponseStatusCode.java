package com.jiucai.mall.common;

import lombok.Getter;

@Getter
public enum ResponseStatusCode {
    FAIL(-1, "FAIL"),
    SUCCESS(0, "SUCCESS"),
    NEED_LOGIN(10, "用户未登录！"),
    ERROR(1, "ERROR");

    private int code;
    private String description;

    /**
     * 响应状态码的构造器
     * @param code
     * @param description
     */
    ResponseStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
