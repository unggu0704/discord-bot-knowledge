package Bot.features.Command.Valorant;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;

public class ValorantCommand implements BasicCommand {

    private static final List<ValorantExecute> gameList = new ArrayList<>();

    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String command) {
        switch (command) {
            case "발로란트":
                gameList.add(new ValorantExecute());
                Print.showMessage(tc, "발로란트 하실분 @here");
                return;
            case "해산":
                gameList.remove(findGuild(gameList, guild));
                Print.showMessage(tc, "수고하셨습니다.");
                return;
        }

        findGuild(gameList, guild).execute(userRepository, user, tc, guild, command);
    }

    public <T> T findGuild(List<T> listByGuild, Guild guild) {
        for (T thing : listByGuild) {
            if (thing.equals(guild)) {
                return thing;
            }
        }

        throw new RuntimeException("찾는 길드가 없음");
    }
}
