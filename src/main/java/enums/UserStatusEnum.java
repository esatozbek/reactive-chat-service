package enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.Map;

public enum UserStatusEnum {
    ONLINE("ONLINE"),
    OFFLINE("OFFLINE"),
    AWAY("AWAY");

    public final String status;

    UserStatusEnum(String status) {
        this.status  = status;
    }

    private static Map<String, UserStatusEnum> statusMap = new HashMap<>();

    static {
        statusMap.put("ONLINE", ONLINE);
        statusMap.put("OFFLINE", OFFLINE);
        statusMap.put("AWAY", AWAY);
    }

    @JsonCreator
    public static UserStatusEnum forValue(String value) {
        return statusMap.get(value.toUpperCase());
    }
}
