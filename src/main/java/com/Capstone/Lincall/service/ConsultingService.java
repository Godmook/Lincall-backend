package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Consulting;
import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.mapper.ConsultingMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConsultingService {
    UserService userService;
    ConsultingMapper consultingMapper;

    public ConsultingService(ConsultingMapper consultingMapper, UserService userService){
        this.consultingMapper = consultingMapper;
        this.userService = userService;
    }

    public int createConsulting(String counselor, String client){
        long start = System.currentTimeMillis();
        Consulting consulting = new Consulting();
        consulting.setCounselor(counselor);
        consulting.setClient(client);
        consulting.setStart(start);
        consulting.setEnd(start);
        consultingMapper.save(consulting);
        return consulting.getId();
    }

    public void endConsulting(String consultingID){
        long end = System.currentTimeMillis();
        consultingMapper.updateEnd(consultingID, end);
    }

    public List<ConsultingView> getConsultingsByClient(String client){
        List<Consulting> consultings = consultingMapper.getByClient(client);
        List<ConsultingView> consultingViews = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        for(Consulting c : consultings){
            String counselor_name = userService.getCounselor(c.getCounselor()).getName();
            String client_name = userService.getClient(c.getClient()).getName();
            Date start = new Date(c.getStart());
            Date end = new Date(c.getEnd());
            long diff = c.getEnd() - c.getStart();
            String time = String.format("%02d:%02d:%02d",(diff/(1000 * 60 * 60)) % 24, (diff/(1000 * 60)) % 60, (diff/ 1000) % 60);
            consultingViews.add(new ConsultingView(c.getId(), counselor_name, client_name,sdf.format(start), sdf.format(end), time));
        }
        return consultingViews;
    }
}
