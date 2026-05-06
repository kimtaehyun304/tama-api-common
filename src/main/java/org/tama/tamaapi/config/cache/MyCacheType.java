package org.tama.tamaapi.config.cache;

import lombok.Getter;

@Getter
public enum MyCacheType {

    //부하 테스트 결과, TPS 14 길래 캐시 적용
    BEST_ITEM(60*60*24, 10000);

    private final int expireAfterWrite;
    private final int maximumSize;

    MyCacheType(int expireAfterWrite, int maximumSize) {
        this.expireAfterWrite = expireAfterWrite;
        this.maximumSize = maximumSize;
    }

}
