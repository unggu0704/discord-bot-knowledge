
package Bot.chatBot.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Bot.chatBot.Message.Processor.MessageProcessor;
import Bot.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
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
        jda.getPresence().setActivity(Activity.playing("서버를 관리 "));
        jda.addEventListener(new Main());

    }

    Main() {
        userRepository = new UserRepository();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        log.info("로그 : 메세지 감지! ");
        MessageProcessor.processReceivedMessage(userRepository, event);
    }

    private static String readTokenFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine().split("=")[1].trim();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Token 읽기 실패!");
            return "Empty";
        }
    }
}



