package com.miproyecto.ucursos.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull; 
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
    
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extraer el token
            String email = jwtUtil.extractEmail(token); // Obtener el email del token
            
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                // Debug
                System.out.println("User Details: " + userDetails); // Imprimir detalles del usuario
                
                if (jwtUtil.validateToken(token, userDetails)) {
                    String role = jwtUtil.extractRole(token);
                    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);
    
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    
                    // Debug
                    System.out.println("Authentication set for user: " + email);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no válido");
                    return; 
                }
            }
        }
    
        chain.doFilter(request, response); // Continúa con la cadena de filtros
    }
}

