package Bot.data;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class UserData {
    @Getter
    private User user;
    @Getter
    private Member member;
    @Getter
    private int negativeSpeechCount = 0;
    @Getter
    private String name;
    @Getter
    private String id;
    @Getter
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
