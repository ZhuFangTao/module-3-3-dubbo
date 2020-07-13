package util;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/7/13 下午9:07
 * \
 */
public class TheadLocalUtil {

    private static TheadLocalUtil instance = new TheadLocalUtil();

    private TheadLocalUtil() {

    }

    public static TheadLocalUtil getInstance() {
        return instance;
    }

    private final static ThreadLocal<String> _currentIp = new ThreadLocal<>();

    public static String getIp() {
        return _currentIp.get();
    }

    public static void setIp(String ip){
        _currentIp.set(ip);
    }


}