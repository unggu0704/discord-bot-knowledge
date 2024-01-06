package Bot.features.Command.Help;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class HelpValorantCommand implements BasicCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();

    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild) {
        eb.setTitle("! 발로란트 : 발로란트 파티 리스트를 엽니다. (온라인에게 알람이 갑니다.) \n"
                + "! 동원령 : 모든 플레이어들에게 알람이 갑니다. \n"
                + "! 참가: 발로란트 리스트에 본인을 추가합니다. \n"
                + "! 탈퇴: 발로란트 리스트에 본인을 제거합니다. \n"
                + "! 현재인원: 현재 리스트를 봅니다. \n"
                + "! 해산: 발로란트 파티 리스트를 제거합니다. \n"
                + "! 가이드: 발로란트 관련 가이드를 봅니다."
        );
        Main.showBuilderMeesage(tc, eb);
    }

}
