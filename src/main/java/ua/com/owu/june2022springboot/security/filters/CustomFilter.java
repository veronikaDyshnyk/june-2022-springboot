package ua.com.owu.june2022springboot.security.filters;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class CustomFilter extends OncePerRequestFilter{
    private CustomerDAO customerDAO;

    public CustomFilter(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

         String authorization = request.getHeader("Authorization");
         if (authorization != null && authorization.startsWith("Bearer ")){
         String token = authorization.replace("Bearer", "");

         String subject = Jwts.parser()
         .setSigningKey("okten".getBytes(StandardCharsets.UTF_8))
         .parseClaimsJws(token)/*починаємо витягувати інфу з токена*/
         .getBody()
         .getSubject();

         System.out.println(subject);//should be a login

         Customer customerByLogin = customerDAO.findCustomerByLogin(subject);
         System.out.println(customerByLogin);
         if (customerByLogin != null){
         SecurityContextHolder.getContext().setAuthentication(
         new UsernamePasswordAuthenticationToken(
         customerByLogin.getLogin(),
         customerByLogin.getPassword(),
         Collections.singletonList(new SimpleGrantedAuthority(customerByLogin.getRole())))
         );
         }
         }

         filterChain.doFilter(request,response);
         }
    }




//
//
