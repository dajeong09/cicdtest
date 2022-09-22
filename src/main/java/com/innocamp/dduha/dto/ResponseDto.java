package com.innocamp.dduha.dto;

import com.innocamp.dduha.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private Boolean isSuccess;
    private T data;
    private ErrorCode code;
    private String message;

    public ResponseDto(Boolean isSuccess, T data, ErrorCode error){
        this.isSuccess = isSuccess;
        this.data = data;
        this.code = error;
        this.message = error.getMessage();
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, ErrorCode.NULL);
    }

    public static <T> ResponseDto<T> fail(ErrorCode error) {
        return new ResponseDto<>(false, null, error);
    }
}
