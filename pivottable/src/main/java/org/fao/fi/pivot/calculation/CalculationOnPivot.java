package org.fao.fi.pivot.calculation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.StatUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.fao.fi.pivot.PivotTableException;
import org.fao.fi.pivot.model.CalculatedCollection;
import org.fao.fi.pivot.model.CalculatedField;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.Derived;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.RowField;
import org.fao.fi.pivot.model.RowHeaderField;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.util.PivotUtil;

public class CalculationOnPivot {

    public final static String OTHER = "OTHER";
    public final static String TOP = "TOP";
    public static final String AGGREGATION = "AGGREGATION";

    /**
     * Calculate the average of the values of a column of facts and this to a new row called AVERAGE
     * 
     * 
     * @param pivotTable
     */
    public void doVerticalCalculationOn(PivotTable pivotTable, CalculationType calculationType) {
        List<ColumnField> columnList = pivotTable.getColumnSection().getColumnFieldList();
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        CalculatedCollection ar = new CalculatedCollection(calculationType);
        pivotTable.getRowSection().getCalculatedCollectionList().add(ar);
        for (int i = 0; i < columnList.size(); i++) {
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (Row row : rowList) {
                if (row.getFactList().size() != pivotTable.getColumnSection().getColumnFieldList().size()) {
                    throw new PivotTableException("This row should have "
                            + pivotTable.getColumnSection().getColumnFieldList().size() + " elements but has "
                            + row.getFactList().size() + " elements");
                }

                AbstractFact fact = row.getFactList().get(i);
                if (fact != null) {
                    stats.addValue(((Fact) row.getFactList().get(i)).getValue());
                }
            }
            CalculatedField af = new CalculatedField();
            double calculationResult = 0;
            if (calculationType.equals(CalculationType.AVG)) {
                calculationResult = stats.getMean();
            }
            if (calculationType.equals(CalculationType.SUM)) {
                calculationResult = stats.getSum();
            }
            af.setValue(calculationResult);
            ar.getCalculatedFieldList().add(af);
        }
    }

    /**
     * Calculate the average of the values of a column of facts and this to a new row called AVERAGE
     * 
     * 
     * @param pivotTable
     */

    public void doHorizontalCalculationOn(PivotTable pivotTable, CalculationType calculationType) {
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        CalculatedCollection ac = new CalculatedCollection(calculationType);
        pivotTable.getColumnSection().getCalculatedCollectionList().add(ac);
        for (Row row : rowList) {
            CalculatedField cf = calculateField(row.getFactList(), calculationType);
            ac.getCalculatedFieldList().add(cf);
        }

    }

    /**
     * It could be that there where already calculations done on row level, adding a column on the right with the
     * result. For instance a sum on row level. This logic does this calculation again, there for calculating the
     * correct value for the TOTAL row.
     * 
     * TODO make this logic faster by only updating the TOTAL row and not calculating everything.
     * 
     * @param numberOfSelectedItems
     * @param otherRow
     * 
     * 
     * 
     */
    private void recalculateHorizontalAggregationValues(PivotTable pivotTable, int numberOfSelectedItems,
            Derived otherRow) {
        List<CalculatedCollection> list = pivotTable.getColumnSection().getCalculatedCollectionList();
        for (CalculatedCollection aggregatedColumn : list) {
            aggregatedColumn.getCalculatedFieldList().subList(0, numberOfSelectedItems);
            aggregatedColumn.setCalculatedFieldList(aggregatedColumn.getCalculatedFieldList().subList(0,
                    numberOfSelectedItems));
            CalculatedField cf = calculateField(otherRow.getFactList(), aggregatedColumn.getCalculationType());
            aggregatedColumn.getCalculatedFieldList().add(cf);
        }
    }

    private CalculatedField calculateField(List<AbstractFact> factList, CalculationType calculationType) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (AbstractFact fact : factList) {
            if (fact != null) {

                stats.addValue(((Fact) fact).getValue());
            }
        }
        CalculatedField acf = new CalculatedField();
        double calculationResult = 0;
        if (calculationType.equals(CalculationType.AVG)) {
            calculationResult = stats.getMean();
        }
        if (calculationType.equals(CalculationType.SUM)) {
            calculationResult = stats.getSum();
        }
        acf.setValue(calculationResult);
        return acf;
    }

    /**
     * This logic selects the top x rows. The other values will be aggregated and added as a row below named OTHER to
     * the row section.
     * 
     * 
     * @param numberOfSelectedItems
     */
    public void selectHorizontalTop(PivotTable pivotTable, int numberOfSelectedItems) {
        if (numberOfSelectedItems < pivotTable.getRowSection().getRowList().size()) {
            List<Row> top = pivotTable.getRowSection().getRowList().subList(0, numberOfSelectedItems);
            List<Row> other = pivotTable.getRowSection().getRowList()
                    .subList(numberOfSelectedItems, pivotTable.getRowSection().getRowList().size());

            Derived otherRow = calculateSumRow(pivotTable, other, OTHER);

            // add the new row
            top.add(otherRow);

            // replace the rows with the top rows and other.
            pivotTable.getRowSection().setRowList(top);

            // adjust the columns with aggregated values accordingly
            recalculateHorizontalAggregationValues(pivotTable, numberOfSelectedItems, otherRow);

            // adjust the colum section
            adjustColumnSectionWithNewHorizontalTop(pivotTable, otherRow);
        }
    }

    private Derived calculateSumRow(PivotTable pivotTable, List<Row> toBeSummed, String rowFieldText) {
        // Derived otherRow = new Derived();
        List<ColumnField> columnList = pivotTable.getColumnSection().getColumnFieldList();
        Derived sumRow = new Derived();
        // calculate the OTHER row.
        for (int i = 0; i < columnList.size(); i++) {
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (Row row : toBeSummed) {
                Fact fact = (Fact) row.getFactList().get(i);
                if (fact != null) {
                    stats.addValue(((Fact) row.getFactList().get(i)).getValue());
                }
            }
            Fact fact = new Fact();
            fact.setValue(stats.getSum());
            fact.setRow(sumRow);
            fact.setColumnField(columnList.get(i));
            sumRow.getFactList().add(fact);
        }
        // add the rowfields
        for (int i = 0; i < pivotTable.getRowSection().getRowHeaderFieldList().size(); i++) {
            RowField rowField = new RowField();
            rowField.setValue(rowFieldText);
            sumRow.getRowFieldList().add(rowField);
        }
        return sumRow;
    }

    /**
     * The column section has fields with references to the the facts on column level. The last elements should be
     * corrected with the new row.
     * 
     * @param otherRow
     * 
     */
    private void adjustColumnSectionWithNewHorizontalTop(PivotTable pivotTable, Derived otherRow) {
        int rows = pivotTable.getRowSection().getRowList().size();
        List<ColumnField> cl = pivotTable.getColumnSection().getColumnFieldList();
        for (int i = 0; i < cl.size(); i++) {
            ColumnField columnField = cl.get(i);
            List<AbstractFact> fl = columnField.getFactList().subList(0, rows);
            columnField.setFactList(fl);
            fl.set((rows - 1), otherRow.getFactList().get(i));
        }
    }

    /**
     * Standardize the values on column level.
     * 
     * This is not a calculation of CalculationType because this standardising means changing the values.
     * 
     * 
     * @param pivotTable
     */
    public void standardizeVerticalValues(PivotTable pivotTable) {
        List<ColumnField> columns = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columns) {
            List<AbstractFact> facts = columnField.getFactList();
            double[] sample = new double[facts.size()];
            for (int i = 0; i < facts.size(); i++) {
                sample[i] = ((Fact) facts.get(i)).getValue();
            }
            double[] normalised = StatUtils.normalize(sample);
            for (int i = 0; i < normalised.length; i++) {
                if (Double.isNaN(normalised[i])) {
                    ((Fact) facts.get(i)).setValue(0.0);
                } else {
                    ((Fact) facts.get(i)).setValue(normalised[i]);
                }
            }
        }
    }

    /**
     * Summarise the top into one row. It delete the aggregated columns
     * 
     * @param pivotTable
     */
    public void summarizeTop(PivotTable pivotTable) {

        int initialSize = pivotTable.getRowSection().getRowList().size();
        int indexLastElement = initialSize - 1;

        List<Row> top = null;
        Derived other = new Derived();
        Derived topRow = new Derived();

        if (initialSize == 0) {
            // empty no rows, no others. or
            createEmptyOtherRow(topRow, pivotTable.getRowSection().getRowHeaderFieldList().size(), pivotTable
                    .getColumnSection().getColumnFieldList(), TOP);
            createEmptyOtherRow(other, pivotTable.getRowSection().getRowHeaderFieldList().size(), pivotTable
                    .getColumnSection().getColumnFieldList(), OTHER);
        }
        if (initialSize > 0 && indexLastElement > 0
                && (pivotTable.getRowSection().getRowList().get(indexLastElement) instanceof Derived)) {
            // n rows with derived
            top = pivotTable.getRowSection().getRowList().subList(0, indexLastElement);
            // summarise the top into one row
            topRow = calculateSumRow(pivotTable, top, TOP);
            other = (Derived) pivotTable.getRowSection().getRowList().get(indexLastElement);
        }
        if (initialSize > 0 && indexLastElement > 0
                && !(pivotTable.getRowSection().getRowList().get(indexLastElement) instanceof Derived)) {
            // n rows with not a derived row
            top = pivotTable.getRowSection().getRowList().subList(0, indexLastElement);
            // summarise the top into one row
            topRow = calculateSumRow(pivotTable, top, TOP);
            createEmptyOtherRow(other, pivotTable.getRowSection().getRowHeaderFieldList().size(), pivotTable
                    .getColumnSection().getColumnFieldList(), OTHER);
        }

        // create new row list with top and other rows.
        List<Row> topAndOther = new ArrayList<Row>();
        topAndOther.add(topRow);
        topAndOther.add(other);

        // replace the rows with the top rows and other.
        pivotTable.getRowSection().setRowList(topAndOther);

        // delete the aggregated columns
        pivotTable.getColumnSection().setCalculatedCollectionList(new ArrayList<CalculatedCollection>());

        // adjust the column section
     //   adjustColumnSectionWithNewHorizontalTop(pivotTable, other);
        PivotUtil.copyFactsFromRow2Column(pivotTable);

        // adjust the row field headers
        List<RowHeaderField> rowHeaderFieldList = pivotTable.getRowSection().getRowHeaderFieldList();
        for (RowHeaderField rowHeaderField : rowHeaderFieldList) {
            rowHeaderField.setConceptName(AGGREGATION);
        }
        // it is all aggregated, only one column is enough now
        pivotTable.getRowSection().setRowHeaderFieldList(rowHeaderFieldList.subList(0, 1));
        List<Row> rowList = pivotTable.getRowSection().getRowList();
        for (Row row : rowList) {
            row.setRowFieldList(row.getRowFieldList().subList(0, 1));
        }

        PivotUtil.copyFactsFromRow2Column(pivotTable);

    }

    private void createEmptyOtherRow(Row emptyRow, int towHeaderFieldListSize, List<ColumnField> list, String type) {
        for (ColumnField columnField : list) {
            Fact fact = new Fact(0.0);
            fact.setRow(emptyRow);
            fact.setColumnField(columnField);
            emptyRow.getFactList().add(fact);
        }
        // add the rowfields
        for (int i = 0; i < towHeaderFieldListSize; i++) {
            RowField rowField = new RowField();
            rowField.setValue(type);
            emptyRow.getRowFieldList().add(rowField);
        }
    }

}
