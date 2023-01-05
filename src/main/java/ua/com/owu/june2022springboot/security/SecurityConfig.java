package ua.com.owu.june2022springboot.security;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.security.filters.CustomFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity//впроваджує дефольні налаштування шоб секюріті починало обробку запитів
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomerDAO customerDAO;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> {
            System.out.println("login trig");//для того щоб глянути коли мотод спрацьвує
            Customer customer = customerDAO.findCustomerByLogin(username);//це для того щоб знайти об в бд
            return new User(/*це ми формуємо обєкт автентифікації*/
                    customer.getLogin(),
                    customer.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(customer.getRole())));

        });

    }
    //конфігурвція щоб можна було користуватися бд

    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter(customerDAO);
    }


    @Bean
    CorsConfigurationSource configurationSource() {
        //тут ми пропишемо конфігурацію з яких і ще хостів/серверів дозволено звертатись до джавішної апки,
        //які  http методи дозволені, які додаткові хедери треба показувати на стороні клієнта

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));//"дозволено звертатися з таких хостів"
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.GET.name(),
                HttpMethod.HEAD.name()
        ));

        configuration.addExposedHeader("Authorization");//зі сторони бека формуємо токен який треба відправити кліенту,
        // але за замовчуванням кастомних хедерів НЕ ВИДНО тому щоб їх було видно додоєть це^

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();//створили обєкт
        //тепер ці всі конфігурація що ми зробити треза привязати до певної урли
        source.registerCorsConfiguration("/**", configuration);//привязали конфігурацію яку налаштували до певної урли
        return source;
    }


    @Override
    @Bean/*так цей метод стане "видимим" */
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.POST, "/save").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/secure").hasAnyRole("ADMIN", "USER")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().configurationSource(configurationSource())//нижче ми описуємо налаштування
                //кастомний фільтр для фотмування токенів, в нас є по замовчуванню вбудований фільтр
                // і  якщо ми хочемо застосувати свій то треба додати його ось так
//                .and().addFilterBefore(
//                        (servletRequest, servletResponse, filterChain) -> {
//                            System.out.println("custom filter");
//                            filterChain.doFilter(servletRequest,servletResponse);
//                        },//таке робиться щоб наш фільтр віддавав то шо треба куда треба
//                        UsernamePasswordAuthenticationFilter.class)
                .and().addFilterBefore(
                       customFilter(),
                UsernamePasswordAuthenticationFilter.class
                )
        ;

    }



}
