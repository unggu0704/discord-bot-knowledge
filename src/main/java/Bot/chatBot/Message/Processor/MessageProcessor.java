package Bot.chatBot.Message.Processor;

import Bot.chatBot.Message.Filter.MessageValidator;
import Bot.chatBot.Message.Filter.MessageFilter;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.FrontConmmand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;


@Slf4j
public class MessageProcessor {


    public static void processReceivedMessage(UserRepository userRepository, MessageReceivedEvent event) {

        User user = event.getAuthor();
        TextChannel tc = (TextChannel) event.getChannel();
        Message msg = event.getMessage();
        Guild g = event.getGuild();
        Member member = event.getMessage().getMember();
        String messageContent = msg.getContentRaw();


        if(MessageValidator.validateMessage(user, msg)) //메세지의 유효성 검사
            return;

        checkOAuth2(g, tc);

        UserData userData = new UserData(user.getName(), member, user.getId(), user.getDiscriminator(), user.getAsMention());

        if (!userRepository.findUser(userData))
            userRepository.saveUser(userData);

        preProcessMessage(userData, messageContent, tc, userRepository);

        if (messageContent.charAt(0) == '!' && messageContent.length() > 1) {
            String command = messageContent.substring(1);
            FrontConmmand.handleCommand(userRepository, command, userData, tc, g);
        }
    }

    /**
     * 메세지에 대한 전처리 검사
     * 1. 부정적인 단어 체크
     * 2. 오프라인 상태 체크
     * @param userData
     * @param messageContent
     * @param tc
     * @param userRepository
     * @return
     */
    private static void preProcessMessage(UserData userData, String messageContent, TextChannel tc, UserRepository userRepository) {

        //부정적인 단어 체크
        if (MessageFilter.containsNegativeSpeech(messageContent)) {
            MessageFilter.profanityFilter(userData,tc, userRepository);
        }

        MessageFilter.checkOffline(tc, userData.getMember());

    }

    private static void checkOAuth2(Guild guild, TextChannel tc) {
        Member selfMember = guild.getSelfMember(); // 봇의 Member 객체 가져오기

        if (tc != null) {
            EnumSet<Permission> botPermissions = selfMember.getPermissions(tc); // 채널에서 봇의 권한 조회

            System.out.println("봇의 권한: " + botPermissions);
        }
    }
}
