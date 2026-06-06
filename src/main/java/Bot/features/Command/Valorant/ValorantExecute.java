package Bot.features.Command.Valorant;

import Bot.chatBot.Message.Print.Print;
import Bot.data.UserData;
import Bot.data.UserRepository;
import Bot.features.Command.BasicCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ValorantExecute implements BasicCommand {

    private final List<UserData> valorantUserLists = new ArrayList<>();
    private final Guild guild;
    private final TextChannel tc;

    public ValorantExecute(TextChannel tc, Guild guild) {
        this.tc = tc;
        this.guild = guild;
    }

    public boolean isGuild(Guild guild) {
        return this.guild.equals(guild);
    }

    @Override
    public void execute(UserRepository userRepository, MessageReceivedEvent event) {
        Member member = event.getMember();
        UserData userData = new UserData(
                event.getAuthor().getName(),
                member,
                event.getAuthor().getId(),
                event.getAuthor().getDiscriminator(),
                event.getAuthor().getAsMention()
        );
        String command = event.getMessage().getContentRaw().substring(1);

        switch (command) {
            case "참가":
                joinGame(userData);
                break;
            case "동원령":
                forceStartGame();
                break;
            case "나가기":
                exitGame(userData);
                break;
            case "현재인원":
                printUserList();
                break;
        }
    }

    private boolean checkSize() {
        if (valorantUserLists.size() >= 5) {
            Print.showMessage(tc, "5명이 가득찼습니다.");
            return true;
        }
        if (valorantUserLists.isEmpty()) {
            Print.showMessage(tc, "아무도 참가한 사람이 없습니다.");
            return true;
        }
        return false;
    }

    private boolean checkDuplicate(UserData userData) {
        return valorantUserLists.contains(userData);
    }

    private void joinGame(UserData userData) {
        if (checkDuplicate(userData) || checkSize()) return;
        valorantUserLists.add(userData);
        Print.showMessage(tc, userData.getName() + "님 참가 ! \n [ 현재 인원 ]");
        printUserList();
    }

    private void exitGame(UserData userData) {
        if (!checkDuplicate(userData)) {
            Print.showMessage(tc, "팀에 참가되어 있지 않은 유저입니다.");
            return;
        }
        valorantUserLists.remove(userData);
        Print.showMessage(tc, userData.getName() + "님이 나셨습니다. \n [ 현재 인원 ]");
        printUserList();
    }

    private void forceStartGame() {
        Print.showMessage(tc, "@everyone");
    }

    private void printUserList() {
        StringBuilder sb = new StringBuilder();
        for (UserData userData : valorantUserLists) {
            sb.append(userData.getName()).append("\n");
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("현재 인원");
        eb.setDescription(sb.toString());
        Print.showBuilderMessage(tc, eb);
    }
}
