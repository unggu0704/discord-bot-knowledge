package Bot.features.Command.Valorant;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class ValorantCommand implements BasicCommand {

    private static final List<ValorantExecute> gameList = new ArrayList<>();

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        TextChannel tc = (TextChannel) event.getChannel();
        Guild guild = event.getGuild();
        String command = event.getMessage().getContentRaw().substring(1);

        switch (command) {
            case "발로란트":
                gameList.add(new ValorantExecute(tc, guild));
                Print.showMessage(tc, "발로란트 하실분 @here");
                return;
            case "해산":
                ValorantExecute session = findSession(guild);
                if (session != null) {
                    gameList.remove(session);
                    Print.showMessage(tc, "수고하셨습니다.");
                }
                return;
        }

        ValorantExecute session = findSession(guild);
        if (session != null) {
            session.execute(userRepository, event);
        }
    }

    private ValorantExecute findSession(Guild guild) {
        for (ValorantExecute session : gameList) {
            if (session.isGuild(guild)) {
                return session;
            }
        }
        return null;
    }
}
