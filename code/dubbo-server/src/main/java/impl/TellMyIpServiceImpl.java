package impl;

import org.apache.dubbo.config.annotation.Service;
import service.TellMyIpService;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/12 下午4:24
 * \
 */
@Service(filter = {"filter","-monitorFilter"})
public class TellMyIpServiceImpl implements TellMyIpService {

    public String tellMyIp(String ip) {
        return "got it,client ip is " + ip;
    }
}