package olama.githubstats.controller;

import olama.githubstats.models.Language;
import olama.githubstats.models.RepositoryCommitDto;
import olama.githubstats.models.RepositoryDTO;
import olama.githubstats.models.ResponseDto;

import java.util.*;

public class Utils {

    public static List<RepositoryDTO> sortByCountCommit(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getCountCommits().compareTo(o2.getCountCommits());
            }
        };

        list.sort(comparator);
        return list;
    }


    public static List<RepositoryDTO> sortByStarGazers(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getStarCounts().compareTo(o2.getStarCounts());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByContibutions(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getContributorCount().compareTo(o2.getContributorCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByBranches(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getBranchesCount().compareTo(o2.getBranchesCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByForks(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getForksCount().compareTo(o2.getForksCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByTags(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getTagsCount().compareTo(o2.getTagsCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByRelease(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getReleasesCount().compareTo(o2.getReleasesCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByDeployments(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getDeployments().compareTo(o2.getDeployments());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByEnvironments(List<RepositoryDTO> list) {
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getEnvironments().compareTo(o2.getEnvironments());
            }
        };
        list.sort(comparator);
        return list;
    }


    public static List<RepositoryCommitDto> getCommits(List<RepositoryDTO> list) {
        return list.stream().map(d -> new RepositoryCommitDto(d.getName(), d.getCountCommits())).toList();
    }


    /**
     * It was more accurate to find the actual median if the size is even. But the result
     * could be a float number, thus I decided to skip this extra precision.
     */
    public static RepositoryDTO median(List<RepositoryDTO> list) {
        int size = list.size();
        return list.get((int) Math.floor(size / 2));
    }


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
        return map;
    }

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

    public static ResponseDto createResult(List<RepositoryDTO> response) {


        ResponseDto responseDto = new ResponseDto();

        HashMap<String, Integer> totals = getTotals(response);

        responseDto.setCommitsCount(totals.get("commits"));
        responseDto.setCommitsMedian(median(sortByCountCommit(response)).getCountCommits());
        responseDto.setCommitList(getCommits(response));

        responseDto.setStarsCount(totals.get("stars"));
        responseDto.setStarsMedian(median(sortByStarGazers(response)).getStarCounts());

        responseDto.setContributorsCount(totals.get("contributions"));
        responseDto.setContributorsMedian(median(sortByContibutions(response)).getContributorCount());

        responseDto.setBranchCount(totals.get("branches"));
        responseDto.setBranchMedian(median(sortByBranches(response)).getBranchesCount());

        responseDto.setForksCount(totals.get("forks"));
        responseDto.setForksMedian(median(sortByForks(response)).getForksCount());

        responseDto.setForksCount(totals.get("tags"));
        responseDto.setForksMedian(median(sortByTags(response)).getTagsCount());

        responseDto.setReleasesCount(totals.get("release"));
        responseDto.setReleasesMedian(median(sortByBranches(response)).getReleasesCount());

        responseDto.setDeploymentsCount(totals.get("deployments"));
        responseDto.setDeploymentsMedian(median(sortByDeployments(response)).getDeployments());

        responseDto.setEnvironmentsCount(totals.get("environments"));
        responseDto.setEnvironmentsMedian(median(sortByEnvironments(response)).getEnvironments());


        responseDto.setLanguages(languages(response));

        return responseDto;
    }

    public static int findCount(String s) {
        String[] split = s.split(",");
        String s1 = split[1];
        String substring = s1.substring(s1.indexOf("&page=") + 6, s1.indexOf(">"));
        return Integer.valueOf(substring);
    }

}
