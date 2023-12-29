package Bot.data;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class UserData {
    @Getter
    public User user;
    Member member;
    @Getter
    public int negativeSpeechCount = 0;
    @Getter
    public String name;

    public UserData(User user, String name, Member m) {
        this.name = name;
        this.user = user;
        this.member = m;
    }

    public UserData(String name) {
        this.name = name;
    }

    public void isNegativeSpeechCount() {
        negativeSpeechCount++;
    }

    public User Getuser() {
        return user;
    }

}
