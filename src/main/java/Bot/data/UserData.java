package Bot.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserData {
    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    private Member member;
    @Getter
    @Setter
    private int negativeSpeechCount = 0;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String discriminator;

    public UserData() {
    }


    public UserData(User user, String name, Member m) {
        this.name = name;
        this.user = user;
        this.member = m;
        this.id = user.getId();
        this.discriminator = user.getDiscriminator();
    }

    public void isNegativeSpeechCount() {
        negativeSpeechCount++;
    }
}
