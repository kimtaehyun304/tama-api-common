package org.example.tamaapi.feignClient.member;


import static org.example.tamaapi.exception.CommonExceptionHandler.throwOriginalException;

public class MemberFallback implements MemberFeignClient{

    private final Throwable cause;

    public MemberFallback(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public MemberResponse findMember(Long memberId) {
        throwOriginalException(cause);
        return null;
    }
}
