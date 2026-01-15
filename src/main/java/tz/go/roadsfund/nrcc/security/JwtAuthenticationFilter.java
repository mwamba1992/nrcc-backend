package tz.go.roadsfund.nrcc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tz.go.roadsfund.nrcc.entity.User;
import tz.go.roadsfund.nrcc.repository.UserRepository;
import tz.go.roadsfund.nrcc.service.TokenBlacklistService;

import java.io.IOException;

/**
 * JWT Authentication Filter with blacklist and version checking
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Check if token is blacklisted
                if (tokenBlacklistService.isBlacklisted(jwt)) {
                    logger.warn("Attempt to use blacklisted token");
                    filterChain.doFilter(request, response);
                    return;
                }

                Long userId = tokenProvider.getUserIdFromToken(jwt);
                Integer tokenVersion = tokenProvider.getTokenVersionFromToken(jwt);

                // Check token version
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    Integer currentVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
                    Integer jwtVersion = tokenVersion != null ? tokenVersion : 0;

                    if (!currentVersion.equals(jwtVersion)) {
                        logger.warn("Token version mismatch for user: " + userId +
                                ". Current: " + currentVersion + ", Token: " + jwtVersion);
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
