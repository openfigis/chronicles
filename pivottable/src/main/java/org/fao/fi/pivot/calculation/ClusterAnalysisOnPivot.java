package org.fao.fi.pivot.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.clustering.Cluster;
import org.apache.commons.math.stat.clustering.EuclideanIntegerPoint;
import org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer;
import org.fao.fi.pivot.PivotTableException;
import org.fao.fi.pivot.model.Attribute;
import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;

public class ClusterAnalysisOnPivot {

    private static final int MAX_INTERATIONS = 10;
    private static final int TRICK = 1000000;
    private static final String CLUSTER = "CLUSTER";

    private ClusterAnalysisOnPivot() {
        // Utility classes should not have a public or default constructor
    }

    /**
     * 
     * The algorithm does a clustering for every row. So for every row the resources are clustered in x clusters
     * (columns). Next the mean is calculated for each cluster. The mean will then be the new value for in the row for
     * that cluster (column)
     * 
     * 
     * @param pivotTable
     * @param numberOfClusters
     */
    public static void cluster(PivotTable pivotTable, int numberOfClusters) {
        if (numberOfClusters > pivotTable.getColumnSection().getColumnFieldList().size()) {
            throw new PivotTableException(
                    "The number of clusters can not be larger than the available number of points (columns)");
        }
        KMeansPlusPlusClusterer<EuclideanIntegerPoint> transformer = new KMeansPlusPlusClusterer<EuclideanIntegerPoint>(
                new Random(1746432956321l));

        // fill the cluster logic with the fact table
        List<ColumnField> rows = pivotTable.getColumnSection().getColumnFieldList();
        List<EuclideanIntegerPoint> pointList = new ArrayList<EuclideanIntegerPoint>();
        for (ColumnField columnField : rows) {
            List<AbstractFact> facts = columnField.getFactList();
            int[] point = new int[facts.size()];
            for (int i = 0; i < facts.size(); i++) {
                int value = (int) (((Fact) facts.get(i)).getValue() * TRICK);
                point[i] = value;
            }
            EuclideanIntegerPoint p = new EuclideanIntegerPoint(point);
            pointList.add(p);
        }
        if (pointList.size() > 0) {
            List<Cluster<EuclideanIntegerPoint>> clusters = transformer.cluster(pointList, numberOfClusters,
                    MAX_INTERATIONS);

            // replace the facts with the new cluster values
            updateFacts(pivotTable, clusters, numberOfClusters);

            // define attribute for each column with the number of columns used for the new column (cluster)
            updateColumnAttributes(pivotTable, clusters);
        }
        // Aesthetic: give the facts another concept name
        pivotTable.getColumnSection().setConceptName(CLUSTER);

        // Aesthetic: change the column names
        updateColumnNames(pivotTable);
    }

    /**
     * specify in an attribute the number of columns used for one cluster.
     * 
     * 
     * 
     * @param pivotTable
     * @param clusters
     */
    private static void updateColumnAttributes(PivotTable pivotTable, List<Cluster<EuclideanIntegerPoint>> clusters) {
        List<ColumnField> columns = pivotTable.getColumnSection().getColumnFieldList();
        for (ColumnField columnField : columns) {
            Cluster<EuclideanIntegerPoint> cluster = clusters.get(columns.indexOf(columnField));
            Attribute a = new Attribute(cluster.getPoints().size());
            columnField.getAttributeList().add(a);
        }
    }

    private static void updateColumnNames(PivotTable pivotTable) {
        List<ColumnField> list = pivotTable.getColumnSection().getColumnFieldList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setValue(CLUSTER + (i + 1));
        }
    }

    /**
     * Update the table with the new clusters.
     * 
     * @param pivotTable
     * @param clustersPerRow
     * @param numberOfClusters
     */
    private static void updateFacts(PivotTable pivotTable, List<Cluster<EuclideanIntegerPoint>> clusters,
            int numberOfClusters) {

        // chop facts more that the number of clusters
        List<Row> rows = pivotTable.getRowSection().getRowList();
        for (Row row : rows) {
            row.setFactList(row.getFactList().subList(0, numberOfClusters));
        }
        // chop columns more that the number of clusters
        pivotTable.getColumnSection().setColumnFieldList(
                pivotTable.getColumnSection().getColumnFieldList().subList(0, numberOfClusters));

        for (int columnIndex = 0; columnIndex < numberOfClusters; columnIndex++) {
            Cluster<EuclideanIntegerPoint> cluster = clusters.get(columnIndex);
            List<EuclideanIntegerPoint> points = cluster.getPoints();
            for (EuclideanIntegerPoint euclideanIntegerPoint : points) {
                int[] facts = euclideanIntegerPoint.getPoint();
                for (int i = 0; i < facts.length; i++) {
                    double value = (double) facts[i] / TRICK;
                    AbstractFact fact = pivotTable.getColumnSection().getColumnFieldList().get(columnIndex)
                            .getFactList().get(i);
                    ((Fact) fact).setValue(value);
                }
            }
        }

    }

}
