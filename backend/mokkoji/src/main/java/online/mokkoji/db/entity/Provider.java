package online.mokkoji.db.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    GOOGLE("google"), NAVER("naver");
    //KAKAO("kakao")

    private final String key;
}
