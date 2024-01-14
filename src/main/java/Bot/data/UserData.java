package Bot.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserData {
    @JsonIgnore
    private Member member;
    private int negativeSpeechCount = 0;
    private String name;
    private String id;
    private String discriminator;
    private String mention;
    public UserData() {
    }


    public UserData(String name, Member m, String id, String discriminator, String mention) {
        this.name = name;
        this.member = m;
        this.id = id;
        this.discriminator = discriminator;
        this.mention = mention;
    }

    public void isNegativeSpeechCount() {
        negativeSpeechCount++;
    }
}
