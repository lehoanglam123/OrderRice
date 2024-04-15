package com.bezkoder.spring.datajpa.model;

import com.bezkoder.spring.datajpa.repository.CategoryRepository;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class TimerThing {

    private static final Logger log = LoggerFactory.getLogger(TimerThing.class);

    @Autowired
    private CategoryRepository categoryRepository;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "0 0 16 * * *")
    public void reportCurrentTime() {
       log.info("The time is now {}", dateFormat.format(new Date()));
       categoryRepository.updateAllCategory();
    }
}