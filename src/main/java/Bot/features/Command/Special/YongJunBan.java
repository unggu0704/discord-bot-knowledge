package Bot.features.Command.Special;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import Bot.features.Functions.Ban;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class YongJunBan implements BasicCommand {

    private static final String YONGJUN = "atfullblast";

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        TextChannel tc = (TextChannel) event.getChannel();
        if (event.getAuthor().getName().equals(YONGJUN)) {
            Print.showMessage(tc, "자살은 좋지 않아요!");
            return;
        }
        Ban.killMember(userRepository, tc, event.getGuild(), YONGJUN);
    }
}
