package ua.com.owu.june2022springboot.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity//впроваджує дефольні налаштування шоб секюріті починало обробку запитів
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomerDAO customerDAO;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }//шифрування паролю

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //get patr obj and auth прийнити логін паспорт і знайти обєкт в бд
        auth.userDetailsService(new UserDetailsService() /*фенкціональний інтерфейс,саме UDS знаходить об в бд */ {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                //цей метод займається тим щоб знаходити логін в бд, тобто він має бути унікальним
                Customer customer = customerDAO.findByLogin(username);//ми знайшли в бд свій обєкт
                List<SimpleGrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority(customer.getRole()));
                //створили обгортку, розібрали об на куски і поклали в обгортку
                User user = new User(
                        customer.getLogin(),
                        customer.getPassword(),
                        roles
                );
                return user;
                //тепер спрінг візьме об user, піде в бд і шукатиме по тих пропертях
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//http request access/denied
        http
                .csrf().disable()//застаріла модель
                .cors().disable()//
                .authorizeRequests()//тут починається авторизація
                .antMatchers(HttpMethod.GET, "/", "/open").permitAll()
                //будемо обробляти урлу якимось методом,і вона доступна "всім", або denyAll()щоб "заборонити"
                .antMatchers(HttpMethod.POST, "/save").permitAll()
                .antMatchers(HttpMethod.GET, "/secure").hasAnyRole("CLIENT", "ADMIN")
                //перейти за посиланням може тільки той хто має "дозвіл"
                .and()
                //дозволяє конвертувати обєкт який приходить з antMatchers в об титу http security
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //хочемк доступитись до менеджменту сесій і сказати що в нас система без сесій
        ;

    }
}
//WSCA це абстрактний клас (не можна створити його екземпляр), цей клас дає багато різноманітних методів
// в яких потрібно прописати конфігурфцію. і фактично спрінг буде шукати не SecurityConfig, а нащадків WSCA
//це і є адекватний приклад наслідування
