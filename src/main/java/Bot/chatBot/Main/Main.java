package Bot.chatBot.Main;

import Bot.data.UserRepository;
import Bot.valo.ValoIntelListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        log.info("봇이 실행됨!");

        String token = System.getenv("DISCORD_TOKEN");
        if (token == null || token.isEmpty()) {
            log.info("환경변수를 읽지 못함, 파일에서 읽기 시도");
            token = readTokenFromFile("Token.txt");
        }
        if (token.equals("Empty")) {
            throw new RuntimeException("[ERROR] | 토큰을 읽는데 실패함!");
        }

        UserRepository userRepository = new UserRepository();

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .build();

        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("서버를 관리"));

        jda.awaitReady();

        // 슬래시 커맨드 등록
        jda.upsertCommand(
                Commands.slash("발로코치", "발로란트 전적 분석")
                        .addOption(OptionType.STRING, "닉네임", "닉네임", true)
                        .addOption(OptionType.STRING, "태그", "태그 (예: KR1)", true)
        ).queue();

        jda.addEventListener(new MessageListener(userRepository));
        jda.addEventListener(new ValoIntelListener());
    }

    private static String readTokenFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine().split("=")[1].trim();
        } catch (IOException e) {
            log.error("Token 읽기 실패!");
            return "Empty";
        }
    }
}
