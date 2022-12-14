package com.innocamp.dduha.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 에러 없음
    NULL("NO_ERROR", "에러 없음"),

    // 로그인, 회원 가입 관련 에러
    DUPLICATE_EMAIL("DUPLICATE_EMAIL", "중복된 이메일입니다."),
    DUPLICATE_NICKNAME("DUPLICATE_NICKNAME", "중복된 닉네임입니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "회원정보를 찾을 수 없습니다."),
    INVALID_PASSWORD("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    NEED_LOGIN("NEED_LOGIN", "로그인이 필요합니다"),
    USED_PASSWORD("USED_PASSWORD", "기존 비밀번호와 일치합니다."),
    ALREADY_REQUESTED_EMAIL("ALREADY_REQUESTED_EMAIL", "이미 요청한 이메일 입니다."),
    EXPIRED_CODE("EXPIRED_CODE", "만료된 링크입니다."),
    INVALID_CODE("INVALID_CODE", "유효하지 않은 링크입니다."),
    CODE_NOT_FOUND("CODE_NOT_FOUND", "코드를 찾을 수 없습니다."),
    EMAIL_NOT_FOUND("EMAIL_NOT_FOUND", "이메일을 찾을 수 없습니다."),
    OAUTH_LOGIN_FAILED("OAUTH_LOGIN_FAILED", "소셜 로그인에 실패했습니다."),

    // 토큰 관련 오류
    INVALID_SIGNATURE("INVALID_SIGNATURE", "잘못된 JWT 서명입니다."),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", "지원하지 않는 JWT 토큰입니다."),
    INVALID_TOKEN("INVALID_TOKEN","잘못된 JWT 토큰입니다."),

    // Trip(일정) 관련 오류
    TRIP_NOT_FOUND("TRIP_NOT_FOUND", "일정을 찾을 수 없습니다."),
    COURSE_NOT_FOUND("COURSE_NOT_FOUND", "해당 일정을 찾을 수 없습니다."),
    DETAIL_NOT_FOUND("DETAIL_NOT_FOUND", "해당 상세 정보를 찾을 수 없습니다."),

    // TouristSpot(관광지) 관련 오류
    TOURISTSPOT_NOT_FOUND("TOURISTSPOT_NOT_FOUND", "해당 관광지를 찾을 수 없습니다."),

    // Accommodation(숙소) 관련 오류
    ACCOMMODATION_NOT_FOUND("ACCOMMODATION_NOT_FOUND", "해당 숙소를 찾을 수 없습니다."),

    // Restaurant(맛집) 관련 오류
    RESTAURANT_NOT_FOUND("RESTAURANT_NOT_FOUND", "해당 맛집을 찾을 수 없습니다."),

    // Review(리뷰) 관련 오류
    REVIEW_NOT_FOUND("REVIEW_NOT_FOUND", "해당 리뷰를 찾을 수 없습니다."),

    // Course(코스) 관련 오류
    INVALID_CATEGORY("INVALID_CATEGORY", "잘못된 분류 입니다."),
    EXCEED_MAX_PLACES("EXCEED_MAX_PLACES", "최대 저장 개수를 초과하였습니다."),

    //수정, 삭제권한 관련 오류
    REQUEST_FORBIDDEN("REQUEST_FORBIDDEN", "권한이 없습니다.");


    private final String code;
    private final String message;
}
