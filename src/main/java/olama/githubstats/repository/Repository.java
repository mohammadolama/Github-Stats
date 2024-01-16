package olama.githubstats.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import olama.githubstats.models.RepositoryRoot;
import olama.githubstats.models.RepositoryRootDetails;
import olama.githubstats.models.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class Repository {

    HashMap<String, RepositoryRootDetails> rootHashMap = new HashMap<>();
    HashMap<String, File> rootFiles = new HashMap<>();

    ObjectMapper objectMapper = new ObjectMapper();


    public void add(String id, File base) {
        rootFiles.put(id, base);
    }

    public Optional<File> findById(String id) {
        File root = rootFiles.getOrDefault(id, null);
        if (root == null) {
            return Optional.empty();
        } else {
            return Optional.of(root);
        }
    }

    public boolean save(List<RepositoryRoot> list , String username) {

        try {
            User user = new User(list.size() , list);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = new File("src/main/resources/" +  String.format("result_{%s}.json" , username));
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(objectMapper.writeValueAsString(user));
            printWriter.flush();
            printWriter.close();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

