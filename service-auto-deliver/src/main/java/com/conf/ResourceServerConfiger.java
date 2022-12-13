package com.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableWebSecurity      //开启web访问安全
@EnableResourceServer   //开启资源服务器功能
public class ResourceServerConfiger  extends ResourceServerConfigurerAdapter {

    /**
     * 此方法用来向远程认证服务器发起请求，用来资源校验
     * @param resources
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.resourceId("auto-deliver");
        //定义一个token对象
        RemoteTokenServices remoteTokenServices=new RemoteTokenServices();
        //校验端点/接口设置
        remoteTokenServices.setCheckTokenEndpointUrl("Http://localhost:9999/oauth/check_token");
        //客户端id和客户端安全码
        remoteTokenServices.setClientId("grss");
        //这个需要看具体的请求是怎么定义的
        remoteTokenServices.setClientSecret("123356");

        resources.tokenServices(remoteTokenServices);
    }

    /**
     * 不需要认证的方法在的这里处理
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http    //设置session的创建策略（根据需要创建）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()//什么样的请求需要认证
                .antMatchers("autodeliver/**").authenticated()
                .antMatchers("/demo/**").authenticated()
                .anyRequest().permitAll();//其他请求不需要认证
    }
}
