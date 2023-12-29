package Bot.features.Command;

import Bot.data.UserData;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
@Slf4j
public class FrontController {

    public static void handleCommand(String command, UserData user, TextChannel tc, Guild g) {
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
        }
    }
}
