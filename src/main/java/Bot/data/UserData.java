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

    public UserData(User user, String name, Member m) {
        this.name = name;
        this.user = user;
        this.member = m;
    }


    public void isNegativeSpeechCount() {
        negativeSpeechCount++;
    }

}
