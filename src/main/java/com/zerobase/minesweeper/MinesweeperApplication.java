package com.zerobase.minesweeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.zerobase.minesweeper.*" })
public class MinesweeperApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }
}
