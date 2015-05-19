package ch.fhnw.cantoneditor.datautils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Searcher<T extends Searchable> implements Runnable {

    private final Collection<T> sourcelist;
    private final String searchText;
    private Collection<T> result;

    private ActionListener onFinishAction;

    public Searcher(String searchText, Collection<T> sourcelist) {
        this.sourcelist = sourcelist;
        this.searchText = searchText;
    }

    public Searcher<T> setOnFinish(ActionListener listener) {
        this.onFinishAction = listener;
        return this;
    }

    @Override
    public void run() {
        this.result = filterList(searchText, this.sourcelist);
        if (this.onFinishAction != null)
            this.onFinishAction.actionPerformed(new ActionEvent(this, 0, "run"));

    }

    public Collection<T> getResult() {
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

    public static <T extends Searchable> Collection<T> filterList(final String searchText,
            final Collection<T> allCantons) {
        if (searchText == null || searchText.isEmpty())
            return allCantons;
        List<Match<T>> matches = new ArrayList<Searcher.Match<T>>();

        String lowerSearchText = searchText.toLowerCase();

        for (T cnt : allCantons) {
            String[] searchStrings = cnt.getSearchStrings();
            int bestMatch = -1;
            for (String toSearch : searchStrings) {
                if (lowerSearchText.equals(toSearch)) {
                    bestMatch = 1;
                    break;
                } else if (toSearch.contains(lowerSearchText)) {
                    bestMatch = 2;
                } else {
                    int absoluteDist = getLevenshteinDistance(lowerSearchText, toSearch);
                    if ((absoluteDist / (double) lowerSearchText.length()) < 0.6) {
                        bestMatch = 3 + absoluteDist;
                    }
                }
            }
            if (bestMatch != -1) {
                matches.add(new Match<T>(bestMatch, cnt));
            }
        }
        return matches.stream().sorted((s1, s2) -> Integer.compare(s1.Rank, s2.Rank)).map(s -> s.Item)
                .collect(Collectors.toList());
    }

}
