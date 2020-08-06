package wooteco.subway.members.member.acceptance;

import static wooteco.subway.members.member.acceptance.step.MemberAcceptanceStep.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.security.core.TokenResponse;
import wooteco.subway.common.acceptance.AcceptanceTest;

public class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);
        TokenResponse tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(tokenResponse);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        RestAssured.given().log().all().
                auth().preemptive().basic(EMAIL + "OTHER", PASSWORD).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/login/token").
                then().
                log().all().
                statusCode(HttpStatus.FOUND.value());
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        TokenResponse tokenResponse = new TokenResponse("accesstoken");

        RestAssured.given().log().all().
                auth().oauth2(tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/members/me").
                then().
                log().all().
                statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
