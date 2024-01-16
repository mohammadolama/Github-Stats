package olama.githubstats.models;

import java.util.Date;

public class Deployments {
    public String url;
    public int id;
    public String node_id;
    public String sha;
    public String ref;
    public String task;
    public String original_environment;
    public String environment;
    public String description;
    public Date created_at;
    public Date updated_at;
    public String statuses_url;
    public String repository_url;
    public boolean transient_environment;
    public boolean production_environment;
}
