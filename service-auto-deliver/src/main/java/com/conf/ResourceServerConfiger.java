package com.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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

        /*
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

         */

        //JWT令牌改造
        resources.resourceId("auto-deliver").tokenStore(tokenStore()).stateless(true);
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


    //创建一个用于存储token对象的tokenstore
    public TokenStore tokenStore(){
//        return new InMemoryTokenStore();

        //使用JWTTokenstore
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    //JWT签名的密钥
    private String signKey="grss123";
    /**
     * 返回一个JWT令牌转换器
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter=new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signKey);     //设置签名密钥
        jwtAccessTokenConverter.setVerifier(new MacSigner(signKey));       //设置校验时使用的密钥，现在使用的是对称加密，所以使用相同的密钥。
        return jwtAccessTokenConverter;
    }
}
