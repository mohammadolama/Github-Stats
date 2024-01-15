package olama.githubstats.controller;

import lombok.extern.log4j.Log4j2;
import olama.githubstats.models.*;
import olama.githubstats.services.MyService;
import org.springframework.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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


    @GetMapping("/repos/{username}")
    public ResponseDto findStat(@PathVariable("username") String username) {

        String address = baseAddress.replace("{USER_NAME}" , username);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-GitHub-Api-Version" , "2022-11-28");
        headers.add("Authorization" , token);
        headers.add("Accept" , "application/vnd.github+json");

        HttpEntity requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Root>> tokenResponseEntity = restTemplate.exchange(address, HttpMethod.GET, requestEntity ,new ParameterizedTypeReference<List<Root>>(){});

        List<Root> body = tokenResponseEntity.getBody();
        List<RepositoryDTO> response = myService.createGeneralStatForRepos(body , headers , restTemplate);

        return Utils.createResult(response);


    }


    @GetMapping("/urls/{id}")
    public URL getUrls(@PathVariable("id") String id) throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        Optional<File> file = myService.getFile(id);
        if (file.isPresent()){
            return file.get().toURI().toURL();
        }
        return null;
    }
}
