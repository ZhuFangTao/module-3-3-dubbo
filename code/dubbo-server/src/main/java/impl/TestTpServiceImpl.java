package impl;

import org.apache.dubbo.config.annotation.Service;
import service.TestTpService;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午5:24
 * \
 */
@Service(filter = {"-filter"})
public class TestTpServiceImpl implements TestTpService {
    private static int invokeTimes = 0;

    private ReentrantLock lock = new ReentrantLock(true);

    public int methodA() {
        return sleep();
    }

    public int methodB() {
        return sleep();
    }

    public int methodC() {
        return sleep();
    }

    private int sleep() {
        Random r = new Random();
        int t = r.nextInt(100);
        try {
            Thread.sleep(t);
            lock.lock();
            invokeTimes++;
            return invokeTimes;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        } finally {
            lock.unlock();
        }
    }
}