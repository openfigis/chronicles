package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fao.fi.pivot.model.CalculatedField;
import org.fao.fi.pivot.model.DoubleValue;
import org.junit.Test;

public class NewListComperatorTest {

    @Test
    public void testCompare() {

        CalculatedField d1 = new CalculatedField();
        CalculatedField d2 = new CalculatedField();
        CalculatedField d3 = new CalculatedField();

        List<DoubleValue> oldList1 = new ArrayList<DoubleValue>();
        oldList1.add(d1);
        oldList1.add(d2);
        oldList1.add(d3);
        List<DoubleValue> newList = new ArrayList<DoubleValue>();
        newList.add(d3);
        newList.add(d2);
        newList.add(d1);

        Integer s1 = Integer.valueOf(3);
        Integer s2 = Integer.valueOf(2);
        Integer s3 = Integer.valueOf(5);

        List<Integer> migrate = new ArrayList<Integer>();
        migrate.add(s1);
        migrate.add(s2);
        migrate.add(s3);

        NewListComperator newListComperator = new NewListComperator(oldList1.toArray(), newList, migrate);
        Collections.sort(migrate, newListComperator);

        assertEquals(s3, migrate.get(0));
        assertEquals(s2, migrate.get(1));
        assertEquals(s1, migrate.get(2));

    }

    @Test
    public void testCompare2() {

        List<Double> dl1 = new ArrayList<Double>();
        List<Double> dl2 = new ArrayList<Double>();
        for (int i = 0; i < 77; i++) {
            Double d = new Double(Math.random());
            dl1.add(d);
            dl2.add(d);
        }
        Object[] oldListArray = dl1.toArray();
        Collections.sort(dl1);
        NewListComperator newListComperator = new NewListComperator(oldListArray, dl1, dl2);
        Collections.sort(dl2, newListComperator);
        for (int i = 0; i < 77; i++) {
            assertEquals(dl1.get(i).doubleValue(), dl2.get(i).doubleValue(), 0);
        }

    }

}
