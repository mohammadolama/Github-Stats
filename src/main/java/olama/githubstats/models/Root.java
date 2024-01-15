package olama.githubstats.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Root{
    public int id;
    public String node_id;
    public String name;
    public String full_name;
    public String html_url;
    public Object description;
}
