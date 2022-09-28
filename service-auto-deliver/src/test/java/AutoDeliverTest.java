import com.AutoDeliverApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes={AutoDeliverApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoDeliverTest {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void testMetadata(){
        List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-RESUME");
        //如果有多个，选择使用一个（选择的过程就是负载均衡)
        for (ServiceInstance instance : instances) {
            System.out.println(instance);
        }
    }
}
