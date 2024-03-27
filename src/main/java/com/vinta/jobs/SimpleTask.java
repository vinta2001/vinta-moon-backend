package com.vinta.jobs;

import com.vinta.service.PostInfoService;
import com.vinta.utils.QuartzUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class SimpleTask implements Job {

    @Resource
    private PostInfoService postInfoService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobKey key = jobDetail.getKey();
        String name = key.getName();
        String group = key.getGroup();
        log.info("正在执行id为{}的{}任务", name, group);
        postInfoService.updateStatusById(name);
        Scheduler scheduler = context.getScheduler();
        QuartzUtils.deleteScheduledJob(scheduler, name);
        log.info("已执行完并删除id为{}的{}任务", name, group);
    }
}

