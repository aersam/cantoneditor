package ch.fhnw.cantoneditor.datautils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher<T extends Searchable> implements Runnable {

    private final Iterable<T> sourcelist;
    private final String searchText;
    private Iterable<T> result;

    public Searcher(String searchText, Iterable<T> sourcelist) {
        this.sourcelist = sourcelist;
        this.searchText = searchText;
    }

    @Override
    public void run() {
        this.result = filterList(searchText, this.sourcelist);

    }

    public Iterable<T> getResult() {
        return this.result;
    }

    /** Helper method for levenshtein */

    public static int getLevenshteinDistance(String s0, String s1) {
        // Thank you, wikibooks:
        // http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++)
            cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    private static class Match<T> {
        public final T Item;
        public final int Rank;

        public Match(int rank, T item) {
            this.Rank = rank;
            this.Item = item;
        }
    }

    public static <T extends Searchable> Iterable<T> filterList(final String searchText, final Iterable<T> allCantons) {
        if (searchText == null || searchText.isEmpty())
            return allCantons;
        List<Match<T>> matches = new ArrayList<Searcher.Match<T>>();

        String lowerSearchText = searchText.toLowerCase();

        for (T cnt : allCantons) {
            String[] searchStrings = cnt.getSearchStrings();
            for (String toSearch : searchStrings) {
                if (lowerSearchText.equals(toSearch)) {
                    matches.add(new Match<T>(1, cnt));
                } else if (lowerSearchText.contains(toSearch)) {
                    matches.add(new Match<T>(2, cnt));
                } else {
                    int absoluteDist = getLevenshteinDistance(lowerSearchText, toSearch);
                    if ((absoluteDist / (double) lowerSearchText.length()) < 0.5) {
                        matches.add(new Match<T>(3 + absoluteDist, cnt));
                    }
                }
            }
        }
        return matches.stream().sorted((s1, s2) -> Integer.compare(s1.Rank, s2.Rank)).map(s -> s.Item)
                .collect(Collectors.toList());
    }

}
