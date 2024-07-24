package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.KeywordResponseDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.repository.KeywordRepository;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantInfoService {
    private final RestaurantInfoRepository restaurantInfoRepository;
    private final KeywordRepository keywordRepository;

    //예약/웨이팅 사용하는지 유무
    public String FindRev_WaitingTypeByRestId(int restId) {
        return restaurantInfoRepository.findRestaurantInfoByRestId(restId).getRevWait().toString();
    }
    //웨이팅 상태
    public String FindWaitingStatusByRestId(int restId) {
        return restaurantInfoRepository.findRestaurantInfoByRestId(restId).getRestWaitingStatus().toString();
    }

    public List<String> findkeywordByRestId(int restId) {
        RestaurantInfo restaurantInfoByRestId = restaurantInfoRepository.findRestaurantInfoByRestId(restId);

        if (restaurantInfoByRestId != null) {
            List<String> keywords = new ArrayList<>();
            if (restaurantInfoByRestId.getKeyword1() != null) {
                keywords.add(restaurantInfoByRestId.getKeyword1().getKeyword());
            }
            if (restaurantInfoByRestId.getKeyword2() != null) {
                keywords.add(restaurantInfoByRestId.getKeyword2().getKeyword());
            }
            if (restaurantInfoByRestId.getKeyword3() != null) {
                keywords.add(restaurantInfoByRestId.getKeyword3().getKeyword());
            }
            return keywords;
        } else {
            return Collections.emptyList();
        }
    }
    // 키워드 등록
    @Transactional
    public int enrollkeywordByRestId(int restId, int keywordId) {
        RestaurantInfo restaurantInfo = restaurantInfoRepository.findRestaurantInfoByRestId(restId);
        Keyword key = keywordRepository.findKeywordByKeywordId(keywordId);

        if (key == null || restaurantInfo == null) {
            return 0; // Keyword or RestaurantInfo does not exist
        }

        boolean updated = false;
        if (restaurantInfo.getKeyword1() == null) {
            restaurantInfo.setKeyword1(key);
            updated = true;
        } else if (restaurantInfo.getKeyword2() == null) {
            restaurantInfo.setKeyword2(key);
            updated = true;
        } else if (restaurantInfo.getKeyword3() == null) {
            restaurantInfo.setKeyword3(key);
            updated = true;
        }

        if (updated) {
            restaurantInfoRepository.save(restaurantInfo);
            return keywordId;
        }

        return 0; // Indicates no keyword was enrolled
    }

    // 키워드 삭제
    @Transactional
    public int deleteKeywordByRestId(int restId, int keywordId) {
        RestaurantInfo restaurantInfo = restaurantInfoRepository.findRestaurantInfoByRestId(restId);

        if (restaurantInfo == null) {
            return 0; // RestaurantInfo does not exist
        }

        boolean updated = false;

        if (restaurantInfo.getKeyword1() != null && restaurantInfo.getKeyword1().getKeywordId() == keywordId) {
            restaurantInfo.setKeyword1(null);
            updated = true;
        }
        if (restaurantInfo.getKeyword2() != null && restaurantInfo.getKeyword2().getKeywordId() == keywordId) {
            restaurantInfo.setKeyword2(null);
            updated = true;
        }
        if (restaurantInfo.getKeyword3() != null && restaurantInfo.getKeyword3().getKeywordId() == keywordId) {
            restaurantInfo.setKeyword3(null);
            updated = true;
        }

        if (updated) {
            restaurantInfoRepository.save(restaurantInfo);
            return keywordId; // Indicates that a keyword was deleted
        }

        return 0; // Indicates that no keyword was deleted
    }

    // 식당 공지 수정
    @Transactional
    public void updateRestPost(int restId, String restPost) {
        RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restId));
        restaurantInfo.setRestPost(restPost);
        restaurantInfoRepository.save(restaurantInfo);
    }





}
