package com.vinta.utils;


import com.vinta.entity.po.QuartzInfo;
import org.quartz.*;

import java.util.Date;

public class QuartzUtils {

    /**
     * 创建一个定时任务
     *
     * @param scheduler
     * @param quartzInfo
     */
    public static void createScheduledJob(Scheduler scheduler, QuartzInfo quartzInfo) {
        Class<? extends Job> jobClass = quartzInfo.getJobClass();
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(quartzInfo.getJobId(), quartzInfo.getJobGroup())
                .build();

        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(quartzInfo.getCron());

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(quartzInfo.getJobId())
                .withSchedule(cronSchedule)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取消定时任务
     *
     * @param scheduler
     * @param jobId
     */
    public static void pauseScheduledJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复定时任务
     *
     * @param scheduler
     * @param jobId
     */
    public static void resumeScheduledJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteScheduledJob(Scheduler scheduler, String jobId) {
        JobKey jobKey = JobKey.jobKey(jobId);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateScheduledJob(Scheduler scheduler, QuartzInfo quartzInfo) {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzInfo.getJobId());
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(quartzInfo.getCron());

        try {
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            trigger = trigger.getTriggerBuilder()
                    .withSchedule(cronSchedule)
                    .withIdentity(triggerKey)
                    .build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}
