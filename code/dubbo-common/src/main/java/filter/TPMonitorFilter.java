package filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午5:19
 * \
 */
@Activate(group = {CommonConstants.CONSUMER})
public class TPMonitorFilter implements Filter, Runnable {

    Map<String, List<InvokeRecord>> invokeRecordMap = new ConcurrentHashMap<>();

    public TPMonitorFilter() {
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this, 1, 5, TimeUnit.SECONDS);
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long startTime = System.currentTimeMillis();
        Result invoke = invoker.invoke(invocation);
        long endTime = System.currentTimeMillis();

        //维护调用耗时
        String methodName = invocation.getMethodName();
        InvokeRecord record = new InvokeRecord(new Date(), endTime - startTime);
        if (invokeRecordMap.get(methodName) == null) {
            List<InvokeRecord> l = Collections.synchronizedList(new ArrayList<>());
            l.add(record);
            invokeRecordMap.put(methodName, l);
        } else {
            invokeRecordMap.get(methodName).add(record);
        }
        return invoke;
    }

    public void run() {
        //统计线程
        for (String key : invokeRecordMap.keySet()) {
            List<InvokeRecord> invokeRecords = invokeRecordMap.get(key);
            if (invokeRecords == null) {
                System.out.println("方法：【" + key + "】暂无有效调用记录");
                continue;
            }
            Iterator<InvokeRecord> it = invokeRecords.iterator();
            while (it.hasNext()) {
                InvokeRecord record = it.next();
                if (record.isOutOfDate()) {
                    it.remove();
                    continue;
                }
            }
            if (invokeRecords.isEmpty()) {
                System.out.println("方法：【" + key + "】暂无有效调用记录");
                continue;
            }
            List<Long> costTimeList = new ArrayList<>();
            for (InvokeRecord record : invokeRecords) {
                costTimeList.add(record.getCostTime());
            }
            costTimeList = costTimeList.stream().sorted().collect(Collectors.toList());
            Double v1 = costTimeList.size() * 0.9;
            Double v2 = costTimeList.size() * 0.99;
            //获取Tp90;Tp99
            System.out.println("方法：【" + key + "】------TP90:" + costTimeList.get(v1.intValue()) + "ms------TP99:" + costTimeList.get(v2.intValue()) + "ms");
        }
    }

    class InvokeRecord {

        private Date invokeTime;

        private long costTime;

        public InvokeRecord(Date invokeTime, long costTime) {
            this.invokeTime = invokeTime;
            this.costTime = costTime;
        }

        //是否invokeTime在一分钟内
        private boolean isOutOfDate() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -1);
            return this.invokeTime.before(cal.getTime());
        }

        public Date getInvokeTime() {
            return invokeTime;
        }

        public void setInvokeTime(Date invokeTime) {
            this.invokeTime = invokeTime;
        }

        public long getCostTime() {
            return costTime;
        }

        public void setCostTime(long costTime) {
            this.costTime = costTime;
        }
    }
}