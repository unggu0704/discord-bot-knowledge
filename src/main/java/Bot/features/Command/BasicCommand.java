package Bot.features.Command;

import Bot.data.UserData;
import Bot.data.UserRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface BasicCommand {
    void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String commaand);

}
