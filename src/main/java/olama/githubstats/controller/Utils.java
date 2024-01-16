package olama.githubstats.controller;

import olama.githubstats.models.RepositoryCommitDto;
import olama.githubstats.models.RepositoryDTO;
import olama.githubstats.models.ResponseDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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


    public static List<RepositoryDTO> sortByStarGazers(List<RepositoryDTO> list){
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getStarCounts().compareTo(o2.getStarCounts());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByContibutions(List<RepositoryDTO> list){
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getContributorCount().compareTo(o2.getContributorCount());
            }
        };
        list.sort(comparator);
        return list;
    }

    public static List<RepositoryDTO> sortByBranches(List<RepositoryDTO> list){
        Comparator<RepositoryDTO> comparator = new Comparator<RepositoryDTO>() {
            @Override
            public int compare(RepositoryDTO o1, RepositoryDTO o2) {
                return o1.getBranchesCount().compareTo(o2.getBranchesCount());
            }
        };
        list.sort(comparator);
        return list;
    }


    public static List<RepositoryCommitDto> getCommits(List<RepositoryDTO> list){
        return list.stream().map(d -> new RepositoryCommitDto(d.getName(), d.getCountCommits())).toList();
    }


    /**
     * It was more accurate to find the actual median if the size is even. But the result
     * could be a float number, thus I decided to skip this extra precision.
     */
    public static RepositoryDTO median(List<RepositoryDTO> list){
        int size = list.size();
        return list.get((int) Math.floor(size/2));
    }


    public static HashMap<String , Integer> getTotals(List<RepositoryDTO> list){
        int commits = 0;
        int stars = 0;
        int contribution = 0;
        int branches = 0;
        for (RepositoryDTO repositoryDTO : list) {
            commits += repositoryDTO.getCountCommits();
            stars += repositoryDTO.getStarCounts();
            contribution += repositoryDTO.getContributorCount();
            branches += repositoryDTO.getBranchesCount();
        }
        HashMap<String , Integer> map = new HashMap<>();
        map.put("stars" , stars);
        map.put("commits" , commits);
        map.put("contributions" , contribution);
        map.put("branches" , branches);
        return map;
    }

    public static ResponseDto createResult(List<RepositoryDTO> response) {


        ResponseDto responseDto = new ResponseDto();

        HashMap<String, Integer> totals = getTotals(response);

        responseDto.setCommitsCount(totals.get("commits"));
        responseDto.setCommmitsMedian(median(sortByCountCommit(response)).getCountCommits());
        responseDto.setCommitList(getCommits(response));

        responseDto.setStarsCount(totals.get("stars"));
        responseDto.setStarsMedian(median(sortByStarGazers(response)).getStarCounts());

        responseDto.setContibutorsCount(totals.get("contributions"));
        responseDto.setContributorsMedian(median(sortByContibutions(response)).getContributorCount());

        responseDto.setBranchCount(totals.get("branches"));
        responseDto.setBranchMedian(median(sortByBranches(response)).getBranchesCount());

        return responseDto;
    }
}
