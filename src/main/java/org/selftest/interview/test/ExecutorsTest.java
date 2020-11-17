package org.selftest.interview.test;

import com.alibaba.fastjson.JSON;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author guozhipeng
 * @date 2020/11/9 19:34
 */
public class ExecutorsTest {
    public static void main(String[] args) {
        List list = new ArrayList<>();
        try {
        ThreadPoolTaskExecutor executorService = buildThreadPoolTaskExecutor();
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Future<String> future = executorService.submit(() -> sayHi("submit", finalI));
            futureList.add(future);
        }

        for (int i = 0,len=futureList.size(); i < len; i++) {
            Future future = futureList.get(i);
            list.add(future.get(10, TimeUnit.SECONDS));
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("总输出："+ JSON.toJSONString(list));
    }

    private static String sayHi(String name,int n) throws InterruptedException {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + ":"+n+"】";
        System.out.println(printStr);
        if(n>0 && n % 2 == 0){
            throw new RuntimeException(printStr + ",我异常啦!哈哈哈!");
        }
//        Thread.sleep(1000);
        return name+n;

    }

    private static ThreadPoolTaskExecutor buildThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executorService = new ThreadPoolTaskExecutor();
        executorService.setThreadNamePrefix("666");
        executorService.setCorePoolSize(5);
        executorService.setMaxPoolSize(10);
        executorService.setQueueCapacity(10);
        executorService.setKeepAliveSeconds(30);
        executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executorService.initialize();
        return executorService;
    }
}
