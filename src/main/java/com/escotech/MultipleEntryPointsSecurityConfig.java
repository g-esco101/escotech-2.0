package com.escotech;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MultipleEntryPointsSecurityConfig {
    @Configuration
    @Order(1)
    public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .ignoringAntMatchers("/addItem/*", "/removeItem/*", "product/delete/*", "/state/enum/*", "/create/order", "/paypal/capture", "/updateCart/*")
                    .and()
                    .authorizeRequests(authorize ->
                            authorize.mvcMatchers("/admin/**")
                                    .authenticated())
                    .oauth2Login()
                    .and()
                    .logout()
                    .logoutSuccessUrl("/entry");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().mvcMatchers("/static/css/**", "/static/js/**", "/static/images/**", "/mail/**", "/images/**");
        }
    }

    @Configuration
    @Order(2)
    public static class App3ConfigurationAdapter extends WebSecurityConfigurerAdapter {

        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .ignoringAntMatchers("/", "/addItem/*", "/removeItem/*", "product/delete/*", "/state/enum/*", "/create/order", "/paypal/capture")
                    .and()
                    .authorizeRequests().antMatchers("/about", "/orderReview", "/order/enum/*").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/entry", "/shipping", "/state/enum/*", "/products").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/products/*", "/product/image", "/create/order", "/paypal/capture").permitAll()
                    .and()
                    .authorizeRequests().antMatchers("/services", "/addItem/*", "/removeItem/*", "/cart", "/updateCart").permitAll();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().mvcMatchers("/static/css/**", "/static/js/**", "/static/images/**", "/mail/**", "/images/**");
        }
    }
}
