package Bot.features.Command.Special;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import Bot.features.Functions.Ban;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class YongJunBan implements BasicCommand {

    private static final String YONGJUN = "atfullblast";
    @Override
    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String commaand) {
        if (user.getName().equals(YONGJUN) || user.getName().equals("unggu")) {
            Main.showMessage(tc, "자살은 좋지 않아요!");
        }

        Ban.execute(userRepository, tc, guild, YONGJUN);
    }
}
