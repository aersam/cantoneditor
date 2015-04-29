package ch.fhnw.observation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a helper class for ComputedValue. Classes that implement PropertyChangeable can use this
 * to notify computed values about Reads. This makes automatic dependency tracking possible
 * */
public class ReadObserver {

    /** The dependencies that are currently being observed */
    private static Map<PropertyChangeable, List<String>> trackedReads = new HashMap<PropertyChangeable, List<String>>();

    /** A boolean indicating whether it is necessary to track Reads currently */
    private static boolean isObserving = false;

    /** Notify about read access to a property. The property can be null to read all properties */
    public static void notifyRead(PropertyChangeable object, String property) {
        if (isObserving) {// Track changes
            List<String> observingProperties = new ArrayList<String>();
            List<String> existingProperties = trackedReads.putIfAbsent(object, observingProperties);
            if (existingProperties != null) {
                observingProperties = existingProperties;
            }
            if (!observingProperties.contains(property)) {
                observingProperties.add(property);
            }
        }
    }

    /**
     * Start observing reads of properties. If false is returned, no reads are tracked! Do not call
     * endObserve in such a case
     */
    /* package */static boolean startObserving() {
        if (isObserving)
            return false;
        isObserving = true;
        trackedReads = new HashMap<PropertyChangeable, List<String>>();
        return true;
    }

    /** End of observing */
    /* package */static Map<PropertyChangeable, List<String>> endObserve() {
        Map<PropertyChangeable, List<String>> trackedReads = ReadObserver.trackedReads;
        ReadObserver.trackedReads = null;
        isObserving = false;
        return trackedReads;
    }
}
