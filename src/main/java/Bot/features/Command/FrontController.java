package Bot.features.Command;

import Bot.chatBot.Main;
import Bot.data.UserData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FrontController {

    private static final List<ValorantCommand> gameList = new ArrayList<>();
    public static void handleCommand(String command, UserData user, TextChannel tc, Guild guild) {
        log.info("Command : " + command);
        switch (command) {
            case "도움":
                HelpCommand.Info(tc);
                break;
            case "도움1":
                HelpCommand.Valorant(tc);
                break;
            case "도움2":
                HelpCommand.special(tc);
                break;
            case "발로란트":
                gameList.add(new ValorantCommand(guild, tc));
                log.info("발로란트 게임이 시작됨");
                break;
            case "동원령":
                findGuild(gameList, guild).forceStartGame();
                break;
            case "참가":
                findGuild(gameList, guild).joinGame(user);
                break;
            case "나가기":
                findGuild(gameList, guild).exitGame(user);
                break;
            case "현재인원":
                findGuild(gameList, guild).printUserList();
                break;
            case "해산":
                Main.showMessage(tc, "수고하셨습니다.");
                gameList.remove(findGuild(gameList, guild));
                break;
        }
    }

    public static<T> T findGuild(List<T> listByGuild, Guild guild) {
        for (T thing : listByGuild) {
            if (thing.equals(guild)) {
                return thing;
            }
        }

        throw new RuntimeException("[ERROR] 찾는 길드가 없습니다.");
    }
}
