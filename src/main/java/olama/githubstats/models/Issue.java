package olama.githubstats.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    public String url;
    public String repository_url;
    public String labels_url;
    public String comments_url;
    public String events_url;
    public String html_url;
    public int id;
    public String node_id;
    public int number;
    public String title;
    public ArrayList<Object> labels;
    public String state;
    public boolean locked;
    public Object assignee;
    public ArrayList<Object> assignees;
    public Object milestone;
    public int comments;
    public Date created_at;
    public Date updated_at;
    public Date closed_at;
    public String author_association;
    public Object active_lock_reason;
    public boolean draft;
    public PullRequest pull_request;
    public String body;
    public Reactions reactions;
    public String timeline_url;
    public Object performed_via_github_app;
    public Object state_reason;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PullRequest{
    public String url;
    public String html_url;
    public String diff_url;
    public String patch_url;
    public Date merged_at;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Reactions{
    public String url;
    public int total_count;
    public int laugh;
    public int hooray;
    public int confused;
    public int heart;
    public int rocket;
    public int eyes;
}