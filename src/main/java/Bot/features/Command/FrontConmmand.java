package Bot.features.Command;

import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.Special.*;
import Bot.features.Command.Help.*;
import Bot.features.Command.Valorant.*;
import Bot.features.Command.Help.HelpInfoCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class FrontConmmand {

    private static final Map<String, BasicCommand> COMMAND_MAP = new HashMap<>();

    // 기능 매핑
    static {
        COMMAND_MAP.put("도움", new HelpInfoCommand());
        COMMAND_MAP.put("도움1", new HelpSpecialCommand());
        COMMAND_MAP.put("도움2", new HelpValorantCommand());
        COMMAND_MAP.put("발로란트", new ValorantCommand());
        COMMAND_MAP.put("참가", new ValorantCommand());
        COMMAND_MAP.put("동원령", new ValorantCommand());
        COMMAND_MAP.put("나가기", new ValorantCommand());
        COMMAND_MAP.put("현재인원", new ValorantCommand());
        COMMAND_MAP.put("해산", new ValorantCommand());
        COMMAND_MAP.put("용준컷", new YongJunBan());
    }

    public static void handleCommand(UserRepository userRepository, String command, UserData user, TextChannel tc, Guild guild) {
        log.info("Command : " + command);

        for (String keyString : COMMAND_MAP.keySet()) {
            if (keyString.equals(command)) {
                COMMAND_MAP.get(keyString).execute(userRepository, user, tc, guild, command);
            }
        }
    }


}
