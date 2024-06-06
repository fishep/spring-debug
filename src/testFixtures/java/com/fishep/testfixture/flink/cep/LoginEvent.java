package com.fishep.testfixture.flink.cep;

/**
 * @Author fly.fei
 * @Date 2024/6/6 18:07
 * @Desc
 **/
public class LoginEvent {

    public String userId;

    public String ipAddress;

    public String eventType;

    public Long eventTime;

    public LoginEvent() {
    }

    public LoginEvent(String userId, String ipAddress, String eventType, Long eventTime) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "LoginEvent{" +
            "userId='" + userId + '\'' +
            ", ipAddress='" + ipAddress + '\'' +
            ", eventType='" + eventType + '\'' +
            ", eventTime=" + eventTime +
            '}';
    }

}
