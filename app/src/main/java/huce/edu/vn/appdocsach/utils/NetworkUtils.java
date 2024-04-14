package huce.edu.vn.appdocsach.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class NetworkUtils {
    public static String getLocalHostIp() {
//        Executor executor = Executors.newSingleThreadExecutor();
//        AtomicReference<String> response = new AtomicReference<>();
//        executor.execute(() -> {
//            try {
//                response.set(InetAddress.getLocalHost().getHostAddress());
//            } catch (UnknownHostException e) {
//                LoggerUtil.getInstance().error(e);
//                response.set("Network Problem");
//            }
//        });
//        return response.get();
                return "192.168.1.3";
//        return "192.168.1.13";
    }
}
