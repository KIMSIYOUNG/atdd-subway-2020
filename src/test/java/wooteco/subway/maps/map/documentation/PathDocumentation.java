package wooteco.subway.maps.map.documentation;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.web.context.WebApplicationContext;

import wooteco.security.core.TokenResponse;
import wooteco.subway.common.documentation.Documentation;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.map.ui.MapController;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;
import wooteco.subway.members.member.domain.LoginMember;

@WebMvcTest(controllers = MapController.class)
public class PathDocumentation extends Documentation {
    @MockBean
    private MapService mapService;

    protected TokenResponse tokenResponse;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token");
    }

    @DisplayName("거리 기준으로 경로를 찾는 경우를 문서화합니다.")
    @Test
    void findPathByDistance() {
        final PathResponse response = new PathResponse(
            Arrays.asList(StationResponse.of(new Station("강남")), StationResponse.of(new Station("판교"))),
            40,
            30,
            1550
        );
        when(mapService.findPath(anyLong(), anyLong(), any(), any(LoginMember.class))).thenReturn(response);

        given().log().all().
            header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, PathType.DISTANCE).
            then().
            log().all().
            apply(document("paths/distance/with-header",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer auth credentials")),
                requestParameters(
                    parameterWithName("source").description("출발역 아이디"),
                    parameterWithName("target").description("도착역 아이디"),
                    parameterWithName("type").description("조회 기준")
                ),
                responseFields(
                    fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 및 기타 정보"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NULL).description("경로 역들의 아이디"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("경로 역의 이름"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("걸리는 시간"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("걸리는 거리"),
                    fieldWithPath("totalMoney").type(JsonFieldType.NUMBER).description("총 요금")
                )
            )).
            extract();
    }

    @DisplayName("시간 기준으로 경로를 찾는 경우를 문서화합니다.")
    @Test
    void findPathByDuration() {
        final PathResponse response = new PathResponse(
            Arrays.asList(StationResponse.of(new Station("강남")), StationResponse.of(new Station("판교"))),
            30,
            20,
            1350
        );
        when(mapService.findPath(anyLong(), anyLong(), any(), any(LoginMember.class))).thenReturn(response);

        given().log().all().
            header("Authorization", "Bearer " + tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, PathType.DURATION).
            then().
            log().all().
            apply(document("paths/duration/with-header",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer auth credentials")),
                requestParameters(
                    parameterWithName("source").description("출발역 아이디"),
                    parameterWithName("target").description("도착역 아이디"),
                    parameterWithName("type").description("조회 기준")
                ),
                responseFields(
                    fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 및 기타 정보"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NULL).description("경로 역들의 아이디"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("경로 역의 이름"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("걸리는 시간"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("걸리는 거리"),
                    fieldWithPath("totalMoney").type(JsonFieldType.NUMBER).description("총 요금")
                )
            )).
            extract();
    }

    @DisplayName("로그인 되어 있지 않는 상태의 (헤더가 없는 상태로) 경로 조회를 문서화합니다.")
    @Test
    void findPathByDurationWithoutHeader() {
        final PathResponse response = new PathResponse(
            Arrays.asList(StationResponse.of(new Station("강남")), StationResponse.of(new Station("판교"))),
            30,
            20,
            1350
        );
        when(mapService.findPath(anyLong(), anyLong(), any(), any(LoginMember.class))).thenReturn(response);

        given().log().all().
            accept(MediaType.APPLICATION_JSON_VALUE).
            when().
            get("/paths?source={sourceId}&target={targetId}&type={type}", 1L, 3L, PathType.DURATION).
            then().
            log().all().
            apply(document("paths/duration/without-header",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                    parameterWithName("source").description("출발역 아이디"),
                    parameterWithName("target").description("도착역 아이디"),
                    parameterWithName("type").description("조회 기준")
                ),
                responseFields(
                    fieldWithPath("stations").type(JsonFieldType.ARRAY).description("경로 및 기타 정보"),
                    fieldWithPath("stations[].id").type(JsonFieldType.NULL).description("경로 역들의 아이디"),
                    fieldWithPath("stations[].name").type(JsonFieldType.STRING).description("경로 역의 이름"),
                    fieldWithPath("duration").type(JsonFieldType.NUMBER).description("걸리는 시간"),
                    fieldWithPath("distance").type(JsonFieldType.NUMBER).description("걸리는 거리"),
                    fieldWithPath("totalMoney").type(JsonFieldType.NUMBER).description("총 요금")
                )
            )).
            extract();
    }
}
