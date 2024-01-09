
package Bot.chatBot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Bot.data.UserData;

import Bot.data.UserRepository;
import Bot.features.Command.FrontConmmand;
import Bot.features.MesssagegFilter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;


@Slf4j
public class Main extends ListenerAdapter {
    private final UserRepository userRepository;


    public static void main(String[] args) {
        log.info("봇이 실행됨!");
        String Token = readTokenFromFile("Token.txt");
        if (Token.equals("Empty")) {
            throw new RuntimeException("[ERROR] | 토큰을 읽는데 실패함!");
        }

        JDA jda = JDABuilder.createDefault(Token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("test!"));
        jda.addEventListener(new Main());

    }

    Main() {
        userRepository = new UserRepository();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        User user = event.getAuthor();
        TextChannel tc = (TextChannel) event.getChannel();
        Message msg = event.getMessage();
        Guild g = event.getGuild();
        Member member = event.getMessage().getMember();

        UserData chatUser = new UserData(user, user.getName(), member);
        String messageContent = msg.getContentRaw(); // 채팅 메세지

        if (!userRepository.findUser(chatUser))
            userRepository.saveUser(chatUser);

        //비속어 필터 처리
        if (MesssagegFilter.containsNegativeSpeech(messageContent))
            MesssagegFilter.profanityFilter(messageContent, chatUser, member, tc, g, userRepository);

        //명령어 symbol 체크
        if (messageContent.charAt(0) == '!') {
            String command = messageContent.substring(1);
            FrontConmmand.handleCommand(userRepository, command, chatUser, tc, g);
        }
    }

    private static String readTokenFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine().split("=")[1].trim();
        } catch (IOException e) {
            e.printStackTrace();
            //log.error("Token 읽기 실패!");
            return "Empty";
        }
    }
    public static void showMessage(TextChannel tc, String message) {
        tc.sendMessage(message).queue();
    }

    public static void showBuilderMeesage(TextChannel tc, EmbedBuilder eb) {
        tc.sendMessageEmbeds(eb.build()).queue();
    }


}



