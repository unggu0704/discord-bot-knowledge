package Bot.chatBot.Message.Filter;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.managers.Presence;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MessageFilter {

    private static final int limitNativeSpeechCount = 3;
    private static Random random = new Random();
    public static void profanityFilter(UserData user,  TextChannel tc, UserRepository userRepository) {
        Print.showMessage(tc, "부정적인 발언이 감지 되었습니다. 지속적으로 사용시 재제됨을 알려드립니다.");

        Member target = user.getMember();
        Guild g = target.getGuild();

        if (userRepository.findUser(user)) {
            user.isNegativeSpeechCount();

            Print.showMessage(tc, user.getName() + "님 경고!   (" + user.getNegativeSpeechCount() + "/3)");
            userRepository.saveJsonUser();

        }
        if (user.getNegativeSpeechCount() >= limitNativeSpeechCount) {
            Print.showMessage(tc, user.getName() + "를 **제거했습니다.**");
            g.ban(target, 60, TimeUnit.SECONDS).queue();
        }

    }

    /*
        사용자의 온/오프라인 형태를 구별하는 메서드
        JDA를 통해 getPresence()로 갱신해줘야 현재 상태가 반영이된다.
        단순히 member.getOnlineStatus()로 하면 항상 OFFLINE으로 표시되는 버그가 존재
     */
    public static void checkOffline(TextChannel tc, Member member) {

        if (member == null) {
            log.info("멤버 정보를 가져올 수 없음");
            return;
        }

        OnlineStatus onlineStatus= member.getOnlineStatus();
        tc.getGuild().retrieveMember(member.getUser()).queue(fetchedMember -> {
            OnlineStatus onlineStatus1 = fetchedMember.getOnlineStatus();
            log.info("현재 상태 (fetch): " + onlineStatus1);
        });

        log.info("현재 상태 : " + onlineStatus);

        String displayName = member.getEffectiveName();

        if (onlineStatus == OnlineStatus.OFFLINE) {
            int messageRandom = random.nextInt(2);

            if (messageRandom == 1)
                Print.showMessage(tc, displayName + "님은 현재 **오프라인 상태**에서 말하고 있습니다.");

            return;
        } else if (onlineStatus == OnlineStatus.ONLINE) {
            return;
        }
        int messageRandom = random.nextInt(4);

        if (messageRandom == 3)
            Print.showMessage(tc, member.getNickname() + "님은 온라인 상태로 바꾸는게 좋아보여요!");
    }

    public static boolean containsNegativeSpeech(String content) {
            content = content.replaceAll("\\s+", "");
            for (NegativeWords speech : NegativeWords.values()) {
                if (content.contains(speech.name())) {
                    return true;
                }
            }
            return false;
    }
}
