package org.example.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tamaapi.domain.user.Member;
import org.example.tamaapi.feignClient.member.MemberFeignClient;
import org.example.tamaapi.feignClient.member.MemberResponse;
import org.example.tamaapi.repository.MemberRepository;
import org.example.tamaapi.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEventConsumer {
    private final String MEMBER_TOPIC = "member_topic";

    private final MemberRepository memberRepository;
    private final MemberFeignClient memberFeignClient;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = MEMBER_TOPIC, groupId = "member_consumer_group")
    public void consumeOrderCreatedEvent(MemberCreatedEvent event, Acknowledgment ack) {
        Long memberId = event.memberId();
        Member member = memberFeignClient.findMember(memberId).toEntity();
        memberRepository.save(member);
        ack.acknowledge();
    }
}