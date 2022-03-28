package com.chockolate.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.chockolate.service.impl.UserDetailsServiceImpl;

/**
 * Класс настройки Spring Security
 *
 */
@Configuration
@EnableWebSecurity
public class SecutrityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/basket").access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")//для входа в корзину нужно войти под своим логином и паролем
		.antMatchers("/**").permitAll()//разрешено всем независимо от роли заходить на все адреса
		.anyRequest().authenticated().and().formLogin()
		.loginPage("/login").defaultSuccessUrl("/").permitAll().and().logout().permitAll().logoutSuccessUrl("/")//здесь задаю адрес для страницы логирования
		//и в контроллере создал метод реагирующий на /login и перенаправляющий на страницу входа
		.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);//ловим ошибки,перенаправляет на адрес 403
	}

	/**
	 * Spring Security блокирует css,js,image. Данный метод предотвращает блокировку
	 * данных ресурсов
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/js/**");
		web.ignoring().antMatchers("/images/**");
	}
}
