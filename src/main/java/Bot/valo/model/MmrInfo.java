package Bot.valo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Henrik MMR v3 응답에서 필요한 값만 추린 DTO.
 * 엔드포인트: GET /valorant/v3/mmr/{region}/pc/{name}/{tag}
 */
@Getter
@ToString
@AllArgsConstructor
public class MmrInfo {
    /** 현재 티어 이름 (예: "Diamond 2"). */
    private final String currentTier;
    /** 티어 내 점수 (0~100). */
    private final int rankRating;
    /** 직전 게임 RR 변동량. */
    private final int lastChange;
    /** 시즌 최고 티어 이름. */
    private final String peakTier;
}
