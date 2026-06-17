package Bot.valo;

import Bot.valo.client.ClaudeApiClient;
import Bot.valo.client.HenrikApiClient;
import Bot.valo.model.AccountInfo;
import Bot.valo.model.MmrInfo;
import Bot.valo.processor.MatchDataProcessor;
import Bot.valo.prompt.ValoPrompt;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

/**
 * 발로코치 핵심 비즈니스 로직.
 * Henrik API 호출 → 데이터 정제 → Claude 분석 의 흐름을 조합한다.
 * 리스너(ValoIntelListener)는 이 서비스를 호출만 한다 (관심사 분리).
 */
@Slf4j
public class ValoCoachService {

    private static final int RECENT_MATCH_SIZE = 5;

    private final HenrikApiClient henrikClient;
    private final ClaudeApiClient claudeClient;
    private final MatchDataProcessor processor;

    public ValoCoachService() {
        this(new HenrikApiClient(), new ClaudeApiClient(), new MatchDataProcessor());
    }

    public ValoCoachService(HenrikApiClient henrikClient, ClaudeApiClient claudeClient, MatchDataProcessor processor) {
        this.henrikClient = henrikClient;
        this.claudeClient = claudeClient;
        this.processor = processor;
    }

    /** 슬래시 커맨드 단계에서 유저 존재 확인 + region/puuid 확보. */
    public AccountInfo resolveAccount(String name, String tag) {
        return henrikClient.getAccount(name, tag);
    }

    /**
     * 조합 B: 플레이 스타일 분석.
     * Account + MMR + v4/matches(5게임)
     */
    public String analyzeStyle(String name, String tag) {
        AccountInfo account = henrikClient.getAccount(name, tag);
        MmrInfo mmr = henrikClient.getMmr(account.getRegion(), name, tag);
        JsonNode matches = henrikClient.getMatches(account.getRegion(), name, tag, RECENT_MATCH_SIZE);

        String payload = processor.buildStyleAnalysisPayload(account, mmr, matches, account.getPuuid());
        return claudeClient.analyze(ValoPrompt.STYLE.getSystemPrompt(), payload);
    }

    /**
     * 조합 A: 최근 전적 분석.
     * Account + MMR + MMR History
     */
    public String analyzeRecent(String name, String tag) {
        AccountInfo account = henrikClient.getAccount(name, tag);
        MmrInfo mmr = henrikClient.getMmr(account.getRegion(), name, tag);
        // mmr-history v2: data = { "account": {...}, "history": [...] }
        JsonNode mmrHistoryData = henrikClient.getMmrHistory(account.getRegion(), name, tag);

        String payload = processor.buildRecentAnalysisPayload(account, mmr, mmrHistoryData);
        return claudeClient.analyze(ValoPrompt.RECENT.getSystemPrompt(), payload);
    }

    /**
     * 조합 C: 특정 경기 분석.
     * 현재는 가장 최근 경기 1건 자동 분석.
     * TODO: 경기 선택 UI(SelectMenu) 추가 후 matchId 를 파라미터로 받도록 개선.
     */
    public String analyzeMatch(String name, String tag) {
        AccountInfo account = henrikClient.getAccount(name, tag);
        JsonNode matches = henrikClient.getMatches(account.getRegion(), name, tag, 1);

        String matchId = extractFirstMatchId(matches);
        if (matchId == null) {
            return "분석할 최근 경기를 찾지 못했습니다.";
        }

        // v4/matches 단건에서 첫 번째 match 노드 그대로 사용 (getMatchDetails 불필요)
        JsonNode matchData = matches.get(0);
        String payload = processor.buildMatchAnalysisPayload(matchData, account.getPuuid());
        return claudeClient.analyze(ValoPrompt.MATCH.getSystemPrompt(), payload);
    }

    /**
     * v4/matches data[0].metadata.match_id 추출.
     * 실측 응답: metadata.match_id (문자열)
     */
    private String extractFirstMatchId(JsonNode matches) {
        if (matches != null && matches.isArray() && matches.size() > 0) {
            String id = matches.get(0).path("metadata").path("match_id").asText(null);
            return (id == null || id.isEmpty()) ? null : id;
        }
        return null;
    }
}
