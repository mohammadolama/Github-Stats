package olama.githubstats.models;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    private int commitsCount;

    private int commitsMedian;

    private List<Integer> commits;

    private int starsCount;

    private int starsMedian;

    private List<Integer> stars;

    private int contributorsCount;

    private int contributorsMedian;

    private List<Integer> contributors;

    private int branchCount;

    private int branchMedian;

    private List<Integer> branches;

    private int tagsCount;

    private int tagsMedian;

    private List<Integer> tags;

    private int forksCount;

    private int forksMedian;

    private List<Integer> forks;

    private Integer releasesCount;

    private Integer releasesMedian;

    private List<Integer> releases;

    private Integer closedIssuesCount;

    private Integer closedIssuesMedian;

    private List<Integer> closedIssues;

    private Integer environmentsCount;

    private Integer environmentsMedian;

    private List<Integer> environments;

    private Integer deploymentsCount;

    private Integer deploymentsMedian;

    private List<Integer> deployments;

    private Map<String, Language> languages;


}
