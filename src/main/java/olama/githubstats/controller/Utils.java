package olama.githubstats.controller;

import olama.githubstats.models.Language;
import olama.githubstats.models.RepositoryCommitDto;
import olama.githubstats.models.RepositoryDTO;
import olama.githubstats.models.ResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * This class is used for some utility function like sorting or creating the response dto.
 */
public class Utils {

    public static List<RepositoryDTO> sortByCountCommit(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getCountCommits);

        list.sort(comparator);
        return list;
    }


    public static List<RepositoryDTO> sortByStarGazers(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getStarCounts);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByContibutions(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getContributorCount);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByBranches(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getBranchesCount);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByForks(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getForksCount);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByTags(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getTagsCount);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByRelease(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getReleasesCount);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByDeployments(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getDeployments);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByEnvironments(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getEnvironments);
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByIssues(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = Comparator.comparing(RepositoryDTO::getClosedIssues);
        list.sort(comparator);
        return list;
    }


    /**
     * It was more accurate to find the actual median if the size is even. But the result
     * could be a float number, thus I decided to skip this extra precision of keeping the median
     * as the mean of two middle numbers.
     */
    public static RepositoryDTO median(List<RepositoryDTO> list) {
        int size = list.size();
        return list.get((int) Math.floor(size / 2));
    }


    /**
     * this function will count the total number of each item to produce the response
     */
    public static HashMap<String, Integer> getTotals(List<RepositoryDTO> list) {
        int commits = 0;
        int stars = 0;
        int contribution = 0;
        int branches = 0;
        int tags = 0;
        int forks = 0;
        int release = 0;
        int deployments = 0;
        int environments = 0;
        int issues = 0;

        for (RepositoryDTO repositoryDTO : list) {
            commits += repositoryDTO.getCountCommits();
            stars += repositoryDTO.getStarCounts();
            contribution += repositoryDTO.getContributorCount();
            branches += repositoryDTO.getBranchesCount();
            tags += repositoryDTO.getTagsCount();
            forks += repositoryDTO.getForksCount();
            release += repositoryDTO.getReleasesCount();
            deployments += repositoryDTO.getDeployments();
            environments += repositoryDTO.getEnvironments();
            issues += repositoryDTO.getClosedIssues();
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("stars", stars);
        map.put("commits", commits);
        map.put("contributions", contribution);
        map.put("branches", branches);
        map.put("tags", tags);
        map.put("forks", forks);
        map.put("release", release);
        map.put("environments", environments);
        map.put("deployments", deployments);
        map.put("issues", issues);
        return map;
    }

    /**
     * this function will be used to calculate the total count and median of each programming language in all repositories
     */
    private static Map<String, Language> languages(List<RepositoryDTO> repositoryDTO) {
        Map<String, List<Integer>> map = new HashMap<>();
        Map<String, Language> result = new HashMap<>();
        for (RepositoryDTO dto : repositoryDTO) {

            for (Map.Entry<String, Integer> entry : dto.getLanguages().entrySet()) {
                if (!map.containsKey(entry.getKey())) {
                    map.put(entry.getKey(), new ArrayList<>());
                }
                map.get(entry.getKey()).add(entry.getValue());
            }

            for (Map.Entry<String, List<Integer>> ent : map.entrySet()) {
                List<Integer> value = ent.getValue();
                Collections.sort(value);
                Language language = new Language();
                language.setName(ent.getKey());
                int sum = value.stream().mapToInt(Integer::intValue).sum();
                language.setTotal(sum);
                language.setMedian(value.get(value.size() / 2));
                result.put(ent.getKey(), language);
            }
        }
        return result;
    }

    /**
     * this function is used to create the desired response.
     */
    public static ResponseDto createResult(List<RepositoryDTO> response) {


        ResponseDto responseDto = new ResponseDto();

        HashMap<String, Integer> totals = getTotals(response);

        responseDto.setCommitsCount(totals.get("commits"));
        responseDto.setCommitsMedian(median(sortByCountCommit(response)).getCountCommits());
        responseDto.setCommits(response.stream().map(RepositoryDTO::getCountCommits).toList());

        responseDto.setStarsCount(totals.get("stars"));
        responseDto.setStarsMedian(median(sortByStarGazers(response)).getStarCounts());
        responseDto.setStars(response.stream().map(RepositoryDTO::getStarCounts).toList());

        responseDto.setContributorsCount(totals.get("contributions"));
        responseDto.setContributorsMedian(median(sortByContibutions(response)).getContributorCount());
        responseDto.setContributors(response.stream().map(RepositoryDTO::getContributorCount).toList());

        responseDto.setBranchCount(totals.get("branches"));
        responseDto.setBranchMedian(median(sortByBranches(response)).getBranchesCount());
        responseDto.setBranches(response.stream().map(RepositoryDTO::getBranchesCount).toList());

        responseDto.setForksCount(totals.get("forks"));
        responseDto.setForksMedian(median(sortByForks(response)).getForksCount());
        responseDto.setForks(response.stream().map(RepositoryDTO::getForksCount).toList());

        responseDto.setTagsCount(totals.get("tags"));
        responseDto.setTagsMedian(median(sortByTags(response)).getTagsCount());
        responseDto.setTags(response.stream().map(RepositoryDTO::getTagsCount).toList());

        responseDto.setReleasesCount(totals.get("release"));
        responseDto.setReleasesMedian(median(sortByRelease(response)).getReleasesCount());
        responseDto.setReleases(response.stream().map(RepositoryDTO::getReleasesCount).toList());


        responseDto.setDeploymentsCount(totals.get("deployments"));
        responseDto.setDeploymentsMedian(median(sortByDeployments(response)).getDeployments());
        responseDto.setDeployments(response.stream().map(RepositoryDTO::getDeployments).toList());


        responseDto.setEnvironmentsCount(totals.get("environments"));
        responseDto.setEnvironmentsMedian(median(sortByEnvironments(response)).getEnvironments());
        responseDto.setEnvironments(response.stream().map(RepositoryDTO::getEnvironments).toList());


        responseDto.setClosedIssuesCount(totals.get("issues"));
        responseDto.setClosedIssuesMedian(median(sortByIssues(response)).getClosedIssues());
        responseDto.setClosedIssues(response.stream().map(RepositoryDTO::getClosedIssues).toList());

        responseDto.setLanguages(languages(response));

        responseDto.setRepositoryCounts(response.size());

        return responseDto;
    }

    /**
     * this function is used to find the total number of items in 1 response.
     * if we set the query paramether to per_page =1 , then the result will contain a 'link' header
     * which contains a link to the next page and the last page. the last page number is considered as the total count.
     */
    public static int extractLink(ResponseEntity<?> response) {
        try {
            List<String> link = response.getHeaders().get("link");
            if (link == null || link.isEmpty()) {
                return 0;
            }
            return findCount(link.get(0));
        }catch (Exception e){
            return 0;
        }
    }

    private static int findCount(String s) {
        if (s == null)
            return 0;
        String[] split = s.split(",");
        String s1 = split[1];
        String substring = s1.substring(s1.indexOf("&page=") + 6, s1.indexOf(">"));
        return Integer.parseInt(substring);
    }

}
