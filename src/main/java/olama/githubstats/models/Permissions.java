package olama.githubstats.models;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permissions{
    public boolean admin;
    public boolean maintain;
    public boolean push;
    public boolean triage;
    public boolean pull;
}
