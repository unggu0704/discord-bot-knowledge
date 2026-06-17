package Bot.valo;

import Bot.valo.embed.ValoEmbedBuilder;
import Bot.valo.model.AccountInfo;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ValoIntelListener extends ListenerAdapter {

    private final ValoCoachService coachService;

    public ValoIntelListener() {
        this(new ValoCoachService());
    }

    public ValoIntelListener(ValoCoachService coachService) {
        this.coachService = coachService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("발로코치")) return;

        String name = event.getOption("닉네임").getAsString();
        String tag  = event.getOption("태그").getAsString();

        log.info("발로코치 요청 - 닉네임: {}, 태그: {}", name, tag);

        // 처리 중 표시 (Discord 3초 타임아웃 방지)
        event.deferReply().queue();

        CompletableFuture.runAsync(() -> {
            try {
                // 유저 존재 확인 (PUUID/region 조회) - 없는 유저면 여기서 예외
                AccountInfo account = coachService.resolveAccount(name, tag);
                String puuid = account.getPuuid();

                // 버튼 ID: "valo:{type}:{puuid}:{name}:{tag}"
                String btnStyle  = "valo:style:"  + puuid + ":" + name + ":" + tag;
                String btnRecent = "valo:recent:" + puuid + ":" + name + ":" + tag;
                String btnMatch  = "valo:match:"  + puuid + ":" + name + ":" + tag;

                EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("🎮 " + name + "#" + tag)
                        .setDescription("오니의 분석이 준비되었어!")
                        .setColor(new Color(0xFF4655));

                event.getHook().sendMessageEmbeds(eb.build())
                        .addActionRow(
                                Button.primary(btnStyle,  "플레이 스타일 분석"),
                                Button.success(btnRecent, "최근 전적 분석"),
                                Button.secondary(btnMatch, "특정 경기 분석")
                        ).queue();

            } catch (Exception e) {
                log.error("발로코치 처리 중 오류", e);
                event.getHook().sendMessage("오류가 발생했습니다. 닉네임과 태그를 확인해주세요.").queue();
            }
        });
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        if (!componentId.startsWith("valo:")) return;

        event.deferReply().queue();

        String[] parts = componentId.split(":");
        // parts[0]=valo, parts[1]=type, parts[2]=puuid, parts[3]=name, parts[4]=tag
        if (parts.length < 5) {
            event.getHook().sendMessage("잘못된 요청입니다.").queue();
            return;
        }

        String type = parts[1];
        String name = parts[3];
        String tag  = parts[4];

        log.info("버튼 클릭 - type: {}, name: {}#{}", type, name, tag);

        CompletableFuture.runAsync(() -> {
            try {
                String analysis;
                String title;
                switch (type) {
                    case "style" -> {
                        title = "🔍 플레이 스타일 분석";
                        analysis = coachService.analyzeStyle(name, tag);
                    }
                    case "recent" -> {
                        title = "📊 최근 전적 분석";
                        analysis = coachService.analyzeRecent(name, tag);
                    }
                    case "match" -> {
                        title = "🎯 특정 경기 분석";
                        analysis = coachService.analyzeMatch(name, tag);
                    }
                    default -> {
                        event.getHook().sendMessage("알 수 없는 요청입니다.").queue();
                        return;
                    }
                }
                event.getHook().sendMessageEmbeds(
                        ValoEmbedBuilder.analysisResult(title, name, tag, analysis)).queue();

            } catch (Exception e) {
                log.error("발로코치 분석 중 오류 - type: {}, name: {}#{}", type, name, tag, e);
                event.getHook().sendMessageEmbeds(
                        ValoEmbedBuilder.error("분석 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")).queue();
            }
        });
    }
}
