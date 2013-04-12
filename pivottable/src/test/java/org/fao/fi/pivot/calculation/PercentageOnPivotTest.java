package org.fao.fi.pivot.calculation;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.pivot.model.Row;
import org.fao.fi.pivot.model.fact.Fact;
import org.fao.fi.pivot.test.Pivot2Sysout;
import org.fao.fi.pivot.test.PivotTableMocker;
import org.junit.Test;

public class PercentageOnPivotTest {

	// @Test
	public void testVerticalPercentage() {
		int rows = 3;
		int columns = 2;
		int rowColumns = 1;

		PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, rowColumns);
		PercentageOnPivot.verticalPercentage(pivotTable);

		Pivot2Sysout.pivot2SysOut(pivotTable);
		List<Row> rowList = pivotTable.getRowSection().getRowList();

		// row 1
		assertEquals(11.11, ((Fact) rowList.get(0).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(16.66, ((Fact) rowList.get(0).getFactList().get(1)).getValue(), 1e-2);

		// row 2
		assertEquals(33.33, ((Fact) rowList.get(1).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(33.33, ((Fact) rowList.get(1).getFactList().get(1)).getValue(), 1e-2);

		// row 3
		assertEquals(55.55, ((Fact) rowList.get(2).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(50.0, ((Fact) rowList.get(2).getFactList().get(1)).getValue(), 0);
	}

	@Test
	public void testHorizontalPercentage() {
		int rows = 3;
		int columns = 2;
		int rowColumns = 1;
		PivotTable pivotTable = PivotTableMocker.mockIt(rows, columns, rowColumns);
		Pivot2Sysout.pivot2SysOut(pivotTable);
		PercentageOnPivot.horizontalPercentage(pivotTable);

		Pivot2Sysout.pivot2SysOut(pivotTable);
		List<Row> rowList = pivotTable.getRowSection().getRowList();

		// 33.33333333 66.66666667 100
		// 42.85714286 57.14285714 100
		// 45.45454545 54.54545455 100

		// row 1
		assertEquals(33.33, ((Fact) rowList.get(0).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(66.66, ((Fact) rowList.get(0).getFactList().get(1)).getValue(), 1e-2);

		// row 2
		assertEquals(42.85, ((Fact) rowList.get(1).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(57.14, ((Fact) rowList.get(1).getFactList().get(1)).getValue(), 1e-2);

		// row 3
		assertEquals(45.45, ((Fact) rowList.get(2).getFactList().get(0)).getValue(), 1e-2);
		assertEquals(54.54, ((Fact) rowList.get(2).getFactList().get(1)).getValue(), 1e-2);
	}

}
