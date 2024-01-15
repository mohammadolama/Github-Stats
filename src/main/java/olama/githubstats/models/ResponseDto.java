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
    private int commitsTotalCount;

    @JsonProperty("median count of all commits")
    private int commmitsMedianCount;

    @JsonProperty("list of commits")
    private List<RepositoryCommitDto> commitList;

    @JsonProperty("total counts of all stars")
    private int totalCountStars;

    @JsonProperty("median count of all stars")
    private int medianCountStars;
}
