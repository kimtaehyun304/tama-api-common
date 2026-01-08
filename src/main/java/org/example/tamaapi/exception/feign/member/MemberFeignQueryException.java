package org.example.tamaapi.exception.feign.member;

public class MemberFeignQueryException extends RuntimeException{
    public MemberFeignQueryException(String message) {
        super(message);
    }
}
