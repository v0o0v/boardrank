package net.boardrank.boardgame.domain;

public enum GameMatchStatus {
    init //게임 시작 대기. Ready
    ,proceeding //게임 진행 증. In Progress
    ,finished //게임 결과 승인 대기 중. Waiting Result
    ,resultAccepted //매치 완료. Finished
}
