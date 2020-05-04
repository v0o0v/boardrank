package net.boardrank.boardgame.domain;

public enum GameMatchStatus {
    init //만든 직후
    ,proceeding //게임 시작 후
    ,finished //게임 끝난 후
    ,resultAccepted // 게임 결과를 멤버들이 받아들임
}
