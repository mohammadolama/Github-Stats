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

    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * this function will create a json object for each user. it will save the downloaded files in a json file.
     */
    public boolean save(List<RepositoryRoot> list, String username) {

        try {
            User user = new User(list.size(), list);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File file = new File("src/main/resources/" + String.format("result_{%s}.json", username));
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(objectMapper.writeValueAsString(user));
            printWriter.flush();
            printWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

