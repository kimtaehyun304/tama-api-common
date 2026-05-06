package org.tama.tamaapi.repository;

import org.tama.tamaapi.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
