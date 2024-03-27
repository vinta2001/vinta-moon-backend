package com.vinta.controller;


import com.vinta.entity.po.QuartzInfo;
import com.vinta.jobs.SimpleTask;
import com.vinta.jobs.TestTask;
import com.vinta.utils.QuartzUtils;
import jakarta.annotation.Resource;
import org.quartz.Scheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class TestController {

    @Resource
    private Scheduler scheduler;

    @GetMapping("/createJob")
    public String createJob(){
        QuartzInfo quartz = QuartzInfo.builder()
                .jobGroup("test")
                .jobId("1")
                .jobClass(TestTask.class)
                .cron("0/2 * * * * ?")
                .build();

        QuartzUtils.createScheduledJob(scheduler, quartz);
        return "success";
    }
}
