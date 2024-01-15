package olama.githubstats.models;

import lombok.*;

import java.util.List;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDTO {
    private String name;

    private Integer countCommits;

    private Integer starCounts;


}
