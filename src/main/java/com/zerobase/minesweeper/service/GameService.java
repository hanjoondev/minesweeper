package com.zerobase.minesweeper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zerobase.minesweeper.dto.GameRequest;
import com.zerobase.minesweeper.dto.GameResponse;
import com.zerobase.minesweeper.dto.GamerStatResponse;
import com.zerobase.minesweeper.dto.GetGameListResponse;
import com.zerobase.minesweeper.dto.GetGameResponse;
import com.zerobase.minesweeper.dto.LandingPageDto;
import com.zerobase.minesweeper.dto.RankingResponse;
import com.zerobase.minesweeper.dto.Rank;
import com.zerobase.minesweeper.entity.Game;
import com.zerobase.minesweeper.entity.Gamer;
import com.zerobase.minesweeper.entity.Ranking;
import com.zerobase.minesweeper.exception.GameException;
import com.zerobase.minesweeper.repository.GameRepository;
import com.zerobase.minesweeper.repository.GamerRepository;
import com.zerobase.minesweeper.repository.RankingRepository;
import com.zerobase.minesweeper.type.ErrorCode;
import com.zerobase.minesweeper.type.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GamerRepository gamerRepository;
    private final RankingRepository rankingRepository;

    @Transactional
    public GameResponse registerGame(GameRequest request) {
        int h = request.getHeight(), w = request.getWidth(), m = request.getNumMines();
        if (h < 8 || h > 80) return GameResponse.builder().errorCode(1).build();
        if (w < 8 || w > 80) return GameResponse.builder().errorCode(2).build();
        if (m < h * w / 10 || m > h * w * 2 / 3) return GameResponse.builder().errorCode(3).build();
        if (request.getGamerId() > 0 && gamerRepository.findById(request.getGamerId()).isEmpty())
            return GameResponse.builder().errorCode(4).build();
        String difficulty = h == 10 && w == 10 && m == 10 ? "Easy"
                          : h == 16 && w == 16 && m == 40 ? "Medium"
                          : h == 16 && w == 30 && m == 99 ? "Hard" : "Custom";
        Integer tmp = h * w * m, score = 0;
        while (tmp > 10) {
            tmp /= 10;
            score++;
        }
        Game game = Game.builder()
                        .height(h).width(w).numMines(m)
                        .timePlayed(request.getTimePlayed())
                        .nearbyMines(request.getNearbyMines())
                        .steps(request.getSteps())
                        .difficulty(difficulty)
                        .gamerId(request.getGamerId() > 0 ? request.getGamerId() : createGamer().getId())
                        .isAnon(request.getGamerId() > 0 ? false : true)
                        .score(score)
                        .build();
        gameRepository.save(game);
        updateRanking(game.getGamerId(), difficulty, request.getTimePlayed(), game.getId());
        return GameResponse.builder()
                           .gameId(game.getId())
                           .gamerId(game.getGamerId())
                           .build();
    }

    @Transactional
    public Gamer createGamer() {
        Gamer gamer = Gamer.builder().build();
        gamerRepository.save(gamer);
        gamer.setName("anonymous" + String.valueOf(gamer.getId()));
        return gamer;
    }

    @Transactional
    public void updateRanking(Long gamerId, String difficulty, Double timePlayed, Long gameId) {
        Optional<Ranking> optionalRanking = rankingRepository.findByGamerId(gamerId);
        Ranking ranking = optionalRanking.isEmpty() ? Ranking.builder().build() : optionalRanking.get();
        if (optionalRanking.isEmpty()) ranking.setGamerId(gamerId);
        ranking.setGamerName(gamerRepository.findById(gamerId).get().getName());
        if (difficulty.equals("Easy")) {
            if (ranking.getEasyTime() == null || timePlayed < ranking.getEasyTime()) {
                ranking.setEasyId(gameId);
                ranking.setEasyTime(timePlayed);
            }
        } else if (difficulty.equals("Medium")) {
            if (ranking.getMediumTime() == null || timePlayed < ranking.getMediumTime()) {
                ranking.setMediumId(gameId);
                ranking.setMediumTime(timePlayed);
            }
        } else if (difficulty.equals("Hard")) {
            if (ranking.getHardTime() == null || timePlayed < ranking.getHardTime()) {
                ranking.setHardId(gameId);
                ranking.setHardTime(timePlayed);
            }
        }
        rankingRepository.save(ranking);
    }

    public GetGameResponse getGame(String gameId) {
        Long id = Long.valueOf(gameId);
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isEmpty()) return GetGameResponse.builder().errorCode(-1).build();
        Game game = optionalGame.get();
        return GetGameResponse.builder()
                              .difficulty(game.getDifficulty())
                              .nearbyMines(game.getNearbyMines())
                              .steps(game.getSteps())
                              .createdAt(game.getCreatedAt())
                              .timePlayed(game.getTimePlayed())
                              .build();
    }

    public GetGameListResponse getGameList(String gamerId) {
        Long id = Long.valueOf(gamerId);
        Optional<Gamer> optionalGamer = gamerRepository.findById(id);
        List<Game> games = gameRepository.findFirst10ByGamerIdOrderByCreatedAtDesc(id);
        return GetGameListResponse.builder()
                .gamerId(id)
                .errorCode(optionalGamer.isEmpty() ? 1 : games.size() == 0 ? 2 : 0)
                .gameList(games.stream().map(i -> i.getId()).collect(Collectors.toList()))
                .difficultyList(games.stream().map(i -> i.getDifficulty()).collect(Collectors.toList()))
                .timeList(games.stream().map(i -> i.getTimePlayed()).collect(Collectors.toList()))
                .createdAtList(games.stream().map(i -> i.getCreatedAt().toString()).collect(Collectors.toList()))
                .build();
    }

    public LandingPageDto getLandingPageRanking() {
        List<Ranking> eList = rankingRepository.findTop3ByOrderByEasyTimeAsc();
        List<Ranking> mList = rankingRepository.findTop3ByOrderByMediumTimeAsc();
        List<Ranking> hList = rankingRepository.findTop3ByOrderByHardTimeAsc();
        List<Rank> easy = new ArrayList<>(), medium = new ArrayList<>(), hard = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            long rank = i + 1;
            Ranking e = eList.get(i), m = mList.get(i), h = hList.get(i);
            easy.add(new Rank(rank, e.getGamerName(), e.getEasyTime(), e.getEasyId(), e.getGamerId()));
            medium.add(new Rank(rank, m.getGamerName(), m.getMediumTime(), m.getMediumId(), m.getGamerId()));
            hard.add(new Rank(rank, h.getGamerName(), h.getHardTime(), h.getHardId(), h.getGamerId()));
        }
        return LandingPageDto.builder()
                             .easy(easy)
                             .medium(medium)
                             .hard(hard)
                             .build();
    }

    public RankingResponse getGamerRanking(String difficulty, Integer pageIdx, Integer pageSize) {
        Integer d = difficulty.equals("Hard") ? 2 :
                    difficulty.equals("Medium") ? 1 : 0;
        Pageable pageable = PageRequest.of(pageIdx, pageSize,
                Sort.by(d == 2 ? "hardTime" : d == 1 ? "mediumTime" : "easyTime").ascending());
        Page<Ranking> page = d == 2 ? rankingRepository.findByHardTimeNotNull(pageable) :
                             d == 1 ? rankingRepository.findByMediumTimeNotNull(pageable)
                                    : rankingRepository.findByEasyTimeNotNull(pageable);
        RankingResponse response = RankingResponse.builder()
                                                  .difficulty(difficulty)
                                                  .firstPage(page.isFirst())
                                                  .lastPage(page.isLast())
                                                  .numberOfElements(page.getNumberOfElements())
                                                  .totalElements(page.getTotalElements())
                                                  .contents(new ArrayList<>())
                                                  .build();
        Integer numItems = page.getNumberOfElements();
        Long start = page.getPageable().getOffset() + 1;
        for (int i = 0; i < numItems; i++) {
            Double[] times = new Double[] { page.getContent().get(i).getEasyTime(),
                                            page.getContent().get(i).getMediumTime(),
                                            page.getContent().get(i).getHardTime() };
            Long[] ids = new Long[] { page.getContent().get(i).getEasyId(),
                                      page.getContent().get(i).getMediumId(),
                                      page.getContent().get(i).getHardId() };
            response.getContents().add(Rank.builder()
                                           .ranking(start + i)
                                           .name(page.getContent().get(i).getGamerName())
                                           .time(times[d])
                                           .gameId(ids[d])
                                           .gamerId(page.getContent().get(i).getGamerId())
                                           .build());
        }
        return response;
    }

    public GamerStatResponse getGamerStat(String gamerId) {
        Long id = Long.valueOf(gamerId);
        Optional<Ranking> optionalGamer = rankingRepository.findByGamerId(id);
        if (optionalGamer.isEmpty()) return GamerStatResponse.builder()
                                                             .gamerId(id)
                                                             .errorCode(1)
                                                             .build();
        Ranking r = optionalGamer.get();
        Double e = r.getEasyTime(), m = r.getMediumTime(), h = r.getHardTime();
        return GamerStatResponse.builder()
                .gamerId(id)
                .easyCleared(gameRepository.countByGamerIdAndDifficulty(id, "Easy"))
                .mediumCleared(gameRepository.countByGamerIdAndDifficulty(id, "Medium"))
                .hardCleared(gameRepository.countByGamerIdAndDifficulty(id, "Hard"))
                .easyRank(e != null ? rankingRepository.countByEasyTimeLessThan(e) + 1 : null)
                .mediumRank(m != null ? rankingRepository.countByMediumTimeLessThan(m) + 1 : null)
                .hardRank(h != null ? rankingRepository.countByHardTimeLessThan(h) + 1 : null)
                .easyTime(e)
                .mediumTime(m)
                .hardTime(h)
                .isAdmin(gamerRepository.findById(id).get().getRole().equals(Role.ROLE_ADMIN) ? true : false)
                .build();
    }

    @Transactional
    public boolean deleteGame(String gameId) {
        Long id = Long.valueOf(gameId);
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameException(ErrorCode.GAME_NOT_FOUND));
        gameRepository.delete(game);
        return true;
    }
}
