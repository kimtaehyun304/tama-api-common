package org.example.tamaapi.exception.feign.member;

public class MemberFeignCommandException extends RuntimeException{
    public MemberFeignCommandException(String message) {
        super(message);
    }
}
