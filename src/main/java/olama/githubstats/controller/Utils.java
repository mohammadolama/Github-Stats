package olama.githubstats.controller;

import olama.githubstats.models.RepositoryCommitDto;
import olama.githubstats.models.RepositoryDTO;
import olama.githubstats.models.ResponseDto;

import java.util.Comparator;
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
    /**
     * It was more accurate to find the actual median if the size is even. But the result
     * could be a float number, thus I decided to skip this extra precision.
     */
    public static Integer findMedianCommit(List<RepositoryDTO> list){
        int size = list.size();
        return list.get((int) Math.floor(size/2)).getCountCommits();
    }

    public static Integer findTotalCommits(List<RepositoryDTO> list){
        int res = 0;
        for (RepositoryDTO repositoryDTO : list) {
            res += repositoryDTO.getCountCommits();
        }
        return res;
    }

    public static List<RepositoryCommitDto> getCommits(List<RepositoryDTO> list){
        return list.stream().map(d -> new RepositoryCommitDto(d.getName(), d.getCountCommits())).toList();
    }


    public static Integer findMedianStars(List<RepositoryDTO> list){
        int size = list.size();
        return list.get((int) Math.floor(size/2)).getStarCounts();
    }

    public static Integer findTotalStars(List<RepositoryDTO> list){
        int res = 0;
        for (RepositoryDTO repositoryDTO : list) {
            res += repositoryDTO.getStarCounts();
        }
        return res;
    }

    public static ResponseDto createResult(List<RepositoryDTO> response) {

        List<RepositoryDTO> repositoryDTOS = sortByCountCommit(response);

        int totalCommits = findTotalCommits(repositoryDTOS);
        int medianCommits = findMedianCommit(repositoryDTOS);
        List<RepositoryCommitDto> commits = Utils.getCommits(repositoryDTOS);


        List<RepositoryDTO> repositoryDTOS1 = sortByStarGazers(response);
        int totalStars = findTotalStars(repositoryDTOS1);
        int medianStars = findMedianStars(repositoryDTOS1);


        return new ResponseDto(totalCommits , medianCommits , commits , totalStars , medianStars);
    }
}
