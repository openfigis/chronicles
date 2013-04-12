package org.fao.fi.pivot.calculation;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.pivot.model.ColumnField;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.AbstractFact;
import org.fao.fi.pivot.model.fact.Fact;

public class PercentageOnPivot {

	private PercentageOnPivot() {
		// Utility classes should not have a public or default constructor
	}

	/**
	 * calculate the percentages bases on the series of vertical values
	 * (columns)
	 * 
	 * 
	 * @param pivotTabel
	 */
	public static void verticalPercentage(PivotTable pivotTabel) {
		List<ColumnField> cl = pivotTabel.getColumnSection().getColumnFieldList();
		List<Double> sums = new ArrayList<Double>();
		// calculate the total for every column
		for (ColumnField columnField : cl) {
			List<AbstractFact> al = columnField.getFactList();
			double sum = 0;
			for (AbstractFact abstractFact : al) {
				Fact f = (Fact) abstractFact;
				sum = sum + f.getValue();
			}
			sums.add(sum);
		}
		// calculate the percentage and replace the old value.
		for (ColumnField columnField : cl) {
			List<AbstractFact> al = columnField.getFactList();
			for (AbstractFact abstractFact : al) {
				Fact f = (Fact) abstractFact;
				double percentage = f.getValue() / sums.get(cl.indexOf(columnField)) * 100;
				f.setValue(percentage);
			}
		}
	}

	/**
	 * Calculate the percentages bases on the series of horizontal values
	 * (columns)
	 * 
	 * 
	 * @param pivotTabel
	 */
	public static void horizontalPercentage(PivotTable pivotTabel) {
		List<Row> rl = pivotTabel.getRowSection().getRowList();
		List<Double> sums = new ArrayList<Double>();
		// calculate the total for every column
		for (Row row : rl) {
			List<AbstractFact> al = row.getFactList();
			double sum = 0;
			for (AbstractFact abstractFact : al) {
				Fact f = (Fact) abstractFact;
				sum = sum + f.getValue();
			}
			sums.add(sum);
		}
		// calculate the percentage and replace the old value.
		for (Row row : rl) {
			List<AbstractFact> al = row.getFactList();
			for (AbstractFact abstractFact : al) {
				Fact f = (Fact) abstractFact;
				double percentage = f.getValue() / sums.get(rl.indexOf(row)) * 100;
				f.setValue(percentage);
			}
		}
	}

}
