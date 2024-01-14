package Bot.data;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * 추후 UserData에서 User 나 Member 객체 사용시에 사용될 DTO클래스
 * 현재는 사용하지 않는다.
 * @author unggu
 *
 */
@Slf4j
public class UserDTO {
    private String id;
    private String discriminator;
    private String name;
    private String avatarId; // 추가

    @JsonCreator
    public UserDTO(
            @JsonProperty("id") String id,
            @JsonProperty("discriminator") String discriminator,
            @JsonProperty("name") String name,
            @JsonProperty("avatarId") String avatarId) {
        this.id = id;
        this.discriminator = discriminator;
        this.name = name;
        this.avatarId = avatarId;
    }

    public String getId() {
        return id;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getName() {
        return name;
    }

    public String getAvatarId() {
        return avatarId;
    }
}
