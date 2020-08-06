package wooteco.subway.members.member.application;

import wooteco.security.core.userdetails.UserDetailsService;
import wooteco.security.core.authentication.AuthenticationException;
import wooteco.subway.members.member.domain.LoginMember;
import wooteco.subway.members.member.domain.Member;
import wooteco.subway.members.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember loadUserByUsername(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(AuthenticationException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }
}
