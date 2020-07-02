package org.selftest.interview.multithreading.list;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Java多线程处理List数据
 *
 * 第一种实例：如何让n个线程顺序遍历含有n个元素的List集合
 *
 * 适用场景：excel导入的大批量数据处理
 *
 */
@Slf4j
public class HandleList1Application {

    public static void main(String[] args) throws InterruptedException {
        // 准备测试数据
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            data.add("item" + i);
        }

        long startTime = System.currentTimeMillis();

        //不走多线程处理
//        data.stream().forEach(i -> {
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        long endTime = System.currentTimeMillis();
//        System.out.println("=============主线程执行完毕!================"+(endTime-startTime));

        //设置需要开启的线程数(多线程数量要等于机器CPU核数-1)
        int threadNum = Runtime.getRuntime().availableProcessors()-1;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);//CountDownLatch实现使用一个计数器，而参数cout就是初始化计数器的值，该值一经初始化就不能再被修改。
        handleList(data, countDownLatch, threadNum);
        countDownLatch.await();// 调用await方法阻塞当前线程，等待子线程完成后在继续执行

        long endTime2 = System.currentTimeMillis();
        System.out.println("=============主线程执行完毕!================"+(endTime2-startTime));


    }

    /**
     * 多线程处理list
     * 这里我改造成了静态方法
     *
     * @param data           数据list
     * @param countDownLatch 协调多个线程之间的同步
     * @param threadNum      开启的线程数:也可以使用countDownLatch.getCount();//来获取开启的线程数但是要注意这个会是一个风险。因为这是一个可变的数。而控制他改变的罪魁祸首就是countDownLatch.countDown();
     */
    public static synchronized void handleList(List<String> data,
                                               CountDownLatch countDownLatch, int threadNum) {
        int length = data.size();//获取数据的总数
        int tl = length % threadNum == 0 ? length / threadNum : (length
                / threadNum + 1);//计算每个线程平均处理的数据
        for (int i = 0; i < threadNum; i++) {
            int end = (i + 1) * tl;//获得每个线程的最后下标(避免有余数导致数据错误所以前面的线程下标+1)
            HandleThread thread = new HandleThread("线程[" + (i + 1) + "] ",
                    data, i * tl, end > length ? length : end, countDownLatch);//最后一个线程拿到的是剩余的数据
            thread.start();//开启线程运行run方法进行数据处理
        }
    }

    static class HandleThread extends Thread {
        private String threadName; //线程名称
        private List<String> data; //该线程负责的数据
        private int start;         //开始集合的下标
        private int end;           //结束集合的下标
        private CountDownLatch countDownLatch; //协调多个线程之间的同步

        /**
         * 无参构造函数
         */
        public HandleThread() {
            super();
        }

        /**
         * 带参构造方法
         *
         * @param threadName     当前线程名称
         * @param data           数据
         * @param start          开始的下标
         * @param end            结束的下标
         * @param countDownLatch 协调多个线程之间的同步
         */
        public HandleThread(String threadName, List<String> data, int start,
                            int end, CountDownLatch countDownLatch) {
            this.threadName = threadName;
            this.data = data;
            this.start = start;
            this.end = end;
            this.countDownLatch = countDownLatch;
        }

        /**
         * 重写Thread的run方法  调用start方法之后自动调用run方法
         */
        @Override
        public void run() {
            // 这里处理数据
            List<String> subList = data.subList(start, end);//获取当前线程需要处理的数据
            for (int a = 0; a < subList.size(); a++) {
//                System.out.println(threadName + "处理了   " + subList.get(a) +
//                        "  ！");
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            System.out.println(threadName + "处理了 " + subList.size() + "条数据！");
            // 执行子任务完毕之后，countDown减少一个点
            countDownLatch.countDown();
        }

    }

}

