package com.vinta.entity.po;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.quartz.Job;

@Data
@Builder
public class QuartzInfo {
    private String jobGroup;
    private Class<? extends Job> jobClass;
    private String cron;
    @NotNull
    private String jobId;
}
