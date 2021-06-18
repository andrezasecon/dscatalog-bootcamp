package com.andrezasecon.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	private static final String[] PUBLIC = {"/oauth/token"};
	
	private static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"};
	
	private static final String[] ADMIN = {"/users/**"};
	
	@Autowired
	private JwtTokenStore tokenStore;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll() // rota /oauth/token pode ser acessada por todos
		.antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll() // rotas /products/ e /categories/ operador ou admin acessa somente método GET
		.antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")//define os perfis de OPERATOR_OR_ADMIN acima
		.antMatchers(ADMIN).hasRole("ADMIN") // rota /users pode ser acessado somente pelo perfil ADMIN
		.anyRequest().authenticated(); // qualquer outra rota não precisa de autenticação
	}
	
	

}
