package com.example.config;

import com.example.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        Integer userId = null;

        // Authorization header'ı kontrol et
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // Token'dan kullanıcı bilgilerini çıkar
                username = jwtService.getUsernameFromToken(token);
                userId = jwtService.getUserIdFromToken(token);
                
                // Token geçerli mi kontrol et
                if (username != null && jwtService.validateToken(token) && 
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                    
                    // Authentication oluştur
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // User ID'yi request attribute'una ekle
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);
                    
                    // Security context'e ekle
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.error("JWT Token parsing error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // Auth endpoint'leri ve public endpoint'leri filtrele
        return path.startsWith("/api/auth/") || 
               path.startsWith("/api/cards/public") || 
               path.startsWith("/api/cards/search") ||
               path.startsWith("/api/cards/test");
    }
}
