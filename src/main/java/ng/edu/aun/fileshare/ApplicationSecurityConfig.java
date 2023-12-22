package ng.edu.aun.fileshare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

        private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfig.class);

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                logger.info("Configuring HTTP Security");
                http
                                .csrf(csrf -> csrf.disable()) // Disables CSRF protection
                                .authorizeRequests(requests -> requests
                                                .antMatchers("/login", "/register", "/landing").permitAll()
                                                .antMatchers("/admin/**").hasAuthority("admin")
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/landing")
                                                .permitAll()
                                                .loginProcessingUrl("/login") // Specify the login processing URL
                                                .defaultSuccessUrl("/landing", true) // Specify a default success URL
                                                .successHandler(customAuthenticationSuccessHandler))
                                .exceptionHandling(handling -> handling
                                                .accessDeniedPage("/accessDenied"))
                                .logout(logout -> logout
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/landing").permitAll());
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setUserDetailsService(userDetailsService);
                provider.setPasswordEncoder(bCryptPasswordEncoder());
                return provider;
        }
}
