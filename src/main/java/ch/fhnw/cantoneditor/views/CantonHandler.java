package ch.fhnw.cantoneditor.views;

import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.observation.ObservableValue;

public class CantonHandler {
    private static final ObservableValue<Canton> currentCanton = new ObservableValue<>();

    public static Canton getCurrentCanton() {
        return currentCanton.get();
    }

    public static void setCurrentCanton(Canton cnt) {
        currentCanton.set(cnt);
    }
}
