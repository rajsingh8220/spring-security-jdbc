package io.singh.springsecurityjdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class MySecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	@Autowired
	DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Using default schema of user table
		/*
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.withDefaultSchema()  // Default schema includes USERNAME, PASSWORD, ENABLED
			.withUser(
					User.withUsername("user")
						.password("pass")
						.roles("USER")
					)
			.withUser(
					User.withUsername("admin")
						.password("pass")
						.roles("ADMIN")					
					);
					
		*/
		
		//Default schema, default behavior  but lets look at custom
		/*
		auth.jdbcAuthentication()
			.dataSource(dataSource);
		*/
		
		
		//Customizing the default behavior and specifying the username and password and authority.
		//Telling spring that how to fetch username and authority
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("select username, password, enabled from users where username = ?")
			.authoritiesByUsernameQuery("select username, authority from authorities where username = ?");
		
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		//super.configure(http);
		
		http.authorizeRequests()
			.antMatchers("/admin").hasRole("ADMIN")
			.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
			.antMatchers("/", "static/css", "static/js").permitAll()
			.and().formLogin();
	
	}
	
	//We need password encoder, but for now no op password encoder
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	

}
