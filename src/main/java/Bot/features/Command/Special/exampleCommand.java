package Bot.features.Command.Special;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class exampleCommand implements BasicCommand {

    @Override
    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String commaand) {
        Print.showMessage(tc, "예시 명령어");
    }
}
