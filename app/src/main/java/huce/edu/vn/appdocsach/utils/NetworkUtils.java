package huce.edu.vn.appdocsach.utils;


public class NetworkUtils {
    public static String getLocalHostIp() {
//        try {
//            AppLogger.getInstance().info("Ip : ", InetAddress.getLocalHost().getHostAddress());
//        } catch (UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
//        Code này throw NetworkOnMainThreadException

        return "192.168.1.3";
//        return "192.168.1.13";
//        return "192.168.181.144";
//        return "172.20.10.3";
    }
}
