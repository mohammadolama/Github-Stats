package olama.githubstats.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import olama.githubstats.Repository;
import olama.githubstats.models.*;
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

    private void findCountOfBranches(String baseUrl , HttpEntity requestEntity  , RestTemplate restTemplate , RepositoryDTO repositoryDTO){
        String url = baseUrl.replace("{/branch}", "?per_page=1");
        ResponseEntity<List<Branch>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Branch>>() {});
            log.info("branch: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() !=null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            }catch (Exception e){
                count = response.getBody().size();
            }
            repositoryDTO.setBranchesCount(count);
        }
    }

    private void findCountOfCommits(String baseUrl , HttpEntity requestEntity  , RestTemplate restTemplate , RepositoryDTO repositoryDTO){
        String url = baseUrl.replace("{/sha}", "?per_page=1");
        ResponseEntity<List<CommitRoot>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<CommitRoot>>() {});
        log.info("commits: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() !=null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            }catch (Exception e){
                count = response.getBody().size();
            }
            repositoryDTO.setCountCommits(count);
        }
    }

    private void findCountOfContributions(String baseUrl , HttpEntity requestEntity  , RestTemplate restTemplate , RepositoryDTO repositoryDTO){
        String url = baseUrl + "?per_page=1";
        ResponseEntity<List<Contributor>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Contributor>>() {});
        log.info("contributions: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() !=null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            }catch (Exception e){
                count = response.getBody().size();
            }
            repositoryDTO.setContributorCount(count);
        }
    }


    public List<RepositoryDTO> createGeneralStatForRepos(List<Root> list ,HttpHeaders headers, RestTemplate restTemplate){

        List<RepositoryDTO> result = new ArrayList<>();

        for (Root root : list) {
            try {
                RepositoryDTO repositoryDTO = new RepositoryDTO();
                repositoryDTO.setName(root.getName());
                repositoryDTO.setStarCounts(root.getStargazers_count());

                HttpEntity requestEntity = new HttpEntity<>(headers);

                findCountOfCommits(root.getCommits_url() , requestEntity , restTemplate , repositoryDTO);

                findCountOfContributions(root.getContributors_url()  , requestEntity , restTemplate , repositoryDTO);

                findCountOfBranches(root.getBranches_url() , requestEntity , restTemplate , repositoryDTO);

                log.info(repositoryDTO.toString());
                result.add(repositoryDTO);
            }catch (Exception e){
                log.debug(e);
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
