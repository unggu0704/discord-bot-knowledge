package Bot.chatBot.Message.Print;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class Print {

    public static void showMessage(TextChannel tc, String message) {
        tc.sendMessage(message).queue();
    }
    public static void showBuilderMeesage(TextChannel tc, EmbedBuilder eb) {
        tc.sendMessageEmbeds(eb.build()).queue();
    }
}
