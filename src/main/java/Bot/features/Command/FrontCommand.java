package Bot.features.Command;

import Bot.data.UserRepository;
import Bot.features.Command.Link.LinkCommand;
import Bot.features.Command.Special.*;
import Bot.features.Command.Help.*;
import Bot.features.Command.Valorant.*;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FrontCommand {

    private static final ValorantCommand valorantCommand = new ValorantCommand();
    private static final Map<String, BasicCommand> COMMAND_MAP = new HashMap<>();

    static {
        COMMAND_MAP.put("도움", new HelpInfoCommand());
        COMMAND_MAP.put("도움1", new HelpValorantCommand());
        COMMAND_MAP.put("도움2", new HelpSpecialCommand());
        COMMAND_MAP.put("링크", new LinkCommand());
        COMMAND_MAP.put("발로란트", valorantCommand);
        COMMAND_MAP.put("참가", valorantCommand);
        COMMAND_MAP.put("동원령", valorantCommand);
        COMMAND_MAP.put("나가기", valorantCommand);
        COMMAND_MAP.put("현재인원", valorantCommand);
        COMMAND_MAP.put("해산", valorantCommand);
        COMMAND_MAP.put("용준컷", new YongJunBan());
    }

    public static void handleCommand(UserRepository userRepository, String command, MessageReceivedEvent event) {
        log.info("Command : " + command);
        BasicCommand cmd = COMMAND_MAP.get(command);
        if (cmd != null) {
            cmd.execute(userRepository, event);
        }
    }
}
