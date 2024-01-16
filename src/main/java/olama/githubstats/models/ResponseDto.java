package olama.githubstats.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {

    @JsonProperty("total counts of all commits")
    private int commitsCount;

    @JsonProperty("median count of all commits")
    private int commmitsMedian;

    @JsonProperty("list of commits")
    private List<RepositoryCommitDto> commitList;

    @JsonProperty("total counts of all stars")
    private int starsCount;

    @JsonProperty("median count of all stars")
    private int starsMedian;

    @JsonProperty("total count of all contributors")
    private int contibutorsCount;

    @JsonProperty("median of contributors")
    private int contributorsMedian;

    @JsonProperty("total count of all branches")
    private int branchCount;

    @JsonProperty("median of branches")
    private int branchMedian;
}
