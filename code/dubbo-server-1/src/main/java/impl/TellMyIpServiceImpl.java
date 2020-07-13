package impl;

import org.apache.dubbo.config.annotation.Service;
import service.TellMyIpService;
import service.TellMyIpService1;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午4:24
 * \
 */
@Service(filter = {"filter","-monitorFilter"})
public class TellMyIpServiceImpl implements TellMyIpService1 {

    public String tellMyIp() {
        return "got it,client ip is " ;
    }
}