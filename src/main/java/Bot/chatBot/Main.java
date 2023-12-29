
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
    ArrayList<String> player = new ArrayList<>();

    private final UserRepository userRepository;
    boolean game_flag = false;
    int flag = 0;
    int count = 0;
    int game_count = 2;
    int ans[];
    String s;
    int index;

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
            FrontController.handleCommand(command, chatUser, tc, g);
        }
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
//                else if( str.equalsIgnoreCase("발로란트")&& !game_flag) {
//                    tc.sendMessage("Valorant プレイする人！").queue();
//                    tc.sendMessage("@here").queue();
//                    game_flag = true;
//                }
//
//
//                else if (str.equalsIgnoreCase("동원령")) {
//                    tc.sendMessage("@everyone").queue();
//                    game_flag = true;
//                }
//                else if(str.equalsIgnoreCase("참가") && game_flag){
//                    EmbedBuilder eb = new EmbedBuilder();
//                    String player_str = "";
//                    if(player.size() < 5 && !player.contains(user.getName())) {
//                        player.add(user.getName());
//                        tc.sendMessage(user.getName() + "님 참가 ! \n" + " [ 현재 인원 ] \n")
//                                .queue();
//                        for(int i =0 ; i<player.size(); i++) {
//                            player_str += "\n" + player.get(i);
//                        }
//                        eb.setTitle(player_str);
//                        tc.sendMessageEmbeds(eb.build()).queue();
//                        if(player.size() == 4 ) {
//                            tc.sendMessage("주의 4명이서는 플레이 할 수 없습니다.").queue();
//                        }
//                    }
//                    else if (player.size() >= 5){
//                        tc.sendMessage("파티 정원 가득참。").queue();
//                    }
//                    else if(player.contains(user.getName())){
//                        tc.sendMessage("이미 있는 플레이어").queue();
//                    }
//
//
//                }
//
//                else if(str.equalsIgnoreCase("탈퇴")  && game_flag) {
//                    String username = user.getName();
//                    if(player.contains(username)) {
//                        player.remove(player.indexOf(username));
//                    }
//                    tc.sendMessage(username + "님이 나가셨습니다. ").queue();
//                }
//
//                else if(str.equalsIgnoreCase("현재인원")  && game_flag) {
//                    EmbedBuilder eb = new EmbedBuilder();
//                    String player_str = "";
//                    if(player.size() == 0) { tc.sendMessage("현재 인원이 없습니다").queue(); }
//                    else {
//                        tc.sendMessage("[ 현재인원 ]").queue();
//                        for(int i =0 ; i<player.size(); i++) {
//                            player_str += "\n" + player.get(i);
//                        }
//                        eb.setTitle(player_str);
//                        tc.sendMessageEmbeds(eb.build()).queue();
//                    }
//                }
//
//                else if(str.equalsIgnoreCase("해산")  && game_flag && player.contains(user.getName())) {
//                    tc.sendMessage("수고하셨어요! ").queue();
//                    player.clear();
//                    game_flag = false;
//                }
//
//
//                else if(str.equalsIgnoreCase("링크")) {
//
//                    EmbedBuilder eb = new EmbedBuilder();
//                    eb.setTitle( "발로란트 전적 사이트"," https://dak.gg/valorant");
//                    tc.sendMessageEmbeds(eb.build()).queue();
//                    eb.setTitle( "하스스톤 야생 사이트", "https://tempostorm.com/hearthstone");
//                    tc.sendMessageEmbeds(eb.build()).queue();
//                }
//
//                else if(str.equalsIgnoreCase("가이드")) {
//                    EmbedBuilder eb = new EmbedBuilder();
//                    eb.setTitle("바이퍼 셋팅 가이드", "https://docs.google.com/document/u/0/d/1kPHF61JHqZm_caKTQjg_tyocGSuOoCWVt5YpCZRBu5U/mobilebasic"
//                    );
//                    tc.sendMessageEmbeds(eb.build()).queue();
//                    eb.setTitle("스플릿 소바 가이트" , "https://gall.dcinside.com/mgallery/board/view/?id=projecta&no=492318&search_head=30&page=1");
//                    tc.sendMessageEmbeds(eb.build()).queue();
//                }
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


