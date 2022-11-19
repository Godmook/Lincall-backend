package com.Capstone.Lincall.domain;

import lombok.Getter;

@Getter
public class WebSocketMessage {
    private String type;
    private String sender;
    private int channelId;
    private String data;
}
