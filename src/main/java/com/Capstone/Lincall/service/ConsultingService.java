package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Consulting;
import com.Capstone.Lincall.domain.ConsultingView;
import com.Capstone.Lincall.domain.User;
import com.Capstone.Lincall.mapper.ConsultingMapper;
import com.Capstone.Lincall.mapper.MessageMapper;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ConsultingService {
    UserService userService;
    ConsultingMapper consultingMapper;

    MessageMapper messageMapper;

    private String baseUrl;

    public ConsultingService(ConsultingMapper consultingMapper, UserService userService, MessageMapper messageMapper){
        this.consultingMapper = consultingMapper;
        this.userService = userService;
        this.messageMapper = messageMapper;
        ResourceBundle rb = ResourceBundle.getBundle("flaskServer", Locale.KOREA);
        baseUrl = rb.getString("flaskURL");
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

    public int updateConsultingUser(int id, String counselor, String client){
        return consultingMapper.updateUserInfo(id,counselor, client);
    }

    public void endConsulting(String consultingID){
        long end = System.currentTimeMillis();
        consultingMapper.updateEnd(consultingID, end);
    }

    public void startConsulting(String consultingID){
        long start = System.currentTimeMillis();
        consultingMapper.updateStart(consultingID, start);
    }

    public void deleteConsulting(String consultingID){
        consultingMapper.removeConsulting(consultingID);
    }

    public List<ConsultingView> getConsultingsByClient(String id){
        List<Consulting> consultings = consultingMapper.getByClient(id);
        List<ConsultingView> consultingViews = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        for(Consulting c : consultings){
            User counselor = userService.getCounselor(c.getCounselor());
            User client = userService.getClient(c.getClient());
            if(counselor == null || client == null) continue;
            String counselor_name = counselor.getName();
            String client_name = client.getName();
            Date start = new Date(c.getStart());
            Date end = new Date(c.getEnd());
            long diff = c.getEnd() - c.getStart();
            String time = String.format("%02d:%02d:%02d",(diff/(1000 * 60 * 60)) % 24, (diff/(1000 * 60)) % 60, (diff/ 1000) % 60);
            consultingViews.add(new ConsultingView(c.getId(), counselor_name, client_name,sdf.format(start), sdf.format(end), time));
        }
        return consultingViews;
    }

    public List<ConsultingView> getConsultingsByCounselor(String id){
        List<Consulting> consultings = consultingMapper.getByCounselor(id);
        List<ConsultingView> consultingViews = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        for(Consulting c : consultings){
            User counselor = userService.getCounselor(c.getCounselor());
            User client = userService.getClient(c.getClient());
            if(counselor == null || client == null) continue;
            String counselor_name = counselor.getName();
            String client_name = client.getName();
            Date start = new Date(c.getStart());
            Date end = new Date(c.getEnd());
            long diff = c.getEnd() - c.getStart();
            String time = String.format("%02d:%02d:%02d",(diff/(1000 * 60 * 60)) % 24, (diff/(1000 * 60)) % 60, (diff/ 1000) % 60);
            consultingViews.add(new ConsultingView(c.getId(), counselor_name, client_name,sdf.format(start), sdf.format(end), time));
        }
        return consultingViews;
    }

    public String getCounselorInfo(String id){
        JSONObject obj = new JSONObject();

        long monthT = consultingMapper.getSumOfConsultingTimeMonth(id);
        obj.put("month", monthT);

        long dayT = consultingMapper.getSumOfConsultingTimeDay(id);
        obj.put("today", dayT);

        return obj.toString();
    }

    public String getCounselorInfoToday(String id) {
        JSONObject obj = new JSONObject();

        int count = consultingMapper.getCountOfConsultingToday(id);
        obj.put("count", count);

        long dayT = consultingMapper.getSumOfConsultingTimeDay(id);
        obj.put("time", dayT);

        return obj.toString();
    }

    public String getHappyWordcloud(String id){
        String messageStr = messageMapper.getHappyTextByRoomId(id);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(baseUrl + "/wordcloud?sentence="+messageStr, String.class);
    }

    public String getAngryWordcloud(String id){
        String messageStr = messageMapper.getAngryTextByRoomId(id);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(baseUrl + "/wordcloud?sentence="+messageStr, String.class);
    }

}
