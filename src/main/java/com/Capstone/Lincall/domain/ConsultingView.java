package com.Capstone.Lincall.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultingView {
    private int id;
    private String counselorName;
    private String clientName;
    private String start;
    private String end;
    private String time;

    public ConsultingView(int id, String counselorName, String clientName, String start, String end, String time) {
        this.id = id;
        this.counselorName = counselorName;
        this.clientName = clientName;
        this.start = start;
        this.end = end;
        this.time = time;
    }
}
