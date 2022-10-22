package com.innocamp.dduha.dto;

import com.innocamp.dduha.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private T data;
    private ErrorCode code;
    private String message;

    public ResponseDto(T data, ErrorCode error){
        this.data = data;
        this.code = error;
        this.message = error.getMessage();
    }

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data, ErrorCode.NULL);
    }

    public static <T> ResponseDto<T> fail(ErrorCode error) {
        return new ResponseDto<>(null, error);
    }
}
