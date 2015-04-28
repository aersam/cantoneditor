package ch.fhnw.cantoneditor.datautils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ch.fhnw.cantoneditor.model.Canton;

public class ComputedValueTest {

    static int readCounter = 0;

    private static void increaseCounter() {
        readCounter++;
    }

    @Test
    public void testDependencies() {
        Canton c = Canton.GetById(1, true);
        ComputedValue<String> comp = new ComputedValue<String>(() -> {
            String output = c.getName() + " " + c.getCapital();
            ComputedValueTest.increaseCounter();
            return output;
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
}
