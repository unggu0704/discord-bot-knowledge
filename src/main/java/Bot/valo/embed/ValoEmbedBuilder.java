package Bot.valo.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.Color;

/**
 * 발로코치 분석 결과를 디스코드 Embed 카드로 만든다.
 */
public class ValoEmbedBuilder {

    private static final Color VALO_RED = new Color(0xFF4655);
    /** Embed description 최대 길이(디스코드 제한 4096). 여유 있게 자른다. */
    private static final int MAX_DESCRIPTION = 4000;

    private ValoEmbedBuilder() {
    }

    /** 분석 결과 카드. */
    public static MessageEmbed analysisResult(String title, String name, String tag, String analysis) {
        return new EmbedBuilder()
                .setTitle(title + " | " + name + "#" + tag)
                .setDescription(truncate(analysis))
                .setColor(VALO_RED)
                .setFooter("VALOINTEL · Claude 분석")
                .build();
    }

    /** 오류 안내 카드. */
    public static MessageEmbed error(String message) {
        return new EmbedBuilder()
                .setTitle("⚠️ 분석 실패")
                .setDescription(message)
                .setColor(VALO_RED)
                .build();
    }

    private static String truncate(String text) {
        if (text == null) return "(빈 응답)";
        if (text.length() <= MAX_DESCRIPTION) return text;
        return text.substring(0, MAX_DESCRIPTION - 3) + "...";
    }
}
