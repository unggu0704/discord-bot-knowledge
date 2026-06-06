package Bot.features.Command;

import Bot.data.UserData;
import Bot.data.UserRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface BasicCommand {
    void execute(UserRepository userRepository, MessageReceivedEvent event);

}
