
package Bot.chatBot;

import java.io.BufferedReader;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;



public class Main extends ListenerAdapter
{
    ArrayList<String> player = new ArrayList<>();
    List<user_info> ul = new ArrayList<user_info>();
    boolean game_flag = false;
    int flag = 0; int count = 0; int game_count =2; int ans[]; String s; int index;

    public static void main(String[] args)
    {

        JDA jda = JDABuilder.createDefault("MTA3NjgwNjIwNzk2MjEwMzgwOA.G_q9ob.wCxvJGhfx4tv81LbwIKMGioIQkArY-45OElUdk")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // enables explicit access to message.getContentDisplay()
                .build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("test! "));
        jda.addEventListener(new Main());

    }

    Main(){
        isCSVload();
        printUserInfo();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

        User user = event.getAuthor();
        TextChannel tc = (TextChannel) event.getChannel();
        Message msg = event.getMessage();
        Guild g = event.getGuild();
        Member member = event.getMessage().getMember();
        user_info ab = new user_info(user, user.getName(), member);


        if(!ul.contains(ab)) {
            ul.add(ab);
        }
        //분기문
        //숫자 게임
        int answer =0;
        if(flag == 1) {
            System.out.println("분기 짐");

            StringTokenizer st = new StringTokenizer(msg.getContentRaw());

            if(st.nextToken().equalsIgnoreCase("정답")) {
                String str = st.nextToken();
                if(!str.equals("-1")) {
                    int[] digits = Stream.of(str.split("")).mapToInt(Integer::parseInt).toArray();
                    Integer[] digits2 = Arrays.stream(digits).boxed().toArray(Integer[]::new);
                    Arrays.sort(digits2, Collections.reverseOrder());
                    str = Arrays.toString(digits2);
                    System.out.println("제발: " + str);
                }
                if(str.compareTo(s) == 0) {
                    tc.sendMessage("맞았습니다!").queue();
                    flag = 0; count = 0; game_count = 2;
                }
                else{
                    if( game_count == 0) {
                        tc.sendMessage("틀렸습니다. 정답은: " + s + "였습니다.").queue();
                        flag = 0; count = 0; game_count = 2;
                    }
                    else {
                        tc.sendMessage("틀렸습니다.").queue();
                        game_count--;
                    }
                }
            }
        }

        else {
            //대답문

            if(msg.getContentRaw().contains("씹") || msg.getContentRaw().contains("개년") || msg.getContentRaw().contains("섹")
                    ||msg.getContentRaw().contains("창녀") || msg.getContentRaw().contains("보지먹") || msg.getContentRaw().contains("따먹")
                    ||msg.getContentRaw().contains("구멍")
            ) {
                tc.sendMessage("부정적일 발언이 감지 되었습니다. 지속적으로 사용시 재제됨을 알려드립니다."	).queue();
                for(int i =0; i< ul.size(); i++) {
                    if(ul.get(i).getName().equals(user.getName())){
                        if((ul.get(i)).check_user() > 3) {
                            tc.sendMessage(user.getAsMention() + " 焼却完了！").queue();
                            Member target = event.getMessage().getMember();
                            g.ban(target, 60, TimeUnit.SECONDS).queue();
                        }
                        else {
                            tc.sendMessage(user.getAsMention()+ "   (" + ul.get(i).check + "/3)").queue();
                        }
                    }
                }
            }

            // ! 명령어
            StringTokenizer st = new StringTokenizer(msg.getContentRaw());
            System.out.println(flag);

            if(st.nextToken().equalsIgnoreCase("!")) {
                String str = st.nextToken();

                if(str.equalsIgnoreCase("도움")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("! 도움1 : 발로란트 관련 명령어 \n"
                            + "! 도움2: 정보 명령어 \n"
                            + "! 도움3: 기타 명령어 "
                    );
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("도움1")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("! 발로란트 : 발로란트 파티 리스트를 엽니다. (온라인에게 알람이 갑니다.) \n"
                            + "! 동원령 : 모든 플레이어들에게 알람이 갑니다. \n"
                            + "! 참가: 발로란트 리스트에 본인을 추가합니다. \n"
                            + "! 탈퇴: 발로란트 리스트에 본인을 제거합니다. \n"
                            + "! 현재인원: 현재 리스트를 봅니다. \n"
                            + "! 해산: 발로란트 파티 리스트를 제거합니다. \n"
                            + "! 가이드: 발로란트 관련 가이드를 봅니다."
                    );
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("도움2")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle( "! 자기소개: 자기소개를 듣습니다.\n"
                            + "! 링크: 각종 링크를 얻습니다. "
                    );
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("도움3")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(" ! 용준컷: 용준이를 죽입니다. "
                    );
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if( str.equalsIgnoreCase("발로란트")&& !game_flag) {
                    tc.sendMessage("Valorant プレイする人！").queue();
                    tc.sendMessage("@here").queue();
                    game_flag = true;
                }


                else if (str.equalsIgnoreCase("동원령")) {
                    tc.sendMessage("@everyone").queue();
                    game_flag = true;
                }
                else if(str.equalsIgnoreCase("참가") && game_flag){
                    EmbedBuilder eb = new EmbedBuilder();
                    String player_str = "";
                    if(player.size() < 5 && !player.contains(user.getName())) {
                        player.add(user.getName());
                        tc.sendMessage(user.getName() + "님 참가 ! \n" + " [ 현재 인원 ] \n")
                                .queue();
                        for(int i =0 ; i<player.size(); i++) {
                            player_str += "\n" + player.get(i);
                        }
                        eb.setTitle(player_str);
                        tc.sendMessageEmbeds(eb.build()).queue();
                        if(player.size() == 4 ) {
                            tc.sendMessage("주의 4명이서는 플레이 할 수 없습니다.").queue();
                        }
                    }
                    else if (player.size() >= 5){
                        tc.sendMessage("파티 정원 가득참。").queue();
                    }
                    else if(player.contains(user.getName())){
                        tc.sendMessage("이미 있는 플레이어").queue();
                    }


                }

                else if(str.equalsIgnoreCase("탈퇴")  && game_flag) {
                    String username = user.getName();
                    if(player.contains(username)) {
                        player.remove(player.indexOf(username));
                    }
                    tc.sendMessage(username + "님이 나가셨습니다. ").queue();
                }

                else if(str.equalsIgnoreCase("현재인원")  && game_flag) {
                    EmbedBuilder eb = new EmbedBuilder();
                    String player_str = "";
                    if(player.size() == 0) { tc.sendMessage("현재 인원이 없습니다").queue(); }
                    else {
                        tc.sendMessage("[ 현재인원 ]").queue();
                        for(int i =0 ; i<player.size(); i++) {
                            player_str += "\n" + player.get(i);
                        }
                        eb.setTitle(player_str);
                        tc.sendMessageEmbeds(eb.build()).queue();
                    }
                }

                else if(str.equalsIgnoreCase("해산")  && game_flag && player.contains(user.getName())) {
                    tc.sendMessage("수고하셨어요! ").queue();
                    player.clear();
                    game_flag = false;
                }


                else if(str.equalsIgnoreCase("링크")) {

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle( "발로란트 전적 사이트"," https://dak.gg/valorant");
                    tc.sendMessageEmbeds(eb.build()).queue();
                    eb.setTitle( "하스스톤 야생 사이트", "https://tempostorm.com/hearthstone");
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("가이드")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("바이퍼 셋팅 가이드", "https://docs.google.com/document/u/0/d/1kPHF61JHqZm_caKTQjg_tyocGSuOoCWVt5YpCZRBu5U/mobilebasic"
                    );
                    tc.sendMessageEmbeds(eb.build()).queue();
                    eb.setTitle("스플릿 소바 가이트" , "https://gall.dcinside.com/mgallery/board/view/?id=projecta&no=492318&search_head=30&page=1");
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("용준컷")) {

                    int index = yong_Check();
                    if(index != -1) {
                        g.ban(ul.get(index).member, 600, TimeUnit.SECONDS).queue();
                        tc.sendMessage("bye~ bye~ 용준ちゃん~").queue();
                        ul.remove(index);
                    }
                    else {
                        tc.sendMessage("아직 그가 없거나 아무 말도 하지 않았습니다.").queue();
                    }
                }
                else if(str.equalsIgnoreCase("숫자놀이도움")) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("시키는 갑자기 숫자 하나를 보고 숫자의 특징에 대해 생각했다.\n "
                            + "숫자 10은 2*5로 나타낼 수 있고 25의 각 자릿수의 곱셈은 10이라는 것이다.\n"
                            + "양의 정수 N( 1 <= N <= 500)이 있을 때 모든 자릿수의 곱이 정확히 N이 되는 X는 몇자리일까?.\n"
                            + "자릿수의 곱이 20인 수들을 예를 들면, 522 보다는 225(3)가 작고 225 보다는 45(2)가 작다 즉 답은 2이다.\n"
                            + "시키가 내는 숫자의 가장 작은 자릿수를 맞추어보자. 없을시에는 -1");
                    tc.sendMessageEmbeds(eb.build()).queue();
                }

                else if(str.equalsIgnoreCase("숫자놀이")) {
                    flag = 1;
                    int random = random_int();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle(" [" + random + "] 를 만들기 위한 가장 낮은 자릿수는? ");
                    tc.sendMessageEmbeds(eb.build()).queue();
                    ans = new int[5];
                    for(int a: ans) {
                        ans[a] = 0;
                    }
                    index = 0;
                    numberPlay(random);
                    System.out.println(Arrays.toString(ans));

                    System.out.println(count);
                    for(int i =0; i<ans.length; i++) {
                        if(count ==4 ) {	answer += ans[i]*1000;	count--;	}
                        else if(count ==3) {		answer += ans[i]*100;	count--;	}
                        else if(count ==2) {		answer += ans[i]*10;	count--;	}
                        else if(count ==1) {		answer += ans[i];	count--;	}
                        else if(count ==0) {	break;	}
                        else if(count == -1) { answer = -1; break;}
                    }
                    s = Integer.toString(answer);
                    System.out.println("answer: " + answer );

                }

                else if(str.equalsIgnoreCase("저장")) {
                    isCSVrite(); // 유저리스트 저장
                    tc.sendMessage("저장중... ").queue();
                }

            }
        }


    }
    int yong_Check() {
        for(int i = 0; i< ul.size(); i++) {
            if(ul.get(i).getName().equals("AT FULL BLAST")) {
                return i;
            }
        }
        return -1;
    }

    int random_int() {
        int min = 1;
        int max = 100;
        int random = (int) ((Math.random() * (max - min) ) + min);
        if(random % 2 == 1) {
            random += 1;
        }
        return random;
    }

    int numberPlay(int input) {
        System.out.print("현재 input: " + input);
        int tmp = input;
        if(input < 10 ) { count++; ans[index++] = input;return 1;}
        for(int i = 9; i != 1; i--) {
            //	System.out.println(i);
            if(((input % i) == 0) ) {
                System.out.println(index + "번째 인덱스에 " + i + " 값 추가 ");
                count++;
                input = input / i;
                ans[index++] = i;
                numberPlay(input);
                break;
            }
        }
        if(input == tmp ) {
            count = -1;
            return count;
        }
        else {
            return count;
        }
    }


    public void isCSVrite() {
        String csvFilePath = "user_info.csv";

        try (FileWriter writer = new FileWriter(csvFilePath)) {

            for (user_info userInfo : ul) {
                System.out.println("유저 저장 완료!" + userInfo.getName());
                writer.write(userInfo.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void isCSVload() {

        String csvFilePath = "user_info.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String name;
            reader.readLine();
            while ((name = reader.readLine()) != null) {

                user_info userInfo = new user_info(name);
                ul.add(userInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printUserInfo() {
        System.out.println("유저 정보 ");
        for(user_info a : ul) {
            System.out.println(a.getName());
        }
    }

}


class user_info {
    User u;
    Member member;
    int check = 0;
    public String name;

    user_info(User u, String name, Member m){
        this.name= name;
        this.u = u;
        this.member = m;
    }
    user_info(String name){
        this.name= name;
    }
    int check_user() {
        check++;
        return check;
    }

    String getName() {
        return name;
    }

    User Getuser() {
        return u;
    }

}