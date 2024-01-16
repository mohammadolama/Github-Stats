package olama.githubstats.models;

import lombok.Data;

@Data
public class Language {

    private String name;

    private Integer total;

    private Integer median;
}
