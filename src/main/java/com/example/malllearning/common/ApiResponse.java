package com.example.malllearning.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应结果")
public class ApiResponse<T> {

    @Schema(description = "状态码", example = "0")
    private Integer code;

    @Schema(description = "提示信息", example = "success")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    public ApiResponse() {}

    public ApiResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, "success", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(0, "success", null);
    }

    public static <T> ApiResponse<T> fail(Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }

    public void setCode(Integer code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
}
