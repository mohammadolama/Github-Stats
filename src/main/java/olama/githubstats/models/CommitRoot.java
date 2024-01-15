package olama.githubstats.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommitRoot {

    public String sha;
    public String node_id;
}
