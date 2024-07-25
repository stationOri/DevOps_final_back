package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.KeywordResponseDto;
import org.example.oristationbackend.dto.restaurant.RevWaitSettingResDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.type.ReservationType;
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
    //키워드 받아오기
    public List<KeywordResponseDto> findkeywordByRestId(int restId) {
        RestaurantInfo restaurantInfoByRestId = restaurantInfoRepository.findRestaurantInfoByRestId(restId);

        if (restaurantInfoByRestId != null) {
            List<KeywordResponseDto> keywords = new ArrayList<>();
            if (restaurantInfoByRestId.getKeyword1() != null) {
                Keyword keyword1 = restaurantInfoByRestId.getKeyword1();
                keywords.add(new KeywordResponseDto(keyword1.getKeywordId(), keyword1.getKeyword()));
            }
            if (restaurantInfoByRestId.getKeyword2() != null) {
                Keyword keyword2 = restaurantInfoByRestId.getKeyword2();
                keywords.add(new KeywordResponseDto(keyword2.getKeywordId(), keyword2.getKeyword()));
            }
            if (restaurantInfoByRestId.getKeyword3() != null) {
                Keyword keyword3 = restaurantInfoByRestId.getKeyword3();
                keywords.add(new KeywordResponseDto(keyword3.getKeywordId(), keyword3.getKeyword()));
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

    //예약 및 웨이팅 받아오기
    public RevWaitSettingResDto findRevWaitSettingByRestId(int restId) {

        RestaurantInfo restaurantInfo = restaurantInfoRepository.findRestaurantInfoByRestId(restId);

        if (restaurantInfo == null) {
            throw new RuntimeException("Restaurant not found with ID: " + restId);
        }

        RevWaitSettingResDto resDto = new RevWaitSettingResDto();
        resDto.setRevWait(restaurantInfo.getRevWait()); // 예: A, B, C
        resDto.setRestReserveopenRule(restaurantInfo.getRestReserveopenRule()); // 예: WEEK, MONTH
        resDto.setRestReserveInterval(restaurantInfo.getRestReserveInterval()); // 예: ONEHOUR, HALFHOUR
        resDto.setMaxPpl(restaurantInfo.getMaxPpl());
        resDto.setRestTablenum(restaurantInfo.getRestTablenum());
        resDto.setRestDepositMethod(restaurantInfo.getRestDepositMethod()); // 예: A, B
        resDto.setRestDeposit(restaurantInfo.getRestDeposit());

        return resDto;
    }

    @Transactional
    public int updateResWaitSetting(int restId, RevWaitSettingResDto resDto) {
        RestaurantInfo restaurantInfo = restaurantInfoRepository.findRestaurantInfoByRestId(restId);
        if (restaurantInfo == null) {
            return 0;
        }

        if (resDto.getRevWait().equals(ReservationType.A)) {
            // RevWait이 "A"일 때 나머지 필드 초기화
            restaurantInfo.setRevWait(resDto.getRevWait());
            restaurantInfo.setRestReserveopenRule(null); // 혹은 "WEEK" 또는 "MONTH"로 설정할 수 있음
            restaurantInfo.setRestReserveInterval(null); // 혹은 "HALFHOUR" 또는 "ONEHOUR"로 설정할 수 있음
            restaurantInfo.setMaxPpl(0);
            restaurantInfo.setRestTablenum(0);
            restaurantInfo.setRestDepositMethod(null); // "A" 또는 "B"로 설정할 수 있음
            restaurantInfo.setRestDeposit(0);
        } else {
            // RevWait이 "A"가 아닐 때는 전달된 값으로 설정
            restaurantInfo.setRevWait(resDto.getRevWait());
            restaurantInfo.setRestReserveopenRule(resDto.getRestReserveopenRule());
            restaurantInfo.setRestReserveInterval(resDto.getRestReserveInterval());
            restaurantInfo.setMaxPpl(resDto.getMaxPpl());
            restaurantInfo.setRestTablenum(resDto.getRestTablenum());
            restaurantInfo.setRestDepositMethod(resDto.getRestDepositMethod());
            restaurantInfo.setRestDeposit(resDto.getRestDeposit());
        }

        restaurantInfoRepository.save(restaurantInfo);

        return restId;
    }




}
