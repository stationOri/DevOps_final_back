package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.FavoriteDto;
import org.example.oristationbackend.dto.user.FavoriteDto;
import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;

  // 사용자 id에 따라 찜 목록 조회
  public List<FavoriteDto> getFavoritesByUserId(int userId) {
    return favoriteRepository.findByUser_UserId(userId).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  // 찜 추가
  @Transactional
  public FavoriteDto addFavorite(Favorite favorite) {
    Favorite savedFavorite = favoriteRepository.save(favorite);
    return convertToDto(savedFavorite);
  }

  // 찜 삭제
  @Transactional
  public void deleteFavorite(int favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }

  // 엔티티를 DTO로 변환
  private FavoriteDto convertToDto(Favorite favorite) {
    return new FavoriteDto(
        favorite.getFavoriteId(),
        favorite.getUser().getUserId(),
        favorite.getRestaurant().getRestId()
    );
  }
}