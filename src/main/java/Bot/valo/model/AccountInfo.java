package Bot.valo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Henrik Account v2 응답에서 필요한 값만 추린 DTO.
 * 엔드포인트: GET /valorant/v2/account/{name}/{tag}
 */
@Getter
@ToString
@AllArgsConstructor
public class AccountInfo {
    private final String puuid;
    private final String name;
    private final String tag;
    /** 계정이 속한 지역 (kr, ap, na ...). MMR 등 다른 API 호출에 필요. */
    private final String region;
    private final int accountLevel;
}
