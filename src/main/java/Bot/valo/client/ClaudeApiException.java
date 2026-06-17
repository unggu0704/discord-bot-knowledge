package Bot.valo.client;

/**
 * Claude API 호출 실패를 감싸는 예외.
 */
public class ClaudeApiException extends RuntimeException {
    public ClaudeApiException(String message) {
        super(message);
    }

    public ClaudeApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
