package net.boardrank.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .requestCache().requestCache(new VaddinInternalRequestSkipRequestCache())

                .and()
                    .authorizeRequests().requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                        .loginPage(LOGIN_URL).permitAll()
                        .defaultSuccessUrl("/",true).permitAll()
                .and()
                    .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL).permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and()
            .ignoring().antMatchers("/healthCheck")
                .and()
            .ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/logo/**",
                "/background/**",
                "/text/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**",
                "/docs/**"
        );
    }

}
