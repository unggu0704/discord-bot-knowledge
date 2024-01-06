package Bot.features.Command.Link;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class LinkCommand implements BasicCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild) {
        eb.setTitle("발로란트 전적 사이트", " https://dak.gg/valorant");
        eb.setTitle("하스스톤 야생 사이트", "https://tempostorm.com/hearthstone");
        Main.showBuilderMeesage(tc, eb);
    }
}
