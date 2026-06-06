package Bot.features.Command.Link;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LinkCommand implements BasicCommand {

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        TextChannel tc = (TextChannel) event.getChannel();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("링크 모음");
        eb.addField("발로란트 전적 사이트", "https://dak.gg/valorant", false);
        eb.addField("하스스톤 야생 사이트", "https://tempostorm.com/hearthstone", false);
        Print.showBuilderMessage(tc, eb);
    }
}
