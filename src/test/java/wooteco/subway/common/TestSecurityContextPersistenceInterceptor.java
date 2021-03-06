package wooteco.subway.common;

import wooteco.security.core.Authentication;
import wooteco.security.core.context.SecurityContext;
import wooteco.security.core.context.SecurityContextHolder;
import wooteco.subway.members.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestSecurityContextPersistenceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = new Authentication(new LoginMember(1L, "email@email.com", "password", 20));
        SecurityContextHolder.setContext(new SecurityContext(authentication));
        return true;
    }
}
