package org.fao.fi.pivot.calculation;

import java.util.Comparator;

import org.fao.fi.pivot.model.DoubleValue;

public class DoubleValueComperator implements Comparator<DoubleValue> {

    /**
     * Sort from high to low.
     */
    @Override
    public int compare(DoubleValue o1, DoubleValue o2) {
        int result = 0;
        if (o1.getValue() > o2.getValue()) {
            result = -1;
        }
        if (o1.getValue() < o2.getValue()) {
            result = 1;
        }
        return result;
    }

}
