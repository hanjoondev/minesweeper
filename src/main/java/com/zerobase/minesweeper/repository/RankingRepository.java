package com.zerobase.minesweeper.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.zerobase.minesweeper.entity.Ranking;

public interface RankingRepository extends PagingAndSortingRepository<Ranking, Long> {
    Optional<Ranking> findByGamerId(Long gamerId);
    Page<Ranking> findByEasyTimeNotNull(Pageable pageable);
    Page<Ranking> findByMediumTimeNotNull(Pageable pageable);
    Page<Ranking> findByHardTimeNotNull(Pageable pageable);
    Long countByEasyTimeLessThan(Double easyTIme);
    Long countByMediumTimeLessThan(Double mediumTime);
    Long countByHardTimeLessThan(Double hardTime);
    List<Ranking> findTop3ByOrderByEasyTimeAsc();
    List<Ranking> findTop3ByOrderByMediumTimeAsc();
    List<Ranking> findTop3ByOrderByHardTimeAsc();
}
