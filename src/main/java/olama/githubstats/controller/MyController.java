package olama.githubstats.controller;

import lombok.extern.log4j.Log4j2;
import olama.githubstats.models.*;
import olama.githubstats.services.MyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
public class MyController {

    RestTemplate restTemplate = new RestTemplate();

    private final MyService myService;

    @Value("${github.token}")
    private String token;

    @Value("${github.base_address}")
    private String baseAddress;


    public MyController(MyService myService) {
        this.myService = myService;
    }

    public int extractLink(ResponseEntity response) {
        List<String> link = response.getHeaders().get("link");
        if (link == null || link.isEmpty()) {
            return 0;
        }
        int count = Utils.findCount(link.get(0));
        return count;
    }


    @GetMapping("/repos/{username}")
    public ResponseDto findStat(@PathVariable("username") String username) {

        String address = baseAddress.replace("{USER_NAME}", username);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-GitHub-Api-Version", "2022-11-28");
        headers.add("Authorization", token);
        headers.add("Accept", "application/vnd.github+json");

        HttpEntity requestEntity = new HttpEntity<>(headers);

        List<RepositoryRoot> result = new ArrayList<>();
        ResponseEntity<List<RepositoryRootDetails>> tokenResponseEntity = restTemplate.exchange(address.replace("{I}", 1 + ""), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<RepositoryRootDetails>>() {
        });
        if (tokenResponseEntity.getStatusCode() == HttpStatus.OK && tokenResponseEntity.getBody() != null) {
            for (RepositoryRootDetails repositoryRootDetails : tokenResponseEntity.getBody()) {
                result.add(new RepositoryRoot(repositoryRootDetails));
            }
        }
        int i = extractLink(tokenResponseEntity);
        for (int j = 2; j <= i; j++) {
            tokenResponseEntity = restTemplate.exchange(address.replace("{I}", j + ""), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<RepositoryRootDetails>>() {
            });
            if (tokenResponseEntity.getStatusCode() == HttpStatus.OK && tokenResponseEntity.getBody() != null) {
                for (RepositoryRootDetails repositoryRootDetails : tokenResponseEntity.getBody()) {
                    result.add(new RepositoryRoot(repositoryRootDetails));
                }
            }
        }

        List<RepositoryDTO> response = myService.createGeneralStatForRepos(result, headers, restTemplate, username);
        return Utils.createResult(response);
    }


    @GetMapping("/urls/{id}")
    public URL getUrls(@PathVariable("id") String id) throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        Optional<File> file = myService.getFile(id);
        if (file.isPresent()) {
            return file.get().toURI().toURL();
        }
        return null;
    }
}
