package irrigationsystem.jwt;

import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import irrigationsystem.service.UserService;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Processes incoming HTTP requests to filter and validate JSON Web Tokens (JWTs).
     * This method extracts the JWT from the "Authorization" header, validates it,
     * and sets the authentication in the security context if valid. If the token
     * is invalid or expired, appropriate HTTP error responses are returned.
     *
     * @param request the {@link HttpServletRequest} containing the client's request
     * @param response the {@link HttpServletResponse} to send a response or error back to the client
     * @param filterChain the {@link FilterChain} to pass the request and response
     *        to the next entity in the filter chain
     * @throws IOException if an I/O error occurs during request or response handling
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);

                return;
            }

            String jwtToken = authHeader.substring(7);

            log.debug("JWT {}", jwtToken);

            String username = jwtUtil.extractUsername(jwtToken);

            /*
               Username is not empty and the user is not logged
             */
            if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(jwtToken, userDetails)) {
                    log.debug("User {}", username);

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired. Please login again.");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
