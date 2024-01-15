package olama.githubstats.controller;

import lombok.extern.log4j.Log4j2;
import olama.githubstats.models.Root;
import olama.githubstats.models.RootDto;
import org.springframework.asm.TypeReference;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@Log4j2
public class MyController {

    RestTemplate restTemplate = new RestTemplate();




    @GetMapping("/stat")
    public List<RootDto> findStat(){

        String address = "https://api.github.com/users/mohammadolama/repos";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-GitHub-Api-Version" , "2022-11-28");
        headers.add("Authorization" , "Bearer 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");
        headers.add("Accept" , "application/vnd.github+json");

        HttpEntity requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Root>> tokenResponseEntity = restTemplate.exchange(address, HttpMethod.GET, requestEntity ,new ParameterizedTypeReference<List<Root>>(){});


        log.info(tokenResponseEntity.getStatusCode());
        log.info(tokenResponseEntity.getBody().toString());
        List<RootDto> list1 = tokenResponseEntity.getBody().stream().map(r -> new RootDto(r.getId(), r.getName())).toList();
        return list1;

    }
}
