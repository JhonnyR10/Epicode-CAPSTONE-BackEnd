package Giovanni.Longo.EpicodeCAPSTONEBackEnd.security;


import Giovanni.Longo.EpicodeCAPSTONEBackEnd.exceptions.UnauthorizedException;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.model.User;
import Giovanni.Longo.EpicodeCAPSTONEBackEnd.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    // Creo un filtro che andrò ad aggiungere alla Security Filter Chain

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Per favore metti il token nell'Authorization header");
        } else {
            String accessToken = authHeader.substring(7);

            jwtTools.verifyToken(accessToken);

            String id = jwtTools.extractIdFromToken(accessToken);
            User user = userService.findById(Long.parseLong(id));

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);


            filterChain.doFilter(request, response);

        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        String path = request.getServletPath();
        String method = request.getMethod();

        return (path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                new AntPathMatcher().match("/auth/**", request.getServletPath()) ||

                new AntPathMatcher().match("/v3/api-docs", request.getServletPath()) ||
                new AntPathMatcher().match("/news/getall/**", request.getServletPath()) ||
                new AntPathMatcher().match("/stripe-webhook/**", request.getServletPath()) ||
                new AntPathMatcher().match("/news/getbyid/**", request.getServletPath()));
    }
}

