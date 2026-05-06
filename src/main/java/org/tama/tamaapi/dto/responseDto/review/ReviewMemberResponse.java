package org.tama.tamaapi.dto.responseDto.review;

import lombok.Getter;

@Getter
public class ReviewMemberResponse {
    private final String nickname;
    private final Integer height;
    private final Integer weight;

    public ReviewMemberResponse(String nickname, Integer height, Integer weight) {
        this.nickname = nickname;
        this.height = height;
        this.weight = weight;
    }
}
