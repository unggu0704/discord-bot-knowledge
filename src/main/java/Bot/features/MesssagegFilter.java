package Bot.features;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

public class MesssagegFilter {

    private static final int limitNativeSpeechCount = 3;
    public static void profanityFilter(String msg, UserData user, Member target, TextChannel tc, Guild g, UserRepository userRepository) {
        Main.showMessage(tc, "부정적인 발언이 감지 되었습니다. 지속적으로 사용시 재제됨을 알려드립니다.");

        if (userRepository.findUser(user)) {
            user.isNegativeSpeechCount();
            Main.showMessage(tc, user.getName() + "님 경고!   (" + user.getNegativeSpeechCount() + "/3)");
        }

        if (user.getNegativeSpeechCount() >= limitNativeSpeechCount) {
            g.ban(target, 60, TimeUnit.SECONDS).queue();
            Main.showMessage(tc, user.getName() + "제거 완료!");
        }

    }

    public static boolean containsNegativeSpeech(String content) {
        for (NegativeWords speech : NegativeWords.values()) {
            if (content.contains(speech.name())) {
                return true;
            }
        }
        return false;
    }
}
