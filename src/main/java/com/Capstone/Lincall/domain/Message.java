package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    String type;
    long time;
    String emotion;
    String text;
}
