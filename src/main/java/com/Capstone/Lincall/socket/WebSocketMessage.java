package com.Capstone.Lincall.socket;

import lombok.Getter;

@Getter
public class WebSocketMessage {
    private String type;
    private String sender;
    private int channelId;
    private Object data;
}
