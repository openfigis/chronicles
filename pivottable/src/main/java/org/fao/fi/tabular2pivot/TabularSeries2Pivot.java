package org.fao.fi.tabular2pivot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.tabulardata.model.TabularSeries;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Concept;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;

/**
 * Converter from a TabularSeries to a pivot.
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularSeries2Pivot {

    private SeriesMetadata seriesMetadata;
    private int numberOfNonColumnDimensions;

    /**
     * Construct the converter, giving it a seriesMetadata.
     * 
     * @param series2PivotseriesMetadata
     */
    public TabularSeries2Pivot(SeriesMetadata seriesMetadata) {
        this.seriesMetadata = seriesMetadata;
        // just calculate some metadata, how many dimension are there in the row
        // section?
        List<? extends Concept> cl = seriesMetadata.getHeaderList();
        for (Concept concept : cl) {
            if (concept instanceof Dimension) {
                numberOfNonColumnDimensions++;
            }
        }
    }

    public PivotTable convert(TabularSeries tabularSeries) {
        PivotTable pivotTable = new PivotTable();

        // process the first row of the series
        String[] firstRow = tabularSeries.getTable()[0];
        processFirstRow(firstRow, pivotTable);

        // process the other (data) rows of the series
        processDataRows(tabularSeries, pivotTable);

        fillGapsWithNulls(pivotTable);

        return pivotTable;
    }

    private void fillGapsWithNulls(PivotTable pivotTable) {
        List<Row> rowList = pivotTable.getRowSection().getRowList();

        int numberOfColumns = pivotTable.getColumnSection().getColumnFieldList().size();
        int numberOfRows = pivotTable.getRowSection().getRowList().size();

        for (Row row : rowList) {
            if (row.getFactList().size() != numberOfColumns) {
                // fill rows up with null
                for (int i = row.getFactList().size(); i < numberOfColumns; i++) {
                    row.getFactList().add(null);
                }
                List<ColumnField> columnFieldList = pivotTable.getColumnSection().getColumnFieldList();
                for (ColumnField columnField : columnFieldList) {
                    for (int i = columnField.getFactList().size(); i < numberOfRows; i++) {
                        columnField.getFactList().add(null);
                    }
                }

            }
        }
    }

    private void processDataRows(TabularSeries tabularSeries, PivotTable pivotTable) {
        String table[][] = tabularSeries.getTable();
        Map<String, ColumnField> keyColumnMap = new HashMap<String, ColumnField>();
        Map<String, Row> keyRowMap = new HashMap<String, Row>();
        for (int i = 0; i < table.length; i++) {
            if (i != 0) {
                String[] row = table[i];

                // prepare the fact
                Fact fact = prepareTheFact(row[seriesMetadata.getValueIndex()]);

                // process the Column Section
                processTheColumnSection(pivotTable, keyColumnMap, row[seriesMetadata.getColumnDimensionIndex()], fact);

                // process the Row Section for the dimensions and attributes
                processTheRowSection(pivotTable, keyRowMap, seriesMetadata, fact, row);

            }
        }
    }

    private void processTheRowSection(PivotTable pivotTable, Map<String, Row> keyRowMap,
            SeriesMetadata seriesMetadata2, Fact fact, String[] row) {

        int d[] = seriesMetadata.getNonColumnDimensionsIndices();
        // int a[] = seriesMetadata.getAttributeIndices();
        String rowKey = "";
        for (int j : d) {
            rowKey = rowKey + row[j];
        }
        Row rowFound = null;
        if (keyRowMap.containsKey(rowKey)) {
            // use existing row
            rowFound = keyRowMap.get(rowKey);
        } else {
            // make new row
            rowFound = new Row();
            // add the dimension values
            for (int j : d) {
                RowField dimensionValue = new RowField();
                dimensionValue.setValue(row[j]);
                rowFound.getRowFieldList().add(dimensionValue);
            }
            keyRowMap.put(rowKey, rowFound);
            pivotTable.getRowSection().getRowList().add(rowFound);
        }

        // add the attribute values

        // for (int j : a) {
        // org.fao.fi.pivot.model.Attribute attribute = new org.fao.fi.pivot.model.Attribute();
        // attribute.setValue(row[j]);
        // fact.getAttributeList().add(attribute);
        // }
        fact.setRow(rowFound);

        // the fact should be put on the position of the corresponding column.
        ColumnField columnField = fact.getColumnField();
        int columnIndex = pivotTable.getColumnSection().getColumnFieldList().indexOf(columnField);
        int size = rowFound.getFactList().size();
        if (columnIndex >= size) {
            // if there are no values, fill up with null
            for (int i = size; i < columnIndex; i++) {
                rowFound.getFactList().add(null);
                ColumnField columnField1 = pivotTable.getColumnSection().getColumnFieldList().get(i);
                columnField1.getFactList().add(null);
            }
        }
        rowFound.getFactList().add(fact);
    }

    private void processTheColumnSection(PivotTable pivotTable, Map<String, ColumnField> keyColumnMap,
            String columnValue, Fact fact) {
        ColumnField columnField;
        if (keyColumnMap.containsKey(columnValue)) {
            columnField = keyColumnMap.get(columnValue);
        } else {
            columnField = new ColumnField();
            columnField.setValue(columnValue);
            pivotTable.getColumnSection().getColumnFieldList().add(columnField);
            keyColumnMap.put(columnValue, columnField);
        }
        columnField.getFactList().add(fact);
        fact.setColumnField(columnField);

    }

    private Fact prepareTheFact(String factValue) {
        Fact fact = new Fact();
        if (factValue != null) {
            fact.setValue(new Double(factValue));
        }
        return fact;
    }

    private void processFirstRow(String[] firstRow, PivotTable pivotTable) {
        List<? extends Concept> headerList = seriesMetadata.getHeaderList();
        if (firstRow.length != seriesMetadata.getHeaderList().size()) {
            throw new TabularSeries2PivotException("Number of rows found in series(" + firstRow.length
                    + ") is not the same as expected in the seriesMetadata(" + seriesMetadata.getHeaderList().size()
                    + ")");
        }
        for (Concept concept : headerList) {
            if ((concept instanceof Dimension) && !(concept instanceof ColumnDimension)) {
                RowHeaderField rowHeaderField = new RowHeaderField();
                rowHeaderField.setConceptName(concept.getName());
                pivotTable.getRowSection().getRowHeaderFieldList().add(rowHeaderField);
            }
            if (concept instanceof ColumnDimension) {
                pivotTable.getColumnSection().setConceptName(concept.getName());
            }
            if (concept instanceof Value) {
                pivotTable.setFactConceptName(concept.getName());
            }
        }

    }

}
