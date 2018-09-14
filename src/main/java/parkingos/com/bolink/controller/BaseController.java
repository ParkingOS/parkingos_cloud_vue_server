package parkingos.com.bolink.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础controller
 * <p>
 * Created by waynelu on 2018/5/23.
 */

public class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancer;
    private Map<String,ManagedChannel> channelMap = new HashMap<String, ManagedChannel>();
    /**
     * 通过负载均衡获取可用的server
     *
     * @param serverId
     * @return
     */
    private ServiceInstance choose(String serverId) {
        logger.error("serverId~~~:"+serverId);
        List<ServiceInstance> servers = discoveryClient.getInstances(serverId);
        logger.error("servers~~~:"+servers);
        HashMap<String, ServiceInstance> serverMap = new HashMap<String, ServiceInstance>();
        for (ServiceInstance server : servers) {
            logger.error("server~~~:"+server);
            String key = server.getHost() + ":" + server.getPort();
            serverMap.put(key, server);
        }
        ServiceInstance rServer = loadBalancer.choose(serverId);
        if(rServer!=null){
            return serverMap.get(rServer.getHost() + ":" + rServer.getPort());
        }
        return null;
    }
    /**
     * 根据 serverId 直接生成 grpc  ManagedChannel
     *
     * @param serverId
     * @return
     */
    protected ManagedChannel createGrpcChannel(String serverId) {
        ServiceInstance server = this.choose(serverId);
        if(server!=null){
            logger.info("ho ,get server success"+server);
            String hostName = server.getHost();
            int grpcPort = Integer.parseInt(server.getMetadata().get("grpc.port"));
            String key = hostName+":"+grpcPort;
            if(channelMap.containsKey(key)){
                return channelMap.get(key);
            }
            ManagedChannel channel = ManagedChannelBuilder.forAddress(hostName, grpcPort).usePlaintext(true).build();
            logger.info("ho ,get channel :"+channel);
            if(channel != null){
                channelMap.put(key,channel);
            }
            return channel;
        }
        return null;
    }
}