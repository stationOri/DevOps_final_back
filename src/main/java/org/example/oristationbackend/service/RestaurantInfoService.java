package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantInfoService {
    private final RestaurantInfoRepository restaurantInfoRepository;

    public String FindRev_WaitingTypeByRestId(int restId) {
        return restaurantInfoRepository.findRestaurantInfoByRestId(restId).getRevWait().toString();
    }

    public String FindWaitingStatusByRestId(int restId) {
        return restaurantInfoRepository.findRestaurantInfoByRestId(restId).getRestWaitingStatus().toString();
    }
}
