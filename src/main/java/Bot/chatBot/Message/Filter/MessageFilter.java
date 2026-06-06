package Bot.chatBot.Message.Filter;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class MessageFilter {

    private static final int LIMIT_NEGATIVE_SPEECH_COUNT = 3;

    public static void profanityFilter(MessageReceivedEvent event, UserRepository userRepository, UserData user) {
        TextChannel tc = (TextChannel) event.getChannel();
        Guild g = event.getGuild();
        Member member = event.getMember();

        Print.showMessage(tc, "부정적인 발언이 감지 되었습니다. 지속적으로 사용시 제재됨을 알려드립니다.");

        if (userRepository.findUser(user)) {
            user.isNegativeSpeechCount();
            Print.showMessage(tc, user.getName() + "님 경고! (" + user.getNegativeSpeechCount() + "/3)");
        }

        if (user.getNegativeSpeechCount() >= LIMIT_NEGATIVE_SPEECH_COUNT) {
            g.ban(member, 0, TimeUnit.SECONDS).queue();
            Print.showMessage(tc, user.getName() + " 제거 완료!");
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
