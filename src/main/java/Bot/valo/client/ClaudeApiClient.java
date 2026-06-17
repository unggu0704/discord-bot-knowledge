package Bot.valo.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Claude(Anthropic) Messages API 클라이언트 - OkHttp 기반 raw HTTP 호출.
 * (프로젝트가 OkHttp 를 쓰므로 공식 SDK 대신 raw HTTP 로 통일)
 *
 * endpoint: POST https://api.anthropic.com/v1/messages
 * 인증: x-api-key 헤더 (ANTHROPIC_API_KEY 환경변수)
 *
 * 시스템 프롬프트에는 prompt caching(cache_control)을 걸어,
 * 동일한 분석 지침을 반복 호출할 때 토큰 비용을 줄인다.
 */
@Slf4j
public class ClaudeApiClient {

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // 기획서 기준 Sonnet 사용. (기획서의 claude-sonnet-4-20250514 는 구버전 ID → 현재 ID 로 교체)
    private static final String MODEL = "claude-sonnet-4-6";
    private static final int MAX_TOKENS = 2000;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public ClaudeApiClient() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS) // 분석 응답이 길 수 있어 넉넉히
                .build();
        this.objectMapper = new ObjectMapper();

        this.apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            log.warn("ANTHROPIC_API_KEY 환경변수가 설정되지 않았습니다. Claude API 호출이 실패할 수 있습니다.");
        }
    }

    /**
     * Claude 에 분석을 요청하고, 응답 텍스트를 반환한다.
     *
     * @param systemPrompt 분석가 역할/지침 (캐싱 대상)
     * @param userContent  정제된 발로란트 데이터(JSON 문자열) + 분석 요청
     * @return Claude 가 생성한 분석 텍스트
     */
    public String analyze(String systemPrompt, String userContent) {
        String requestJson = buildRequestBody(systemPrompt, userContent);

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("x-api-key", apiKey == null ? "" : apiKey)
                .addHeader("anthropic-version", ANTHROPIC_VERSION)
                .addHeader("content-type", "application/json")
                .post(RequestBody.create(requestJson, JSON))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                throw new ClaudeApiException("Claude API 응답 오류 [" + response.code() + "] body=" + body);
            }
            return extractText(body);
        } catch (IOException e) {
            throw new ClaudeApiException("Claude API 호출 실패", e);
        }
    }

    /** Messages API 요청 본문을 구성한다. */
    private String buildRequestBody(String systemPrompt, String userContent) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", MODEL);
        root.put("max_tokens", MAX_TOKENS);

        // system: prompt caching 적용 (재사용되는 지침 블록)
        ArrayNode systemArr = objectMapper.createArrayNode();
        ObjectNode systemBlock = objectMapper.createObjectNode();
        systemBlock.put("type", "text");
        systemBlock.put("text", systemPrompt);
        systemBlock.set("cache_control", objectMapper.createObjectNode().put("type", "ephemeral"));
        systemArr.add(systemBlock);
        root.set("system", systemArr);

        // messages: 단일 user 턴
        ArrayNode messages = objectMapper.createArrayNode();
        ObjectNode userMsg = objectMapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", userContent);
        messages.add(userMsg);
        root.set("messages", messages);

        try {
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new ClaudeApiException("Claude 요청 직렬화 실패", e);
        }
    }

    /** 응답 JSON 에서 content[0].text 를 추출한다. */
    private String extractText(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode content = root.path("content");
            if (content.isArray() && content.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (JsonNode block : content) {
                    if ("text".equals(block.path("type").asText())) {
                        sb.append(block.path("text").asText());
                    }
                }
                return sb.toString();
            }
            throw new ClaudeApiException("Claude 응답에 text content 가 없습니다. body=" + body);
        } catch (IOException e) {
            throw new ClaudeApiException("Claude 응답 파싱 실패", e);
        }
    }
}
