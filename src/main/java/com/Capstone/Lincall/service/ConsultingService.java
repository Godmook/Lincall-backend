package com.Capstone.Lincall.service;

import com.Capstone.Lincall.domain.Consulting;
import com.Capstone.Lincall.mapper.ConsultingMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ConsultingService {
    ConsultingMapper consultingMapper;

    public ConsultingService(ConsultingMapper consultingMapper){
        this.consultingMapper = consultingMapper;
    }

    public void createConsulting(String counselor, String client){
        Timestamp start = new Timestamp(System.currentTimeMillis());
        Consulting consulting = new Consulting();
        consulting.setCounselor(counselor);
        consulting.setClient(client);
        consulting.setStart(start);
        consulting.setEnd(null);
        consultingMapper.save(consulting);
    }
}
