package olama.githubstats.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepositoryCommitDto {
    private String repository;
    private int commitCount;
}
