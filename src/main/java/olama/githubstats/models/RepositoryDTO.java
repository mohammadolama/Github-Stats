package olama.githubstats.models;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepositoryDTO {
    private String name;

    private Integer countCommits;

    private Integer starCounts;

    private Integer contributorCount;

    private Integer branchesCount;

    private Integer tagsCount;

    private Integer forksCount;

    private Integer releasesCount;

    private Integer closedIssues;

    private Integer environments;

    private Integer deployments;

    private Map<String , Integer> languages;


}
