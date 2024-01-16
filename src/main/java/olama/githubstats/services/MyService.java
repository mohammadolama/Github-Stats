package olama.githubstats.services;

import lombok.extern.log4j.Log4j2;
import olama.githubstats.controller.Utils;
import olama.githubstats.models.*;
import olama.githubstats.repository.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@Log4j2
public class MyService {

    private final Repository repository;


    @Value("${github.environment_address}")
    private String envAddress;

    @Value("${github.deployment_address}")
    private String deploymentAddress;

    @Value("${github.issues_address}")
    private String issuesAddress;

    private static final String PER_PAGE = "?per_page=1";

    public MyService(Repository repository) {
        this.repository = repository;
    }

    /**
     * this function will make an API call to calculate the total count of branches in a single repository.
     */
    private void findCountOfBranches(String baseUrl,
                                     HttpEntity<?> requestEntity,
                                     RestTemplate restTemplate,
                                     RepositoryDTO repositoryDTO,
                                     RepositoryRoot repositoryRoot) {
        String url = baseUrl.replace("{/branch}", PER_PAGE);
        ResponseEntity<List<Branch>> response = restTemplate.
                exchange(url,
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("branch: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);
            repositoryDTO.setBranchesCount(count);
            repositoryRoot.setBranches(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of commits in a single repository.
     */
    private void findCountOfCommits(String baseUrl,
                                    HttpEntity<?> requestEntity,
                                    RestTemplate restTemplate,
                                    RepositoryDTO repositoryDTO,
                                    RepositoryRoot repositoryRoot) {
        String url = baseUrl.replace("{/sha}", PER_PAGE);
        ResponseEntity<List<Commit>> response = restTemplate.
                exchange(url,
                        HttpMethod.GET,
                        requestEntity,
                        new ParameterizedTypeReference<>() {
                        });
        log.info("commits: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);

            repositoryDTO.setCountCommits(count);
            repositoryRoot.setCommits(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of contributors in a single repository.
     */
    private void findCountOfContributions(String baseUrl,
                                          HttpEntity<?> requestEntity,
                                          RestTemplate restTemplate,
                                          RepositoryDTO repositoryDTO,
                                          RepositoryRoot repositoryRoot) {
        String url = baseUrl + PER_PAGE;
        ResponseEntity<List<Contributor>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("contributions: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);
            repositoryDTO.setContributorCount(count);
            repositoryRoot.setContributors(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of tags in a single repository.
     */
    private void findCountOfTags(String baseUrl,
                                 HttpEntity<?> requestEntity,
                                 RestTemplate restTemplate,
                                 RepositoryDTO repositoryDTO,
                                 RepositoryRoot repositoryRoot) {
        String url = baseUrl + PER_PAGE;
        ResponseEntity<List<Tags>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("tags: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);
            repositoryDTO.setTagsCount(count);
            repositoryRoot.setTags(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of releases in a single repository.
     */
    private void findCountOfReleases(String baseUrl,
                                     HttpEntity<?> requestEntity,
                                     RestTemplate restTemplate,
                                     RepositoryDTO repositoryDTO,
                                     RepositoryRoot repositoryRoot) {
        String url = baseUrl.replace("{/id}", "") + PER_PAGE;
        ResponseEntity<List<Release>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("releases: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);
            repositoryDTO.setReleasesCount(count);
            repositoryRoot.setReleases(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of lines written in a single repository.
     */
    private void findCountOfLanguages(String baseUrl,
                                      HttpEntity<?> requestEntity,
                                      RestTemplate restTemplate,
                                      RepositoryDTO repositoryDTO,
                                      RepositoryRoot repositoryRoot) {
        ResponseEntity<HashMap<String, Integer>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("languages: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            repositoryDTO.setLanguages(response.getBody());
            repositoryRoot.setLanguages(response.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of environments and deployments in a single repository.
     */
    private void findCountOfEnvs(String username,
                                 String repoName,
                                 HttpEntity<?> requestEntity,
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
        ResponseEntity<List<Deployments>> response2 = restTemplate.exchange(deploymentUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("deployments: " + response2.getStatusCode());
        if (response2.getStatusCode() == HttpStatus.OK && response2.getBody() != null) {
            repositoryDTO.setDeployments(response2.getBody().size());
            repositoryRoot.setDeployments(response2.getBody());
        }
    }

    /**
     * this function will make an API call to calculate the total count of closed issues in a single repository.
     *
     * IMPORTANT: GitHub API will consider pull requests as closed issues either. So the number counted in this function
     * is not accurate. To have an accurate response, we should make an API call for each issue separately and filter \
     * issues where 'PullRequest' field in the Issue is null.(Thus it is a closed issue.)
     */
    private void findCountOfClosedIssues(String username,
                                         String repoName,
                                         HttpEntity<?> requestEntity,
                                         RestTemplate restTemplate,
                                         RepositoryDTO repositoryDTO,
                                         RepositoryRoot repositoryRoot) {
        String issueUrl = issuesAddress.replace("{USER_NAME}", username).replace("{REPO_NAME}", repoName);
        ResponseEntity<List<Issue>> response = restTemplate.exchange(issueUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<>() {
        });
        log.info("issues: " + response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            int count = Utils.extractLink(response);
            repositoryDTO.setClosedIssues(count);
            repositoryRoot.setIssues(response.getBody());
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


                HttpEntity<?> requestEntity = new HttpEntity<>(headers);

                findCountOfCommits(details.getCommits_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfContributions(details.getContributors_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfBranches(details.getBranches_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfTags(details.getTags_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfReleases(details.getReleases_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfLanguages(details.getLanguages_url(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);

                findCountOfEnvs(username, details.getName(), requestEntity, restTemplate, repositoryDTO, repositoryRoot);
                findCountOfClosedIssues(username, details.getName(), requestEntity, restTemplate, repositoryDTO , repositoryRoot );

                log.info(repositoryDTO.toString());
                result.add(repositoryDTO);
            } catch (Exception e) {
                log.debug(e.getStackTrace());
            }
        }
        repository.save(list, username);
        return result;

    }
}
