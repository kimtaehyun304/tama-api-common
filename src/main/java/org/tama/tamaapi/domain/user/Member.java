package org.tama.tamaapi.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tama.tamaapi.domain.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    public Member(Long id, String nickname, Authority authority) {
        this.id = id;
        this.nickname = nickname;
        this.authority = authority;
    }

    public Member(Long id) {
        this.id = id;
    }
}
