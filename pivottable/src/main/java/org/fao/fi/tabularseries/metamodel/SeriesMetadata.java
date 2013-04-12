package org.fao.fi.tabularseries.metamodel;

import java.util.ArrayList;
import java.util.List;

public class SeriesMetadata {

    private List<? extends Concept> headerList;
    private int columnDimensionIndex;
    private int attributeIndices[];
    private int nonColumnDimensionsIndices[];
    private int valueIndex;

    public SeriesMetadata(List<? extends Concept> headerList) {
        this.headerList = headerList;
        int i = 0;
        List<Integer> attributes = new ArrayList<Integer>();
        List<Integer> nonColumnDimensions = new ArrayList<Integer>();
        for (Concept concept : headerList) {
            if (concept instanceof Dimension && !(concept instanceof ColumnDimension)) {
                nonColumnDimensions.add(Integer.valueOf(i));
            }
            if (concept instanceof Value) {
                valueIndex = i;
            }
            if (concept instanceof Attribute) {
                attributes.add(Integer.valueOf(i));
            }
            if (concept instanceof ColumnDimension) {
                columnDimensionIndex = i;
            }
            i++;
        }
        // analyse the attributes
        attributeIndices = new int[attributes.size()];
        for (int j = 0; j < attributes.size(); j++) {
            attributeIndices[j] = attributes.get(j);
        }
        // analyse the nonColumnDimensions
        nonColumnDimensionsIndices = new int[nonColumnDimensions.size()];
        for (int j = 0; j < nonColumnDimensions.size(); j++) {
            nonColumnDimensionsIndices[j] = nonColumnDimensions.get(j);
        }
    }

    public List<? extends Concept> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<? extends Concept> headerList) {
        this.headerList = headerList;
    }

    public int getColumnDimensionIndex() {
        return columnDimensionIndex;
    }

    public void setColumnDimensionIndex(int columnDimensionIndex) {
        this.columnDimensionIndex = columnDimensionIndex;
    }

    public int getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

    public int[] getAttributeIndices() {
        return attributeIndices;
    }

    public void setAttributeIndices(int[] attributeIndices) {
        this.attributeIndices = attributeIndices.clone();
    }

    public int[] getNonColumnDimensionsIndices() {
        return nonColumnDimensionsIndices;
    }

    public void setNonColumnDimensionsIndices(int[] nonColumnDimensionsIndices) {
        this.nonColumnDimensionsIndices = nonColumnDimensionsIndices.clone();
    }

}
