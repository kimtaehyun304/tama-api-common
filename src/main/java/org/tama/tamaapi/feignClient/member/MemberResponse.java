package org.tama.tamaapi.feignClient.member;

import lombok.Data;
import org.tama.tamaapi.domain.user.Authority;
import org.tama.tamaapi.domain.user.Member;

@Data
public class MemberResponse {

    private Long id;
    private String nickname;
    private Authority authority;

    public Member toEntity() {
       return new Member(id, nickname, authority);
    }
}

