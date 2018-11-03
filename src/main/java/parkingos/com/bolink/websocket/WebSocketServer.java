package parkingos.com.bolink.websocket;

import com.danga.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import parkingos.com.bolink.utils.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket")
@Controller
public class WebSocketServer {

    @Autowired
    private MemCachedClient memcachedClient;

    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("sessionID===>>"+session.getId());
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("",session.getId());
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        log.info("来自客户端的消息:" + session.getId());


        if(!"123456789".equals(message)) {
            //每次刷新或者开启页面都把这个存在memcache 里面
            memcachedClient = new MemCachedClient();
            if (memcachedClient.get(message) == null) {
                boolean flag = memcachedClient.set(message, session.getId(), new Date(60 * 30 * 1000));
            } else {
                String value = memcachedClient.get(message) + "";
                String[] strArr = value.split(",");
                boolean b = false;
                for (String s : strArr) {
                    if(StringUtils.compareStr(session.getId(),s)<=0){
                        b = true;
                        break;
                    }
//                    if (s.equals(session.getId())) {
//                        b = true;
//                        break;
//                    }
                }
                if (b) {
                    memcachedClient.set(message, session.getId());
                } else {
                    value = value + "," + session.getId();
                    memcachedClient.set(message, value);
                }
            }
        }
        for (WebSocketServer item : webSocketSet) {
            try {
                if (item.session.getId().equals(session.getId())) {
                    item.sendMessage(message,session.getId());
                }else{
                    continue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info(memcachedClient.get(message)+"");
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message,String id) throws IOException {
        for (WebSocketServer item : webSocketSet) {
            if(item.session.getId().equals(id)) {
                log.info("====>>>>>send"+id);
                try {
                    item.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                continue;
            }
        }
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        log.info(message+"");
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message,item.session.getId());
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
