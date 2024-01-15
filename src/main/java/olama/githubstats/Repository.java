package olama.githubstats;

import olama.githubstats.models.Root;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

@Component
public class Repository {

    HashMap<String , Root> rootHashMap = new HashMap<>();
    HashMap<String , File> rootFiles = new HashMap<>();


    public void add(String id , File base){
        rootFiles.put(id , base);
    }
    public Optional<File> findById(String id){
        File root = rootFiles.getOrDefault(id, null);
        if (root == null){
            return Optional.empty();
        }else {
            return Optional.of(root);
        }
    }
}
