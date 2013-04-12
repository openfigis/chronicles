package org.fao.fi.pivot.calculation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewListComperator implements Comparator<Object> {

    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    private List<? extends Object> toBeSortedList;

    /**
     * 
     * @param oldList
     *            The old list
     * @param newList
     *            The new list, sorted based on the old list according a certain algorithm.
     * @param toBeSortedList
     *            A list which needs to be sorted according the same algorithm which has been used to sort the old list,
     *            generating a new list.
     * 
     */
    public NewListComperator(Object[] oldList, List<? extends Object> newList, List<? extends Object> toBeSortedList) {
        this.toBeSortedList = toBeSortedList;
        int size = oldList.length;
        for (int i = 0; i < size; i++) {
            int n = newList.indexOf(oldList[i]);
            map.put(i, n);
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        int oldIndex1 = toBeSortedList.indexOf(o1);
        int oldIndex2 = toBeSortedList.indexOf(o2);
        int newIndex1 = map.get(oldIndex1);
        int newIndex2 = map.get(oldIndex2);
        int result = 0;
        if (newIndex1 > newIndex2) {
            result = 1;
        }
        if (newIndex1 < newIndex2) {
            result = -1;
        }
        return result;
    }

}
