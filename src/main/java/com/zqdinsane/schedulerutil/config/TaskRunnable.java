package com.zqdinsane.schedulerutil.config;

import com.zqdinsane.schedulerutil.domain.Constant;
import com.zqdinsane.schedulerutil.utils.OkHttpCli;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import java.time.LocalDateTime;


/**
 * 任务运行类-通过url执行
 * @author zhuangqingdian
 * @date 2021/5/14
 */
@Slf4j
public class TaskRunnable implements Runnable{

    //任务id
    private String id;
    //任务地址
    private String execUrl;

    public TaskRunnable(String id, String execUrl) {
        this.id = id;
        this.execUrl = execUrl;
    }

    @Override
    public void run() {
        log.info("任务{}开始执行，执行地址:{}",id,execUrl);
        String status = "执行成功";
        long startTime = System.currentTimeMillis();
        try {
            OkHttpCli okHttpCli = new OkHttpCli(new OkHttpClient());
            okHttpCli.doGet(execUrl);
        } catch (Exception e) {
            log.error("任务地址{}调用异常：{}",execUrl,e.getMessage());
            status = "执行失败:"+e.getMessage();
        }
        long times = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - id：{}, 耗时：{} 毫秒", id,times);
        String lastExecTime = Constant.FORMAT_TIME.format(LocalDateTime.now());
        //todo 更新任务状态 id,上次执行状态,上次执行时间
    }
}
