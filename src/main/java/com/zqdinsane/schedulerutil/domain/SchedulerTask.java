package com.zqdinsane.schedulerutil.domain;

import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务控制类
 * @author zhuangqingdian
 * @date 2021/5/14
 */
public class SchedulerTask {
    public volatile ScheduledFuture<?> future;
    //取消任务
    public boolean cancel(){
        if(this.future != null){
            return future.cancel(true);
        }
        return false;
    }

}
