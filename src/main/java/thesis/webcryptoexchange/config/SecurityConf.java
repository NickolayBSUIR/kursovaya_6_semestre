package thesis.webcryptoexchange.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import thesis.webcryptoexchange.listener.CustomAuthSuccListener;
import org.springframework.security.core.session.*;

@Configuration
@EnableWebSecurity
public class SecurityConf extends WebSecurityConfigurerAdapter {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private DataSource dataSource;
     
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder())
            .dataSource(dataSource)
            .usersByUsernameQuery("select name, password, enabled from users where name=?")
            .authoritiesByUsernameQuery("select name, role from users where name=?")
        ;
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/reg", "/login", "/login-error").anonymous()
            .antMatchers("/resources/css/**", "/resources/img/**").permitAll()
            .antMatchers("/users", "/update", "/block").hasAuthority("ROLE_ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin().successHandler(new CustomAuthSuccListener()).loginPage("/login").failureUrl("/login-error")
            .and()
            .logout().logoutUrl("/logout").invalidateHttpSession(true).permitAll()
            .and()
            .sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()).expiredUrl("/login");
    }

    @Bean
    public SessionRegistry sessionRegistry() {
      return new SessionRegistryImpl();
    }
}