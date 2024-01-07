package Bot.features.Command.Help;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class HelpSpecialCommand implements BasicCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String Command) {
        eb.setTitle(" ! 용준컷: 용준이를 죽입니다. ");
        Main.showBuilderMeesage(tc, eb);
    }
}
