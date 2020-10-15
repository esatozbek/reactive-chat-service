package enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum MessageStatus {
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    READ("READ");

    public final String status;

    MessageStatus(String status) {
        this.status = status;
    }

    private static Map<String, MessageStatus> statusMap = new HashMap<>();

    static {
        statusMap.put("SENT", SENT);
        statusMap.put("RECEIVED", RECEIVED);
        statusMap.put("READ", READ);
    }

    @JsonCreator
    public static MessageStatus forValue(String value) {
        return statusMap.get(value.toUpperCase());
    }
}
