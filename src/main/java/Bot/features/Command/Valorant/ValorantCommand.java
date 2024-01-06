package Bot.features.Command.Valorant;

import Bot.chatBot.Main;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class ValorantCommand implements BasicCommand {
    private final List<UserData> valorantUserLists = new ArrayList<>();

    private Guild guild;
    private TextChannel tc;
    public void execute(UserRepository userRepository, UserData user, TextChannel tc, Guild guild, String command) {
        Main.showMessage(tc, "발로란트 하실분 @here");
        this.guild = guild;
        this.tc = tc;

        switch (command) {
            case "참가":
                joinGame(user);
                break;
            case "동원령":
                forceStartGame();
                break;
            case "나가기":
                exitGame(user);
                break;
            case "현재인원":
                printUserList();
                break;
            case "해산":
                Main.showMessage(tc, "수고하셨습니다.");
                break;
        }
    }

    public  boolean checkSize() {
        if (valorantUserLists.size() >=5) {
            Main.showMessage(tc, "5명이 가득핬습니다.");
            return true;
        }

        if (valorantUserLists.isEmpty()) {
            Main.showMessage(tc, "아무도 참가한 사람이 없습니다.");
            return true;
        }
        return false;
    }

    public  boolean checkDupicate(UserData userData) {
        return valorantUserLists.contains(userData);
    }
    public  void joinGame(UserData userData) {
        if (checkDupicate(userData) || checkSize())
            return;

        valorantUserLists.add(userData);
        Main.showMessage(tc, userData.getUser().getName() + "님 참가 ! \n" + " [ 현재 인원 ] \n");
        printUserList();
    }

    public  void exitGame(UserData userData) {
        if (checkDupicate(userData)) {
            valorantUserLists.remove(userData);
            Main.showMessage(tc, userData.getUser().getName() + "님 이 나셨습니다. ! \n" + " [ 현재 인원 ] \n");
            printUserList();
        }

        Main.showMessage(tc, "팀에 참가 되어 있지 않은 유저입니다.");
    }


    public  void forceStartGame() {
        Main.showMessage(tc, "@everyone");
    }

    public  void printUserList() {
        StringBuilder sb = new StringBuilder();

        for (UserData userData : valorantUserLists) {
            sb.append(userData.getName()).append("\n");
        }

        Main.showBuilderMeesage(tc, new EmbedBuilder().setTitle(sb.toString()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;
        ValorantCommand that = (ValorantCommand) obj;
        return this.guild.equals(that.guild);
    }


}
