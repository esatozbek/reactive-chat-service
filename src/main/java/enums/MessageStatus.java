package enums;

public enum MessageStatus {
    SENT("SENT"),
    RECEIVED("SENT"),
    READ("SENT");

    public final String status;

    private MessageStatus(String status) {
        this.status = status;
    }
}
