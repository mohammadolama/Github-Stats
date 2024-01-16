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

    private List<RepositoryCommitDto> commitList;

    private int starsCount;

    private int starsMedian;

    private int contributorsCount;

    private int contributorsMedian;

    private int branchCount;

    private int branchMedian;

    private int tagsCount;

    private int tagsMedian;

    private int forksCount;

    private int forksMedian;

    private Integer releasesCount;

    private Integer releasesMedian;

    private Integer closedIssuesCount;

    private Integer closedIssuesMedian;

    private Integer environmentsCount;

    private Integer environmentsMedian;

    private Integer deploymentsCount;

    private Integer deploymentsMedian;

    private Map<String, Language> languages;


}
