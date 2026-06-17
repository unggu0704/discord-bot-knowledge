package Bot.valo.client;

/**
 * Henrik API 호출 실패(네트워크 오류, 4xx/5xx, 파싱 실패)를 감싸는 예외.
 * 리스너에서 이 예외를 잡아 사용자에게 친절한 메시지를 보여준다.
 */
public class HenrikApiException extends RuntimeException {
    public HenrikApiException(String message) {
        super(message);
    }

    public HenrikApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
