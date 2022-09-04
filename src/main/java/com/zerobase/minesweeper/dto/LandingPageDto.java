package com.zerobase.minesweeper.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LandingPageDto {
    List<Rank> easy;
    List<Rank> medium;
    List<Rank> hard;
}
