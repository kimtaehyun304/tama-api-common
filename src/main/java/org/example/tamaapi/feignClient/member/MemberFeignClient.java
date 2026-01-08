package org.example.tamaapi.feignClient.member;


import org.example.tamaapi.exception.feign.member.MemberFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "member-service", url = "http://localhost:5003"
        ,configuration = MemberFeignClientConfig.class
        ,fallbackFactory = MemberFallbackFactory.class)
public interface MemberFeignClient {

    //---읽기 msa 동기화---
    @GetMapping("/api/member/{memberId}")
    MemberResponse findMember(@PathVariable Long memberId);

}
