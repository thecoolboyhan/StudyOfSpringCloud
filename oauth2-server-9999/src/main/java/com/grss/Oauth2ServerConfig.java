package com.grss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Oauth2server的配置类
 */
@Configuration
//开启认证服务器功能
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    //客户端详情配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        clients.inMemory()
                .withClient("grss")//名字
                .secret("123456")//密码
                .resourceIds("autodeliver")//客户端需要访问的资源清单
                .authorizedGrantTypes("password", "refresh_token")//认证模式
                .scopes("all");//权限范围。
    }

    //认证服务器最终以API的形式对外提供接口（认证合法性并生成令牌，校验合法性等）
    //这里配置接口的访问权限配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        security.allowFormAuthenticationForClients()//允许表单验证
                .tokenKeyAccess("permitAll()")//定期端口获取tokenkey，现在是允许所有
                .checkTokenAccess("permitAll()");//开启验证端口

    }


    //表示token相关的配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints.tokenStore(tokenStore())//指定token的存储方式
                .tokenServices(authorizationServerTokenServices())//token服务的描述，可以认为是token 的细节描述，比如有效时间等
                .authenticationManager(authenticationManager)//指定认证管理器，注入一个就可以用
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);//配置请求的方式

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

    //返回一个token服务对象，描述了token 的有效期等对象
    public AuthorizationServerTokenServices authorizationServerTokenServices(){
        //AuthorizationServerTokenServices的默认实现
        DefaultTokenServices defaultTokenServices=new DefaultTokenServices();
        defaultTokenServices.setSupportRefreshToken(true);//是否支持令牌
        defaultTokenServices.setTokenStore(tokenStore());//指定token存放的位置

        //针对JWT令牌的添加
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);


        defaultTokenServices.setAccessTokenValiditySeconds(20);//设置令牌的有效时间
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);//设置刷新令牌的时间
        return defaultTokenServices;
    }
}

