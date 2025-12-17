package org.example.tamaapi.feignClient.member;

import org.example.tamaapi.domain.user.Member;
import org.example.tamaapi.feignClient.order.FullOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "member-service", url = "http://localhost:5003")
public interface MemberFeignClient {

    //---읽기 msa 동기화---
    @GetMapping("/api/member/{memberId}")
    MemberResponse findMember(@PathVariable Long memberId);

}
