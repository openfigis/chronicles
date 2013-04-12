package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fao.fi.pivot.PivotTableException;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.PivotTableValidator;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class ClusterAnalysisOnPivotTest {

    PivotTableValidator v = new PivotTableValidator();

    @Test
    public void testCluster1() {
        int rows = 2;
        int columns = 3;
        int rowColumns = 1;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);
        assertTrue(v.validate(p));
        Set<Double> valueSet = new HashSet<Double>();
        List<ColumnField> list = p.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> facts = columnField.getFactList();
            for (AbstractFact fact : facts) {
                valueSet.add(((Fact) fact).getValue());
            }
        }

        int numberOfClusters = 3;
        Pivot2Sysout.pivot2SysOut(p);
        ClusterAnalysisOnPivot.cluster(p, numberOfClusters);

        // check consistency with the desired number of clusters.
        assertEquals(numberOfClusters, p.getColumnSection().getColumnFieldList().size());
        List<Row> rowList = p.getRowSection().getRowList();
        assertEquals(rows, rowList.size());
        for (Row row : rowList) {
            assertEquals(numberOfClusters, row.getFactList().size());
        }

        // do the regular validation
        assertTrue(v.validate(p));

        // check whether the values from a row perspective picked where existing values
        // in this case it should because the number of clusters are the same as the number of input columns
        list = p.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : list) {
            List<AbstractFact> facts = columnField.getFactList();
            assertEquals(rows, facts.size());
            for (AbstractFact fact : facts) {
                assertTrue(valueSet.contains(((Fact) fact).getValue()));
            }
        }

        // check whether the values from a column perspective picked where existing values
        List<Row> foundRows = p.getRowSection().getRowList();
        for (Row row : foundRows) {
            List<AbstractFact> facts = row.getFactList();
            assertEquals(numberOfClusters, facts.size());
            for (AbstractFact fact : facts) {
                System.out.println(((Fact) fact).getValue());
                assertTrue(valueSet.contains(((Fact) fact).getValue()));
            }

        }

        Pivot2Sysout.pivot2SysOut(p);
    }

    /**
     * This test breaks and has therefore been excluded because it has been files as a JIRA for Apache Commons Math.
     * https://issues.apache.org/jira/browse/MATH-429
     * 
     * 
     */
    @Test
    public void testCluster2() {
        int rows = 4;
        int columns = 27;
        int rowColumns = 1;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);

        int numberOfClusters = 3;
        Pivot2Sysout.pivot2SysOut(p);
        ClusterAnalysisOnPivot.cluster(p, numberOfClusters);
        assertTrue(v.validate(p));
        assertEquals("CLUSTER", p.getColumnSection().getConceptName());
    }

    /**
     * 
     */
    @Test
    public void testClusterBin() {
        int rowColumns = 1;
        int numberOfClusters = 2;

        for (int row = 1; row < 12; row++) {
            for (int c = numberOfClusters; c < 31; c++) {
                PivotTable p = PivotTableMocker.mockIt(row, c, rowColumns);
                try {
                    ClusterAnalysisOnPivot.cluster(p, numberOfClusters);
                } catch (ArithmeticException e) {
                    System.out.println(row + " " + c);
                }
                assertTrue(v.validate(p));
            }
        }
    }

    /**
     * Testing while comparing with testdata processed by the desktop program Statistica. Input: 4 4 5 5
     * 
     * 4 4 5 5
     * 
     * 4 4 5 5
     * 
     * 6 6 3 3
     * 
     * 6 6 3 3
     * 
     * clustering into 2 clusters
     * 
     * 4.000000 5.000000
     * 
     * 4.000000 5.000000
     * 
     * 6.000000 3.000000
     * 
     * 6.000000 3.000000
     * 
     * Horizontally there are the variables. Vertically there are the cases. In this clustering you see that a variable
     * cannot show up in more than 1 cluster.
     * 
     * 
     */
    @Test
    public void testClusterStatistica() {
        int rows = 4;
        int columns = 4;
        int rowColumns = 1;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);
        List<Row> rrowss = p.getRowSection().getRowList();
        // row1
        ((Fact) rrowss.get(0).getFactList().get(0)).setValue(4.0);
        ((Fact) rrowss.get(0).getFactList().get(1)).setValue(4.0);
        ((Fact) rrowss.get(0).getFactList().get(2)).setValue(5.0);
        ((Fact) rrowss.get(0).getFactList().get(3)).setValue(5.0);

        // row2
        ((Fact) rrowss.get(1).getFactList().get(0)).setValue(4.0);
        ((Fact) rrowss.get(1).getFactList().get(1)).setValue(4.0);
        ((Fact) rrowss.get(1).getFactList().get(2)).setValue(5.0);
        ((Fact) rrowss.get(1).getFactList().get(3)).setValue(5.0);

        // row3
        ((Fact) rrowss.get(2).getFactList().get(0)).setValue(6.0);
        ((Fact) rrowss.get(2).getFactList().get(1)).setValue(6.0);
        ((Fact) rrowss.get(2).getFactList().get(2)).setValue(3.0);
        ((Fact) rrowss.get(2).getFactList().get(3)).setValue(3.0);

        // row4
        ((Fact) rrowss.get(3).getFactList().get(0)).setValue(6.0);
        ((Fact) rrowss.get(3).getFactList().get(1)).setValue(6.0);
        ((Fact) rrowss.get(3).getFactList().get(2)).setValue(3.0);
        ((Fact) rrowss.get(3).getFactList().get(3)).setValue(3.0);

        int numberOfClusters = 2;
        Pivot2Sysout.pivot2SysOut(p);
        ClusterAnalysisOnPivot.cluster(p, numberOfClusters);
        Pivot2Sysout.pivot2SysOut(p);

        assertTrue(v.validate(p));
        // row1
        assertEquals(4, ((Fact) rrowss.get(0).getFactList().get(1)).getValue(), 0.0);
        assertEquals(5, ((Fact) rrowss.get(0).getFactList().get(0)).getValue(), 0.0);
        // row2
        assertEquals(4, ((Fact) rrowss.get(1).getFactList().get(1)).getValue(), 0.0);
        assertEquals(5, ((Fact) rrowss.get(1).getFactList().get(0)).getValue(), 0.0);
        // row3
        assertEquals(6, ((Fact) rrowss.get(2).getFactList().get(1)).getValue(), 0.0);
        assertEquals(3, ((Fact) rrowss.get(2).getFactList().get(0)).getValue(), 0.0);

        // row3
        assertEquals(6, ((Fact) rrowss.get(3).getFactList().get(1)).getValue(), 0.0);
        assertEquals(3, ((Fact) rrowss.get(3).getFactList().get(0)).getValue(), 0.0);

    }

    @Test
    public void testClusterColumnsInCluster() {
        int rows = 2;
        int columns = 9;
        int rowColumns = 1;
        int numberOfClusters = 6;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);

        Pivot2Sysout.pivot2SysOut(p);
        ClusterAnalysisOnPivot.cluster(p, numberOfClusters);
        Pivot2Sysout.pivot2SysOut(p);

        List<ColumnField> list = p.getColumnSection().getColumnFieldList();

        int numberOfColumuns = 0;
        for (ColumnField columnField : list) {
            numberOfColumuns = numberOfColumuns + columnField.getAttributeList().get(0).getValue();
            assertEquals(1, columnField.getAttributeList().size());
            assertTrue(columnField.getAttributeList().get(0).getValue() > 0);
            System.out.println(columnField.getAttributeList().get(0).getValue());
        }
        assertEquals(columns, numberOfColumuns);
    }

    /**
     * This test breaks and has therefore been excluded because it has been files as a JIRA for Apache Commons Math.
     * https://issues.apache.org/jira/browse/MATH-429
     * 
     * 
     */
    @Test
    public void testClusterWith0Clusters() {
        int rows = 0;
        int columns = 0;
        int rowColumns = 1;
        PivotTable p = PivotTableMocker.mockIt(rows, columns, rowColumns);
        int numberOfClusters = 0;
        ClusterAnalysisOnPivot.cluster(p, numberOfClusters);
    }    
    
    
    @Test
    public void testOnErrorSet() {
        PivotTable pivotTable = PivotTableMocker.mockIt(5, 5, 5);
        try {
            ClusterAnalysisOnPivot.cluster(pivotTable, 200);
            fail();
        } catch (PivotTableException e) {
            // 5 columns cannot be clustered into 200 clusters.
        }
    }

}
