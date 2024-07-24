package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    public Keyword findKeywordByKeywordId(int keywordId);
}
