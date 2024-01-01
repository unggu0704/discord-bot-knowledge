
package Bot.chatBot;

import java.util.*;

import Bot.data.UserData;

import Bot.data.UserRepository;
import Bot.features.Command.FrontController;
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

        JDA jda = JDABuilder.createDefault("MTA3NjgwNjIwNzk2MjEwMzgwOA.Gbzcxn.vWrelnDAF_n1Vk1EmyLcKIAH1CUGdxx8ZpKys8")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                .build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("test! "));
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


        if (messageContent.charAt(0) == '!') {
            String command = messageContent.substring(1);
            FrontController.handleCommand(userRepository, command, chatUser, tc, g);
        }
    }

    public static void findUserID(String banUser) {

    }
    public static void showMessage(TextChannel tc, String message) {
        tc.sendMessage(message).queue();
    }

    public static void showBuilderMeesage(TextChannel tc, EmbedBuilder eb) {
        tc.sendMessageEmbeds(eb.build()).queue();
    }


}
            //대답문
//
//                else if(str.equalsIgnoreCase("용준컷")) {
//
//                    int index = yong_Check();
//                    if(index != -1) {
//                        g.ban(ul.get(index).member, 600, TimeUnit.SECONDS).queue();
//                        tc.sendMessage("bye~ bye~ 용준ちゃん~").queue();
//                        ul.remove(index);
//                    }
//                    else {
//                        tc.sendMessage("아직 그가 없거나 아무 말도 하지 않았습니다.").queue();
//                    }
//                }
//
//                else if(str.equalsIgnoreCase("저장")) {
//                    UserDataController.isCSVrite(ul); // 유저리스트 저장
//                    tc.sendMessage("저장중... ").queue();
//                }

//
//
//
//    int yong_Check() {
//        for(int i = 0; i< ul.size(); i++) {
//            if(ul.get(i).getName().equals("AT FULL BLAST")) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//
//
//}


