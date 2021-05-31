package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

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
				.authorizedGrantTypes("password")
				.accessTokenValiditySeconds(1800);//30minutos pra acessar
				
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
				.tokenStore(tokenStore())
				.accessTokenConverter(acessTokenConverter())
				.authenticationManager(authenticationManager);
				//.reuseRefreshTokens(false)//ser usa a aplicação todo o dia o token não expirar
				//.authenticationManager(authenticationManager);
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

}