package com.zqdinsane.schedulerutil.config;

import com.zqdinsane.schedulerutil.domain.ResultInfo;
import com.zqdinsane.schedulerutil.domain.SchedulerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务管理类，增删改查
 * @author zhuangqingdian
 * @date 2021/5/14
 */
@Component
@Slf4j
public class TaskManager implements DisposableBean {

    //存储任务map
    private final Map<String, SchedulerTask> schedulerTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 添加任务
     * @param task 任务
     * @param cronExpression cron表达式
     * @param id 任务id
     * @return
     */
    public ResultInfo addTask(Runnable task, String cronExpression, String id){
        log.info("添加id为{}的任务",id);
        try {
            addInTask(new CronTask(task,cronExpression),id);
        } catch (Exception e) {
            return new ResultInfo("error","添加失败："+e.getMessage());
        }
        return new ResultInfo("success","添加成功");
    }

    private void addInTask(CronTask cronTask, String id) {
        if(cronTask != null){
            if(schedulerTaskMap.containsKey(id)){
                //已存在该任务
                removeTask(id);
            }
            this.schedulerTaskMap.put(id,createTask(cronTask));
        }
    }

    //移除定时任务
    private boolean removeTask(String id) {
        SchedulerTask schedulerTask = this.schedulerTaskMap.remove(id);
        if(schedulerTask != null){
            return schedulerTask.cancel();
        }
        return false;
    }

    private SchedulerTask createTask(CronTask cronTask) {
        SchedulerTask schedulerTask = new SchedulerTask();
        //执行
        schedulerTask.future = this.taskScheduler.schedule(cronTask.getRunnable(),cronTask.getTrigger());
        return schedulerTask;
    }

    //查看任务
    public SchedulerTask getById(String id){
        return this.schedulerTaskMap.get(id);
    }
    public Map<String,SchedulerTask> listAll(){
        return schedulerTaskMap;
    }

    //bean销毁时关闭所有任务
    @Override
    public void destroy() throws Exception {
        for (SchedulerTask s: schedulerTaskMap.values()) {
            try {
                s.cancel();
            } catch (Exception e) {
               log.error("定时任务{}关闭失败",s.toString());
            }
        }
        this.schedulerTaskMap.clear();
    }
}
