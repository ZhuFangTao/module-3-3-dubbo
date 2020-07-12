package filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午4:00
 * \
 */
@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER})
public class TransportIPFilter implements Filter {

    private final static String IP = "ip";

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        //如果是客户端则将调用的ip设置进RpcContext
        if (RpcContext.getContext().isConsumerSide()) {
            System.out.println("consume设置ip:" + invocation.getArguments()[0].toString());
            RpcContext.getContext().setAttachment(IP, invocation.getArguments()[0].toString());
        } else {
            Map<String, String> contextAttachments = RpcContext.getContext().getAttachments();
            System.out.println("provider获取到ip：" + contextAttachments.get(IP));
        }
        return invoker.invoke(invocation);
    }
}