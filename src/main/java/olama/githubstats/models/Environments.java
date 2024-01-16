package olama.githubstats.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Environments {
    public int total_count;
    public ArrayList<Environment> environments;
}

class Environment {
    public int id;
    public String node_id;
    public String name;
    public String url;
    public String html_url;
    public Date created_at;
    public Date updated_at;
}