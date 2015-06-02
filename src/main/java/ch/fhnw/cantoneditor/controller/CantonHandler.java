package ch.fhnw.cantoneditor.controller;

import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.observation.ObservableValue;

class CantonHandler {
    private static final ObservableValue<Canton> currentCanton = new ObservableValue<>();

    public static Canton getCurrentCanton() {
        return currentCanton.get();
    }

    public static ObservableValue<Canton> getCurrentCantonObservable() {
        return currentCanton;
    }

    public static void setCurrentCanton(Canton cnt) {
        if (cnt == null && currentCanton.get() != null) {
            return;
        }
        currentCanton.set(cnt);
    }
}
