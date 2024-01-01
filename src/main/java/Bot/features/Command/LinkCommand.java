package Bot.features.Command;

import Bot.chatBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class LinkCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public static void provideLink(TextChannel tc)  {
        eb.setTitle("발로란트 전적 사이트", " https://dak.gg/valorant");
        eb.setTitle("하스스톤 야생 사이트", "https://tempostorm.com/hearthstone");
        Main.showBuilderMeesage(tc, eb);
    }
}
