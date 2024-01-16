package olama.githubstats.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import olama.githubstats.repository.Repository;
import olama.githubstats.models.*;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
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

    @Value("${github.issues_address}")
    private String issuesAddress;

    public MyService(Repository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    private void findCountOfBranches(String baseUrl,
                                     HttpEntity requestEntity,
                                     RestTemplate restTemplate,
                                     RepositoryDTO repositoryDTO,
                                     RepositoryRoot repositoryRoot) {
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
            repositoryRoot.setBranches(response.getBody());
        }
    }

    private void findCountOfCommits(String baseUrl,
                                    HttpEntity requestEntity,
                                    RestTemplate restTemplate,
                                    RepositoryDTO repositoryDTO,
                                    RepositoryRoot repositoryRoot) {
        String url = baseUrl.replace("{/sha}", "?per_page=1");
        ResponseEntity<List<Commit>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Commit>>() {
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
            repositoryRoot.setCommits(response.getBody());
        }
    }

    private void findCountOfContributions(String baseUrl,
                                          HttpEntity requestEntity,
                                          RestTemplate restTemplate,
                                          RepositoryDTO repositoryDTO,
                                          RepositoryRoot repositoryRoot) {
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
            repositoryRoot.setContributors(response.getBody());
        }
    }

    private void findCountOfTags(String baseUrl,
                                 HttpEntity requestEntity,
                                 RestTemplate restTemplate,
                                 RepositoryDTO repositoryDTO,
                                 RepositoryRoot repositoryRoot) {
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
            repositoryRoot.setTags(response.getBody());
        }
    }

    private void findCountOfReleases(String baseUrl,
                                     HttpEntity requestEntity,
                                     RestTemplate restTemplate,
                                     RepositoryDTO repositoryDTO,
                                     RepositoryRoot repositoryRoot) {
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
            repositoryRoot.setReleases(response.getBody());
        }
    }

    private void findCountOfLanguages(String baseUrl,
                                      HttpEntity requestEntity,
                                      RestTemplate restTemplate,
                                      RepositoryDTO repositoryDTO,
                                      RepositoryRoot repositoryRoot) {
        String url = baseUrl;
        ResponseEntity<HashMap<String, Integer>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<HashMap<String, Integer>>() {
        });
        log.info("releases: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            repositoryDTO.setLanguages(response.getBody());
            repositoryRoot.setLanguages(response.getBody());
        }
    }

    private void findCountOfEnvs(String username,
                                 String repoName,
                                 HttpEntity requestEntity,
                                 RestTemplate restTemplate,
                                 RepositoryDTO repositoryDTO,
                                 RepositoryRoot repositoryRoot) {
        String environmentsUrl = envAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        String deploymentUrl = deploymentAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        ResponseEntity<Environments> response = restTemplate.exchange(environmentsUrl, HttpMethod.GET, requestEntity, Environments.class);
        log.info("envs: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            repositoryDTO.setEnvironments(response.getBody().getTotal_count());
            repositoryRoot.setEnvironments(response.getBody());
        }
        ResponseEntity<List<Deployments>> response2 = restTemplate.exchange(deploymentUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Deployments>>() {
        });
        log.info("deployments: " + response2.getStatusCode());
        if (response2.getStatusCode() == HttpStatus.OK && response2.getBody() != null) {
            repositoryDTO.setDeployments(response2.getBody().size());
            repositoryRoot.setDeployments(response2.getBody());
        }
    }

    private void findCountOfClosedIssues(String username,
                                         String repoName,
                                         HttpEntity requestEntity,
                                         RestTemplate restTemplate,
                                         RepositoryDTO repositoryDTO,
                                         RepositoryRoot repositoryRoot) {
        String issueUrl = issuesAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        ResponseEntity<List<Issue>> response = restTemplate.exchange(issueUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Issue>>() {
        });
        log.info("issues: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = 0;
            try {
                count = findCount(response.getHeaders().get("link").get(0));
            } catch (Exception e) {
                count = response.getBody().size();
            }
            repositoryDTO.setClosedIssues(count);
        }
    }


    public List<RepositoryDTO> createGeneralStatForRepos(List<RepositoryRoot> list,
                                                         HttpHeaders headers,
                                                         RestTemplate restTemplate,
                                                         String username) {

        List<RepositoryDTO> result = new ArrayList<>();

        for (RepositoryRoot repositoryRoot : list) {
            RepositoryRootDetails details = repositoryRoot.getDetails();
            try {
                RepositoryDTO repositoryDTO = new RepositoryDTO();
                repositoryDTO.setName(details.getName());
                repositoryDTO.setStarCounts(details.getStargazers_count());
                repositoryDTO.setForksCount(details.getForks_count());


                HttpEntity requestEntity = new HttpEntity<>(headers);

                findCountOfCommits(details.getCommits_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfContributions(details.getContributors_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfBranches(details.getBranches_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfTags(details.getTags_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfReleases(details.getReleases_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfLanguages(details.getLanguages_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfEnvs(username, details.getName(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);
                findCountOfClosedIssues(username, details.getName(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                log.info(repositoryDTO.toString());
                result.add(repositoryDTO);
            } catch (Exception e) {
                e.printStackTrace();
//                log.debug(e);
            }
        }
        repository.save(list, username);
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
