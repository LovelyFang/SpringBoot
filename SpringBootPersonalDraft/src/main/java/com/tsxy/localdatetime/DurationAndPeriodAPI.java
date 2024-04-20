package com.tsxy.localdatetime;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @Author Liu_df
 * @Date 2023/4/16 10:39
 */
public class DurationAndPeriodAPI {

    public static void main(String[] args) {
        DurationAndPeriodAPI durationAndPeriodAPI = new DurationAndPeriodAPI();
        durationAndPeriodAPI.DurationAPI("2023-04-16", "2024-04-16");
        durationAndPeriodAPI.DurationAPI("2023-01-16", "2024-06-16");
        List<String> string = new ArrayList<>(Arrays.asList("1", "4", "9"));
        List<List<String>> subsets = durationAndPeriodAPI.subsets(string);
        for (List<String> subset : subsets) {
            System.out.print("{");
            String str = String.join(",", subset);
            System.out.print(str);
            System.out.print("}");
        }

    }

    private List<List<String>> subsets(List<String> subset) {
        if (subset.isEmpty()) {
            List<List<String>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }
        String first = subset.get(0);
        List<String> subList = subset.subList(1, subset.size());
        List<List<String>> subsets = subsets(subList);
        List<List<String>> lists = insertAll(first, subsets);
        return concat(subsets, lists);


    }

    private List<List<String>> concat(List<List<String>> subsets, List<List<String>> lists) {
        List<List<String>> ans = new ArrayList<>(subsets);
        ans.addAll(lists);
        return ans;

    }

    private List<List<String>> insertAll(String first, List<List<String>> subsets) {
        List<List<String>> ans = new ArrayList<>();
        for (List<String> subset : subsets) {
            List<String> subInteger = new ArrayList<>();
            subInteger.add(first);
            subInteger.addAll(subset);
            ans.add(subInteger);
        }
        return ans;
    }


    private void DurationAPI(String startTime, String endTime){

        LocalDate startLocalDateTime = LocalDate.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate endLocalDateTime = LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Period between = Period.between(startLocalDateTime, endLocalDateTime);
        System.out.println(between.get(ChronoUnit.DAYS));
    }

}
