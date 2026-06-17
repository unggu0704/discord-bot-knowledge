package Bot.valo.processor;

import Bot.valo.model.AccountInfo;
import Bot.valo.model.MatchSummary;
import Bot.valo.model.MmrInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Henrik API raw 응답을 Claude에 보낼 "정제된 JSON 문자열"로 가공한다.
 *
 * 실측 응답 기준 필드 경로:
 *  - v4/matches data[]: metadata.match_id, metadata.map.name, metadata.queue.name
 *  - players[]: puuid, team_id, agent.name, stats.{kills,deaths,assists,headshots,bodyshots,legshots,score,damage.dealt}
 *  - teams[]: team_id, rounds.{won,lost}, won(boolean)
 *  - mmr-history v2 data: { account, history[] }
 *  - history[]: tier.name, rr, last_change, map.name, date
 */
@Slf4j
public class MatchDataProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // =====================================================================
    // 조합 A: 최근 전적 분석 (Account + MMR + MMR History)
    // =====================================================================
    public String buildRecentAnalysisPayload(AccountInfo account, MmrInfo mmr, JsonNode mmrHistoryData) {
        ObjectNode root = objectMapper.createObjectNode();
        root.set("player", playerNode(account, mmr));

        // mmr-history v2: data = { "account": {...}, "history": [...] }
        JsonNode historyArr = mmrHistoryData.path("history");

        ArrayNode history = objectMapper.createArrayNode();
        if (historyArr.isArray()) {
            for (JsonNode entry : historyArr) {
                ObjectNode h = objectMapper.createObjectNode();
                h.put("tier", entry.path("tier").path("name").asText("Unknown"));
                h.put("rr", entry.path("rr").asInt(0));
                h.put("rrChange", entry.path("last_change").asInt(0));
                h.put("map", entry.path("map").path("name").asText(""));
                h.put("date", entry.path("date").asText(""));
                history.add(h);
            }
        }
        root.set("rankHistory", history);
        return toJsonString(root);
    }

    // =====================================================================
    // 조합 B: 플레이 스타일 분석 (Account + MMR + v4/matches)
    // =====================================================================
    public String buildStyleAnalysisPayload(AccountInfo account, MmrInfo mmr, JsonNode matchesData, String puuid) {
        ObjectNode root = objectMapper.createObjectNode();
        root.set("player", playerNode(account, mmr));

        List<MatchSummary> matches = summarizeMatches(matchesData, puuid);
        ArrayNode matchArr = objectMapper.createArrayNode();
        for (MatchSummary m : matches) {
            matchArr.add(matchSummaryNode(m));
        }
        root.set("recentMatches", matchArr);
        return toJsonString(root);
    }

    /**
     * v4/matches 응답(data 배열)에서 대상 puuid 유저의 성적만 추려 MatchSummary 리스트로 변환.
     * 나머지 9명의 데이터는 제거.
     */
    public List<MatchSummary> summarizeMatches(JsonNode matchesData, String puuid) {
        List<MatchSummary> result = new ArrayList<>();
        if (matchesData == null || !matchesData.isArray()) return result;

        for (JsonNode match : matchesData) {
            JsonNode metadata = match.path("metadata");
            JsonNode players = match.path("players");
            JsonNode teams = match.path("teams");

            // 대상 유저 찾기
            JsonNode target = findPlayer(players, puuid);
            if (target == null) {
                log.warn("match_id={} 에서 puuid={} 를 찾지 못했습니다.", metadata.path("match_id").asText(), puuid);
                continue;
            }

            JsonNode stats = target.path("stats");
            int kills = stats.path("kills").asInt(0);
            int deaths = stats.path("deaths").asInt(0);
            int assists = stats.path("assists").asInt(0);
            double hsPercent = headshotPercent(
                    stats.path("headshots").asInt(0),
                    stats.path("bodyshots").asInt(0),
                    stats.path("legshots").asInt(0));

            String teamId = target.path("team_id").asText("Red");
            int[] scores = extractTeamScores(teams, teamId);
            int myScore = scores[0];
            int enemyScore = scores[1];

            result.add(new MatchSummary(
                    metadata.path("map").path("name").asText(""),
                    target.path("agent").path("name").asText(""),
                    metadata.path("queue").path("name").asText(""),
                    resultLabel(myScore, enemyScore),
                    myScore,
                    enemyScore,
                    kills, deaths, assists,
                    round2(ratio(kills, deaths)),
                    round2(ratio(kills + assists, deaths)),
                    round2(hsPercent),
                    stats.path("score").asInt(0)
            ));
        }
        return result;
    }

    // =====================================================================
    // 조합 C: 특정 경기 분석 (v4/matches 단건)
    // =====================================================================
    /**
     * v4/matches 단건에서 대상 puuid 1명의 데이터만 추려 정제된 JSON으로 변환.
     * 나머지 9명, asset/id 값은 제거한다.
     */
    public String buildMatchAnalysisPayload(JsonNode matchData, String targetPuuid) {
        ObjectNode root = objectMapper.createObjectNode();

        JsonNode metadata = matchData.path("metadata");
        root.put("map", metadata.path("map").path("name").asText(""));
        root.put("mode", metadata.path("queue").path("name").asText(""));
        root.put("match_id", metadata.path("match_id").asText(""));

        JsonNode players = matchData.path("players");
        JsonNode teams = matchData.path("teams");
        JsonNode target = findPlayer(players, targetPuuid);

        if (target == null) {
            log.warn("매치에서 puuid={} 유저를 찾지 못했습니다.", targetPuuid);
            root.put("note", "target player not found in match");
            return toJsonString(root);
        }

        JsonNode stats = target.path("stats");
        int kills = stats.path("kills").asInt(0);
        int deaths = stats.path("deaths").asInt(0);
        int assists = stats.path("assists").asInt(0);
        double hsPercent = headshotPercent(
                stats.path("headshots").asInt(0),
                stats.path("bodyshots").asInt(0),
                stats.path("legshots").asInt(0));

        String teamId = target.path("team_id").asText("Red");
        int[] scores = extractTeamScores(teams, teamId);

        ObjectNode player = objectMapper.createObjectNode();
        player.put("agent", target.path("agent").path("name").asText(""));
        player.put("team", teamId);
        player.put("result", resultLabel(scores[0], scores[1]));
        player.put("score", scores[0] + ":" + scores[1]);
        player.put("kills", kills);
        player.put("deaths", deaths);
        player.put("assists", assists);
        player.put("kd", round2(ratio(kills, deaths)));
        player.put("kda", round2(ratio(kills + assists, deaths)));
        player.put("headshotPercent", round2(hsPercent));
        player.put("acs", stats.path("score").asInt(0));
        player.put("damageDealt", stats.path("damage").path("dealt").asInt(0));
        player.put("damageReceived", stats.path("damage").path("received").asInt(0));
        root.set("player", player);

        return toJsonString(root);
    }

    // =====================================================================
    // 공통 헬퍼
    // =====================================================================

    /** players 배열에서 puuid 가 일치하는 플레이어를 찾는다. */
    private JsonNode findPlayer(JsonNode players, String puuid) {
        if (!players.isArray()) return null;
        for (JsonNode p : players) {
            if (puuid.equals(p.path("puuid").asText())) return p;
        }
        return null;
    }

    /**
     * teams 배열에서 내 팀/상대 팀 라운드 점수를 추출한다.
     * teams[]: { team_id: "Red"|"Blue", rounds: { won, lost }, won: bool }
     * @return [myScore, enemyScore]
     */
    private int[] extractTeamScores(JsonNode teams, String myTeamId) {
        int myScore = 0, enemyScore = 0;
        if (teams.isArray()) {
            for (JsonNode t : teams) {
                int won = t.path("rounds").path("won").asInt(0);
                if (myTeamId.equals(t.path("team_id").asText())) {
                    myScore = won;
                } else {
                    enemyScore = won;
                }
            }
        }
        return new int[]{myScore, enemyScore};
    }

    private ObjectNode playerNode(AccountInfo account, MmrInfo mmr) {
        ObjectNode player = objectMapper.createObjectNode();
        player.put("name", account.getName());
        player.put("tag", account.getTag());
        player.put("level", account.getAccountLevel());
        player.put("currentTier", mmr.getCurrentTier());
        player.put("rr", mmr.getRankRating());
        player.put("lastRrChange", mmr.getLastChange());
        player.put("peakTier", mmr.getPeakTier());
        return player;
    }

    private ObjectNode matchSummaryNode(MatchSummary m) {
        ObjectNode n = objectMapper.createObjectNode();
        n.put("map", m.getMap());
        n.put("agent", m.getAgent());
        n.put("mode", m.getMode());
        n.put("result", m.getResult());
        n.put("score", m.getScore() + ":" + m.getEnemyScore());
        n.put("kills", m.getKills());
        n.put("deaths", m.getDeaths());
        n.put("assists", m.getAssists());
        n.put("kd", m.getKd());
        n.put("kda", m.getKda());
        n.put("headshotPercent", m.getHeadshotPercent());
        n.put("acs", m.getAverageCombatScore());
        return n;
    }

    private static double headshotPercent(int head, int body, int leg) {
        int total = head + body + leg;
        if (total == 0) return 0.0;
        return (head * 100.0) / total;
    }

    private static double ratio(int numerator, int denominator) {
        return denominator == 0 ? numerator : (double) numerator / denominator;
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static String resultLabel(int myScore, int enemyScore) {
        if (myScore > enemyScore) return "Victory";
        if (myScore < enemyScore) return "Defeat";
        return "Draw";
    }

    private String toJsonString(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (Exception e) {
            log.error("정제 데이터 직렬화 실패", e);
            return "{}";
        }
    }
}
