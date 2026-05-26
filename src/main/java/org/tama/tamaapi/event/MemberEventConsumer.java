package org.tama.tamaapi.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tama.tamaapi.domain.user.Member;
import org.tama.tamaapi.feignClient.member.MemberFeignClient;
import org.tama.tamaapi.repository.MemberRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberEventConsumer {
    private final String MEMBER_SYNC_TOPIC = "member_sync_topic";

    private final MemberRepository memberRepository;
    private final MemberFeignClient memberFeignClient;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    @KafkaListener(topics = MEMBER_SYNC_TOPIC, groupId = "member_consumer_group")
    public void consumeMemberCreatedEvent(MemberCreatedEvent event, Acknowledgment ack) {
        Long memberId = event.memberId();
        Member member = memberFeignClient.findMember(memberId).toEntity();
        memberRepository.save(member);
        ack.acknowledge();
        log.info("회원 테이블 동기화 완료");
    }
}