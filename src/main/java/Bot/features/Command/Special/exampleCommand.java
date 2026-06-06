package Bot.features.Command.Special;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class exampleCommand implements BasicCommand {

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
    }
}
