package Bot.features.Command;

import Bot.chatBot.Main;
import Bot.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

@Slf4j
public class BanCommand {

    private static final String USER_NAME = "";
    private static final String YONGJUN = "AT FULL BLAST";

    public static void isBan(UserRepository userRepository, TextChannel tc, Guild guild, String userName) {

        Member banMember = userRepository.getMemberByName(userName);
        log.info(banMember.toString() + "을 찾으러감");
        if (banMember == null) {
            Main.showMessage(tc, "밴 할 유저를 찾을수가 없습니다.");
            return;
        }

        guild.ban(banMember, 600, TimeUnit.SECONDS).queue();
        log.info("ban 실행");
        Main.showMessage(tc, "bye~ " + userName + "씨~");
    }

    public static void yongJunKill(UserRepository userRepository, TextChannel tc, Guild guild) {
        isBan(userRepository, tc, guild, YONGJUN);
    }



}
