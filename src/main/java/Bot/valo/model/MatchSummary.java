package Bot.valo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 한 경기에서 "조회 대상 유저"의 성적만 추려 정제한 DTO.
 * 다른 9명의 데이터, ID/asset 값은 모두 제거하고, KD/KDA/HS%는 미리 계산한다.
 * (명세서의 "데이터 정제" 단계 결과물 - 약 80% 용량 절감)
 */
@Getter
@ToString
@AllArgsConstructor
public class MatchSummary {
    private final String map;
    private final String agent;
    private final String mode;
    /** "Victory" / "Defeat" / "Draw". */
    private final String result;
    private final int score;        // 우리 팀 라운드 획득 수
    private final int enemyScore;   // 상대 팀 라운드 획득 수

    private final int kills;
    private final int deaths;
    private final int assists;

    private final double kd;        // kills / deaths
    private final double kda;       // (kills + assists) / deaths
    private final double headshotPercent;

    private final int averageCombatScore; // ACS
}
