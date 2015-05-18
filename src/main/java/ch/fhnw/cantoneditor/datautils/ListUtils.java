package ch.fhnw.cantoneditor.datautils;

import java.util.Collection;

public class ListUtils {
    public static <T> boolean contentEquals(Collection<T> list1, Collection<T> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        for (T item : list1) {
            if (!list2.contains(item))
                return false;
        }
        return true;
    }
}
