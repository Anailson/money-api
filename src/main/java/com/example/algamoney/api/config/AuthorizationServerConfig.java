package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoney.api.config.token.CustomTokenEnhancer;

@Configurable
@EnableResourceServer
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()
				.withClient("angular")//nome do cliente
				.secret("@ngul@r0")
				.scopes("read", "write")//scope limpar o acesso do cliente angular
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(20)//30minutos pra acessar
				.refreshTokenValiditySeconds(3600 * 24) // 1 dia pra expirar o token 
				
				.and()
				.withClient("mobile")
				.secret("m0b1l30")
				.scopes("read")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24);
				
	}	

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), acessTokenConverter()));
		
		endpoints
				.tokenStore(tokenStore())
				.accessTokenConverter(acessTokenConverter())
				//.authenticationManager(authenticationManager);
				.reuseRefreshTokens(false)//ser usa a aplica????o todo o dia o token n??o expirar
				.authenticationManager(authenticationManager);
	}

	@Bean
	public JwtAccessTokenConverter acessTokenConverter() {
		JwtAccessTokenConverter acceAccessTokenConverter = new JwtAccessTokenConverter();
		acceAccessTokenConverter.setSigningKey("anailson");//chave que valida o token
		
		return acceAccessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		
		return new JwtTokenStore(acessTokenConverter());
		//return new InMemoryTokenStore(AccessTokenConverter());
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
	    return new CustomTokenEnhancer();
	}

}
