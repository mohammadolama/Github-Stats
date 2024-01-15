package olama.githubstats.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import olama.githubstats.Repository;
import olama.githubstats.models.CommitRoot;
import olama.githubstats.models.RepositoryDTO;
import olama.githubstats.models.Root;
import org.apache.commons.lang3.SystemUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class MyService {

    private final Repository repository;



    private ObjectMapper objectMapper;
    public MyService(Repository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }


    public List<RepositoryDTO> createGeneralStatForRepos(List<Root> list ,HttpHeaders headers, RestTemplate restTemplate){

        List<RepositoryDTO> result = new ArrayList<>();

        for (Root root : list) {
            try {
                String commitsUrl = root.getCommits_url().replace("{/sha}", "?per_page=1");
                log.info(commitsUrl);

                HttpEntity requestEntity = new HttpEntity<>(headers);
                ResponseEntity<List<CommitRoot>> tokenResponseEntity = restTemplate.exchange(commitsUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<CommitRoot>>() {
                });
                if (tokenResponseEntity.getStatusCode() == HttpStatus.OK && tokenResponseEntity.getBody() !=null) {
                    log.info(tokenResponseEntity.getStatusCode());
                    int count = findCount(tokenResponseEntity.getHeaders().get("link").get(0));
                    log.info(root.getName() + " : count=" + count + " , star=" + root.getStargazers_count());
                    result.add(new RepositoryDTO(root.getName() , count ,  root.getStargazers_count()));
                }
            }catch (Exception ignore){

            }
        }

        return result;

    }


    private int findCount(String s){
        String[] split = s.split(",");
        String s1 = split[1];
        String substring = s1.substring(s1.indexOf("&page=")+6, s1.indexOf(">"));
        return Integer.valueOf(substring);
    }

    protected String createPath(String st){
        String path = st;
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.substring(1);
        }
        return path;
    }

    public Optional<File> getFile(String id){
        return repository.findById(id);
    }

}
