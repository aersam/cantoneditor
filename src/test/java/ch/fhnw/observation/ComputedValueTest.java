package ch.fhnw.observation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.model.Canton;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ComputedValueTest {

    static int readCounter = 0;

    private static void increaseCounter() {
        readCounter++;
    }

    @Test
    public void testDependencies() throws JsonIOException, JsonSyntaxException, ClassNotFoundException, IOException,
            ParseException, NoDataFoundException {
        Canton c;
        c = new Canton();

        ComputedValue<String> comp = new ComputedValue<String>(() -> c.getName() + " " + c.getCapital());

        comp.addPropertyChangeListener((evt) -> {
            ComputedValueTest.increaseCounter();
        });
        c.setName("Aargau");
        c.setCapital("Aarau");
        // Should return correct value
        assertEquals(comp.get(), c.getName() + " " + c.getCapital());

        int oldReadCount = ComputedValueTest.readCounter;
        c.setName("Zürich");

        // Should have changed
        assertTrue(ComputedValueTest.readCounter > oldReadCount);

        oldReadCount = ComputedValueTest.readCounter;
        assertEquals(comp.get(), c.getName() + " " + c.getCapital());

        // Should not change
        c.setShortCut("ZH");
        comp.get();
        assertEquals(oldReadCount, ComputedValueTest.readCounter);
    }

    @Test
    public void testNested() {
        Canton c = Canton.createNew();
        ComputedValue<String> comp1 = new ComputedValue<String>(c::getName);
        ComputedValue<String> comp2 = new ComputedValue<String>(comp1::get);
        c.setName("asdf");
        assertEquals(comp1.get(), c.getName());
        assertEquals(comp2.get(), c.getName());

        comp2.addPropertyChangeListener((evt) -> ComputedValueTest.increaseCounter());
        int oldCount = ComputedValueTest.readCounter;
        c.setName("newstring");
        assertTrue(ComputedValueTest.readCounter > oldCount);
    }

    @Test
    public void testSet() {
        ObservableList<String> values = new ObservableList<>();
        values.add("a");
        values.add("b");
        ComputedValue<String> commaList = new ComputedValue<String>(() -> String.join(",", values));
        assertEquals(commaList.get(), "a,b");
        commaList.addPropertyChangeListener((evt) -> ComputedValueTest.increaseCounter());
        int oldCount = ComputedValueTest.readCounter;
        values.add("c");
        assertTrue(ComputedValueTest.readCounter > oldCount);
        assertEquals(commaList.get(), "a,b,c");
    }

}
