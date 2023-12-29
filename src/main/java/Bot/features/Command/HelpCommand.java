package Bot.features.Command;

import Bot.chatBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class HelpCommand {
    private static final EmbedBuilder eb = new EmbedBuilder();
    public static void Info(TextChannel tc) {
        eb.setTitle("! 도움1 : 발로란트 관련 명령어 \n"
                + "! 도움2: 기타 명령어 "
        );
        Main.showBuilderMeesage(tc, eb);
    }

    public static void Valorant(TextChannel tc) {
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

    public static void special(TextChannel tc) {
        eb.setTitle(" ! 용준컷: 용준이를 죽입니다. ");
        Main.showBuilderMeesage(tc, eb);
    }

}
