package com.Capstone.Lincall.domain;

import lombok.Getter;

@Getter
public class WebSocketMessage {
    private String roomId;
    private String type;
    private String data;
}
