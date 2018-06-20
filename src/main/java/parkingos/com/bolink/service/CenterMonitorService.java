package parkingos.com.bolink.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface CenterMonitorService {


    public String getCenterMonitor(Long comid, Long groupid);

    byte[]  getConfirmPic(String event_id, long comid, String car_number);

    void matchConfirmPic(String event_id, long comid, String car_number,HttpServletRequest request,HttpServletResponse response);

    Map getSelectOrder(String comid, String carNumber);

    void sendMessageToPark(String orderId, String carNumber, String channel_id, String event_id, String comid, HttpServletResponse response) throws Exception;

    void liftRodMessage(String channel_id, String channel_name, String comid, HttpServletResponse response) throws Exception;

    void callInform(Integer fromOther,String callerid_num, String exten,HttpServletResponse response);

    Map qryChannelByMonitId(Long monitor_id);
}
