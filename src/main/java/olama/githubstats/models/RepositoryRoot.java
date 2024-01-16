package olama.githubstats.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryRoot {

    private RepositoryRootDetails details;

    private List<Deployments> deployments;

    private Environments environments;

    private HashMap<String , Integer> languages;

    private List<Release> releases;

    private List<Tags> tags;

    private List<Contributor> contributors;

    private List<Branch> branches;

    private List<Commit> commits;

    public RepositoryRoot(RepositoryRootDetails details) {
        this.details = details;
    }
}
