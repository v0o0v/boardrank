package net.boardrank.boardgame.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.boardrank.boardgame.service.TimeUtilService;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class GameMatch {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Boardgame boardGame;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Boardgame> expansions = new HashSet<>();

    @OneToOne
    private Account createdMember;

    @OneToOne
    private Account boardgameProvider;

    @OneToOne
    private Account ruleSupporter;

    @Enumerated(EnumType.ORDINAL)
    private GameMatchStatus gameMatchStatus;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "gameMatch")
    private Set<RankEntry> rankentries = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "gameMatch")
    private Set<ImageURL> images = new HashSet<>();


    //지정되는 모든 값은 UTC기준. 다만 get으로 가져올때는 편의상 KST로 변환함.
    private LocalDateTime createdTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private LocalDateTime acceptedTime;
    private String matchTitle;
    private String place;
    private boolean isPrivate = false;

    public GameMatch(Boardgame bg, Account createdMember) {
        this.boardGame = bg;
        this.createdMember = createdMember;
        this.gameMatchStatus = GameMatchStatus.init;
        this.createdTime = LocalDateTime.now();
        this.place = "";
    }

    public LocalDateTime getCreatedTime() {
        return TimeUtilService.transUTCToKTC(createdTime);
    }

    public LocalDateTime getStartedTime() {
        return TimeUtilService.transUTCToKTC(startedTime);
    }

    public LocalDateTime getFinishedTime() {
        return TimeUtilService.transUTCToKTC(finishedTime);
    }

    public LocalDateTime getAcceptedTime() {
        return TimeUtilService.transUTCToKTC(acceptedTime);
    }

    public List<Account> getWinnerList() {
        List<Account> winnerList = new ArrayList<>();
        rankentries.forEach(rankEntry -> {
            if (rankEntry.getRank() != null && rankEntry.getRank().equals(1))
                winnerList.add(rankEntry.getAccount());
        });

        if (winnerList.isEmpty()) return null;
        return winnerList;
    }

    public String getWinnerByString() {
        List<Account> winnerList = this.getWinnerList();
        if (winnerList == null)
            return "";

        StringBuilder winnerString = new StringBuilder();
        for (Account account : winnerList)
            winnerString.append(account.getName() + " ");

        return winnerString.toString().trim();
    }

    public String getPlayingTime() {
        if (startedTime == null || finishedTime == null)
            return "";

        return "" + Duration.between(startedTime, finishedTime).abs().toMinutes();
    }

    @Override
    public String toString() {
        return "GameMatch{" +
                "id=" + id +
                ", matchTitle='" + matchTitle + '\'' +
                '}';
    }

    public void resetRank() {
        rankentries.forEach(rankEntry -> {
            rankEntry.setRank(getNumOfGreaterThanMe(rankEntry) + 1);
        });
    }

    //나보다 점수 높은 사람들 수
    public int getNumOfGreaterThanMe(RankEntry me) {
        return (int) (this.rankentries.stream()
                .filter(rankEntry -> !rankEntry.equals(me))
                .filter(rankEntry -> me.getScore() < rankEntry.getScore())
                .count());
    }

    //나보다 점수 낮은 사람들 수
    public int getNumOfSmallerThanMe(RankEntry me) {
        return (int) (this.rankentries.stream()
                .filter(rankEntry -> !rankEntry.equals(me))
                .filter(rankEntry -> me.getScore() > rankEntry.getScore())
                .count());
    }

    //account가 매치 결과를 isAccept한다.
    public void applyResultAcceptance(Account account, boolean isAccept) {
        this.getRankentries().stream()
                .filter(rankEntry -> rankEntry.getAccount().equals(account))
                .findFirst().orElseThrow(RuntimeException::new)
                .setResultAcceptStatus(isAccept ? ResultAcceptStatus.Accept : ResultAcceptStatus.Deny)
        ;
    }

    //모든 사람이 match 결과를 투표했는지
    public boolean isOverPollOfMatchResult() {
        return this.getRankentries().stream()
                .filter(rankEntry ->
                        rankEntry.getResultAcceptStatus().equals(ResultAcceptStatus.Accept)
                                || rankEntry.getResultAcceptStatus().equals(ResultAcceptStatus.Deny)
                ).count() >= (int) this.getRankentries().size();
    }

    public boolean isAccetableOfMatchResult() {
        return this.getRankentries().stream()
                .filter(rankEntry ->
                        rankEntry.getResultAcceptStatus().equals(ResultAcceptStatus.Accept)
                ).count() >= (int) this.getRankentries().size() / 2;
    }

    public String getFullBoardgameString() {
        StringBuilder name = new StringBuilder();
        name.append(this.boardGame.getName());

        this.getExpansions().forEach(boardgame -> {
            name.append(" + " + boardgame.getName());
        });

        return name.toString();
    }

    public List<Account> getAllParticiants() {
        return getRankentries().stream()
                .map(RankEntry::getAccount)
                .collect(Collectors.toList());
    }

    public void deleteImageURL(String filename) {
        ImageURL imageURL = this.findImageURLAsFilename(filename);
        if(imageURL==null) return;
        this.getImages().remove(imageURL);
    }

    private ImageURL findImageURLAsFilename(String filename) {
        for(ImageURL imageURL : getImages()){
            if(imageURL.getFilename().equals(filename))
                return imageURL;
        }
        return null;
    }

    public void copyRankEntryValue(GameMatch src) {
        src.getRankentries().forEach(rankEntrySrc -> {
            RankEntry rankEntryDest = this.getRankEntry(rankEntrySrc.getAccount());
            if(rankEntryDest==null) return;
            rankEntryDest.setScore(rankEntrySrc.getScore());
            rankEntryDest.setRank(rankEntrySrc.getRank());
        });
    }

    public RankEntry getRankEntry(Account account){
        for(RankEntry rankEntry : this.getRankentries()){
            if(rankEntry.getAccount().equals(account))
                return rankEntry;
        }
        return null;
    }
}
