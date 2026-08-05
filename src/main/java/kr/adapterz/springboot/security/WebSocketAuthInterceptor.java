package kr.adapterz.springboot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel
    ) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("WebSocket 인증 토큰이 없습니다.");
            }
            String token = authorizationHeader.substring(7);
                String subject = tokenProvider.validateTokenAndGetSubject(token);
                String[] split = subject.split(":");
                String userId = split[0];
                String authority = "ROLE_" + split[1];

                User user = new User(userId, "", List.of(new SimpleGrantedAuthority(authority)));

                AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
                accessor.setUser(authenticated);
            }

        return message;
    }
}