package Bot.features.Command.Help;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpSpecialCommand implements BasicCommand {

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        TextChannel tc = (TextChannel) event.getChannel();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("!링크: 다양한 링크를 제공받습니다. \n" +
                "!용준컷: 용준이를 죽입니다. ");
        Print.showBuilderMessage(tc, eb);
    }
}
