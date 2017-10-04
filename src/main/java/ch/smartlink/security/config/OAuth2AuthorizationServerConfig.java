package ch.smartlink.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;


@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;


    @Value("${authorization.clientid.value}")
    private String clientId;
    @Value("${authorization.grant.type}")
    private String grantType;
    @Value("${authorization.scope.read}")
    private String scopeRead;
    @Value("${authorization.scope.write}")
    private String scopeWrite;
    @Value("${authorization.resources.ids}")
    private String resourcesIds;
    @Value("${security.signkey.value}")
    private String signingKey;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);

//        clients.inMemory()
//                .withClient(clientId)
//                .secret(signingKey)
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//                .scopes(scopeRead, scopeWrite)
//                .resourceIds(resourcesIds)
//        .accessTokenValiditySeconds(120)
//        .refreshTokenValiditySeconds(600);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
        endpoints.tokenEnhancer(enhancerChain)
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager);
    }
}
