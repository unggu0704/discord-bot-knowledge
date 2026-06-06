package Bot.features.Command.Help;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpInfoCommand implements BasicCommand {

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        TextChannel tc = (TextChannel) event.getChannel();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("!도움1 : 발로란트 관련 명령어 \n"
                + "!도움2: 기타 명령어 "
        );
        Print.showBuilderMessage(tc, eb);
    }
}
