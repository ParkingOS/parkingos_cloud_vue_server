package parkingos.com.bolink.orderserver;

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
 */

public class BaseServer {

    private Logger logger = LoggerFactory.getLogger(BaseServer.class);
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
        List<ServiceInstance> servers = discoveryClient.getInstances(serverId);
        HashMap<String, ServiceInstance> serverMap = new HashMap<String, ServiceInstance>();
        for (ServiceInstance server : servers) {
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
            String hostName = server.getHost();
            int grpcPort = Integer.parseInt(server.getMetadata().get("grpc.port"));
            String key = hostName+":"+grpcPort;
            logger.info("===>>>>>get server:"+key);
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