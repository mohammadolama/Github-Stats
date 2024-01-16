package olama.githubstats.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import olama.githubstats.Repository;
import olama.githubstats.models.*;
import org.apache.commons.lang3.SystemUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static olama.githubstats.controller.Utils.findCount;

@Service
@Log4j2
public class MyService {

    private final Repository repository;


    private ObjectMapper objectMapper;

    @Value("${github.environment_address}")
    private String envAddress;

    @Value("${github.deployment_address}")
    private String deploymentAddress;

    public MyService(Repository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    private void findCountOfBranches(String baseUrl, HttpEntity requestEntity, RestTemplate restTemplate, RepositoryDTO repositoryDTO) {
        String url = baseUrl.replace("{/branch}", "?per_page=1");
        ResponseEntity<List<Branch>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Branch>>() {
        });
        log.info("branch: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setBranchesCount(count);
        }
    }

    private void findCountOfCommits(String baseUrl, HttpEntity requestEntity, RestTemplate restTemplate, RepositoryDTO repositoryDTO) {
        String url = baseUrl.replace("{/sha}", "?per_page=1");
        ResponseEntity<List<CommitRoot>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<CommitRoot>>() {
        });
        log.info("commits: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setCountCommits(count);
        }
    }

    private void findCountOfContributions(String baseUrl, HttpEntity requestEntity, RestTemplate restTemplate, RepositoryDTO repositoryDTO) {
        String url = baseUrl + "?per_page=1";
        ResponseEntity<List<Contributor>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Contributor>>() {
        });
        log.info("contributions: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setContributorCount(count);
        }
    }

    private void findCountOfTags(String baseUrl, HttpEntity requestEntity, RestTemplate restTemplate, RepositoryDTO repositoryDTO) {
        String url = baseUrl + "?per_page=1";
        ResponseEntity<List<Tags>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Tags>>() {
        });
        log.info("tags: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setTagsCount(count);
        }
    }

    private void findCountOfReleases(String baseUrl, HttpEntity requestEntity, RestTemplate restTemplate, RepositoryDTO repositoryDTO) {
        String url = baseUrl.replace("{/id}", "") + "?per_page=1";
        ResponseEntity<List<Release>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Release>>() {
        });
        log.info("releases: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setReleasesCount(count);
        }
    }

    private void findCountOfLanguages(String baseUrl,
                                      HttpEntity requestEntity,
                                      RestTemplate restTemplate,
                                      RepositoryDTO repositoryDTO) {
        String url = baseUrl;
        ResponseEntity<HashMap<String, Integer>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<HashMap<String, Integer>>() {
        });
        log.info("releases: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            repositoryDTO.setLanguages(response.getBody());
        }
    }

    private void findCountOfEnvs(String username,
                                 String repoName,
                                 HttpEntity requestEntity,
                                 RestTemplate restTemplate,
                                 RepositoryDTO repositoryDTO) {
        String environmentsUrl = envAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        String deploymentUrl = deploymentAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        ResponseEntity<Environments> response = restTemplate.exchange(environmentsUrl, HttpMethod.GET, requestEntity, Environments.class);
        log.info("envs: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            repositoryDTO.setEnvironments(response.getBody().getTotal_count());
        }
        ResponseEntity<List<Deployments>> response2 = restTemplate.exchange(deploymentUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Deployments>>() {
        });
        log.info("deployments: " + response2.getStatusCode());
        if (response2.getStatusCode() == HttpStatus.OK && response2.getBody() != null) {
            repositoryDTO.setDeployments(response2.getBody().size());
        }
    }


    public List<RepositoryDTO> createGeneralStatForRepos(List<Root> list,
                                                         HttpHeaders headers,
                                                         RestTemplate restTemplate,
                                                         String username) {

        List<RepositoryDTO> result = new ArrayList<>();

        for (Root root : list) {
            try {
                RepositoryDTO repositoryDTO = new RepositoryDTO();
                repositoryDTO.setName(root.getName());
                repositoryDTO.setStarCounts(root.getStargazers_count());
                repositoryDTO.setForksCount(root.getForks_count());


                HttpEntity requestEntity = new HttpEntity<>(headers);

                findCountOfCommits(root.getCommits_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfContributions(root.getContributors_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfBranches(root.getBranches_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfTags(root.getTags_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfReleases(root.getReleases_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfLanguages(root.getLanguages_url(), requestEntity, restTemplate, repositoryDTO);

                findCountOfEnvs(username, root.getName(), requestEntity, restTemplate, repositoryDTO);

                log.info(repositoryDTO.toString());
                result.add(repositoryDTO);
            } catch (Exception e) {
                e.printStackTrace();
//                log.debug(e);
            }
        }

        return result;

    }



    protected String createPath(String st) {
        String path = st;
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.substring(1);
        }
        return path;
    }

    public Optional<File> getFile(String id) {
        return repository.findById(id);
    }

}
