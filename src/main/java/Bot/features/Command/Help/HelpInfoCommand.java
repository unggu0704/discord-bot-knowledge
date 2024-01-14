package Bot.features.Command.Help;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class HelpInfoCommand implements BasicCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String Command) {
        eb.setTitle("!도움1 : 발로란트 관련 명령어 \n"
                + "!도움2: 기타 명령어 "
        );
        Main.showBuilderMeesage(tc, eb);

    }
}
