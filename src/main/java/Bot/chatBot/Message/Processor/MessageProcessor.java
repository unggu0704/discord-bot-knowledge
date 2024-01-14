package Bot.chatBot.Message.Processor;

import Bot.chatBot.Message.Filter.MessageValidator;
import Bot.chatBot.Message.Filter.MesssagegFilter;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.FrontConmmand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageProcessor {

    public static void processReceivedMessage(UserRepository userRepository, MessageReceivedEvent event) {

        User user = event.getAuthor();
        TextChannel tc = (TextChannel) event.getChannel();
        Message msg = event.getMessage();
        Guild g = event.getGuild();
        Member member = event.getMessage().getMember();

        if(MessageValidator.validateMessage(user, msg)) //메세지의 유효성 검사
            return;

        UserData userData = new UserData(user.getName(), member, user.getId(), user.getDiscriminator(), user.getAsMention());

        if (!userRepository.findUser(userData))
            userRepository.saveUser(userData);


        String messageContent = msg.getContentRaw(); // 채팅 메세지
        if (MesssagegFilter.containsNegativeSpeech(messageContent)) {
            MesssagegFilter.profanityFilter(messageContent, userData, member, tc, g, userRepository);
            return;
        }

        //명령어 symbol 체크
        if (!messageContent.isEmpty() || messageContent.charAt(0) == '!') {
            String command = messageContent.substring(1);
            //명령어 핸들러로 이동
            FrontConmmand.handleCommand(userRepository, command, userData, tc, g);
        }
    }
}
