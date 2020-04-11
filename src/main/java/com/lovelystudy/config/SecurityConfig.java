package com.lovelystudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lovelystudy.module.security.core.MyAuthenticationProvider;
import com.lovelystudy.module.security.core.MyCustomAuthenticationFilter;
import com.lovelystudy.module.security.core.MyFilterSecurityInterceptor;
import com.lovelystudy.module.security.core.MyUserDetailService;
import com.lovelystudy.module.user.service.PersistentTokenService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyCustomAuthenticationFilter myCustomAuthenticationFilter;
	@Autowired
	private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
	@Autowired
	private MyUserDetailService myUserDetailService;
	@Autowired
	private PersistentTokenService persistentTokenService;
	@Autowired
	private SiteConfig siteConfig;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		MyAuthenticationProvider provider = new MyAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		provider.setUserDetailsService(myUserDetailService);
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http.authorizeRequests().antMatchers("/admin/**").authenticated();

		http.formLogin().loginPage("/adminlogin").loginProcessingUrl("/adminlogin").failureUrl("/adminlogin?error")
				.defaultSuccessUrl("/admin/index").permitAll();

		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout")).logoutSuccessUrl("/adminlogin")
				.deleteCookies("JSESSIONID", siteConfig.getCookie().getAdminUserName());

		http.addFilterBefore(myCustomAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);

	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/static/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices services = new PersistentTokenBasedRememberMeServices("pybbs",
				myUserDetailService, persistentTokenService);
		services.setCookieName(siteConfig.getCookie().getAdminUserName());
		services.setAlwaysRemember(true);
		return services;
	}

}
