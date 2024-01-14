package Bot.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Slf4j
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

    @Override
    public boolean equals(@NotNull Object object) {
        UserData userData = (UserData) object;
        return Objects.equals(this.name, userData.name);
    }

    public Member getMemberInfo(Guild guild) {
        return guild.getMemberById(id);
    }

}
