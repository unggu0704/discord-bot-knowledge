package Bot.valo.client;

import Bot.valo.model.AccountInfo;
import Bot.valo.model.MmrInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Henrik 비공식 발로란트 API 클라이언트.
 * 응답은 모두 {@code { "status": 200, "data": {...} }} 형태로 감싸져 오므로,
 * 공통 GET 헬퍼에서 status 확인 후 "data" 노드를 반환한다.
 *
 * base URL: https://api.henrikdev.xyz
 * 인증: Authorization 헤더에 API 키 (HENRIK_API_KEY 환경변수)
 */
@Slf4j
public class HenrikApiClient {

    private static final String BASE_URL = "https://api.henrikdev.xyz";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public HenrikApiClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();

        this.apiKey = System.getenv("HENRIK_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            log.warn("HENRIK_API_KEY 환경변수가 설정되지 않았습니다. Henrik API 호출이 실패할 수 있습니다.");
        }
    }

    /**
     * 계정 정보 조회 → PUUID, region 확보.
     * GET /valorant/v2/account/{name}/{tag}
     */
    public AccountInfo getAccount(String name, String tag) {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegments("valorant/v2/account")
                .addPathSegment(name)
                .addPathSegment(tag)
                .build();

        JsonNode data = getData(url);

        // TODO: 실제 응답 필드명을 한 번 확인할 것 (puuid / region / account_level).
        return new AccountInfo(
                text(data, "puuid"),
                text(data, "name"),
                text(data, "tag"),
                text(data, "region"),
                data.path("account_level").asInt(0)
        );
    }

    /**
     * MMR(현재 랭크/RR) 조회.
     * GET /valorant/v3/mmr/{region}/pc/{name}/{tag}
     */
    public MmrInfo getMmr(String region, String name, String tag) {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegments("valorant/v3/mmr")
                .addPathSegment(region)
                .addPathSegment("pc")
                .addPathSegment(name)
                .addPathSegment(tag)
                .build();

        JsonNode data = getData(url);

        // TODO: v3 mmr 응답의 실제 구조 확인 (current.tier.name / current.rr / current.last_change / peak.tier.name).
        JsonNode current = data.path("current");
        JsonNode peak = data.path("peak");
        return new MmrInfo(
                current.path("tier").path("name").asText("Unranked"),
                current.path("rr").asInt(0),
                current.path("last_change").asInt(0),
                peak.path("tier").path("name").asText("Unranked")
        );
    }

    /**
     * MMR 변동 이력 (조합 A: 최근 전적 분석용).
     * GET /valorant/v2/mmr-history/{region}/pc/{name}/{tag}
     * 가공 없이 raw "data" 노드를 반환한다 (MatchDataProcessor 가 정제).
     */
    public JsonNode getMmrHistory(String region, String name, String tag) {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegments("valorant/v2/mmr-history")
                .addPathSegment(region)
                .addPathSegment("pc")
                .addPathSegment(name)
                .addPathSegment(tag)
                .build();
        return getData(url);
    }

    /**
     * 최근 경기 목록 (조합 A·B 공통 사용).
     * GET /valorant/v4/matches/{region}/pc/{name}/{tag}?mode=competitive&size=N
     * (curl 테스트 기준 v4 엔드포인트 사용)
     */
    public JsonNode getMatches(String region, String name, String tag, int size) {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegments("valorant/v4/matches")
                .addPathSegment(region)
                .addPathSegment("pc")
                .addPathSegment(name)
                .addPathSegment(tag)
                .addQueryParameter("mode", "competitive")
                .addQueryParameter("size", String.valueOf(size))
                .build();
        return getData(url);
    }

    /**
     * 특정 경기 상세 (조합 C: 특정 경기 분석용).
     * GET /valorant/v2/match/{matchId}
     */
    public JsonNode getMatchDetails(String matchId) {
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addPathSegments("valorant/v2/match")
                .addPathSegment(matchId)
                .build();
        return getData(url);
    }

    // ---------------------------------------------------------------------
    // 공통 헬퍼
    // ---------------------------------------------------------------------

    /** GET 요청을 보내고, status 검증 후 "data" 노드를 반환한다. */
    private JsonNode getData(HttpUrl url) {
        Request.Builder builder = new Request.Builder().url(url).get();
        if (apiKey != null && !apiKey.isEmpty()) {
            builder.addHeader("Authorization", apiKey);
        }

        try (Response response = httpClient.newCall(builder.build()).execute()) {
            String body = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                throw new HenrikApiException(
                        "Henrik API 응답 오류 [" + response.code() + "] url=" + url + " body=" + body);
            }

            JsonNode root = objectMapper.readTree(body);
            JsonNode data = root.get("data");
            if (data == null || data.isNull()) {
                throw new HenrikApiException("Henrik API 응답에 data 필드가 없습니다. url=" + url + " body=" + body);
            }
            return data;
        } catch (IOException e) {
            throw new HenrikApiException("Henrik API 호출 실패 url=" + url, e);
        }
    }

    /** 노드에서 텍스트 값을 안전하게 꺼낸다 (없으면 빈 문자열). */
    private static String text(JsonNode node, String field) {
        return node.path(field).asText("");
    }
}
