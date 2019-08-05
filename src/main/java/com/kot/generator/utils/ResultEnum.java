package com.kot.generator.utils;

/**
 * @author YangYu
 */

public enum ResultEnum {
    //结果枚举值
    SUCCESS(200, "成功"),

    SYS_ERR(500, "系统错误");

    public Integer code;
    public String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
