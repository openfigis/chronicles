package org.fao.fi.tabular2pivot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.fao.fi.tabularseries.metamodel.Attribute;
import org.fao.fi.tabularseries.metamodel.ColumnDimension;
import org.fao.fi.tabularseries.metamodel.Concept;
import org.fao.fi.tabularseries.metamodel.Dimension;
import org.fao.fi.tabularseries.metamodel.SeriesMetadata;
import org.fao.fi.tabularseries.metamodel.Value;
import org.junit.Before;
import org.junit.Test;

public class SeriesMetadataTest {

    SeriesMetadata sm;

    @Before
    public void before() {
        Dimension d1 = new Dimension("country");
        ColumnDimension d2 = new ColumnDimension("year");
        Attribute a1 = new Attribute("comment");
        Value v1 = new Value("catch");
        List<Concept> headerList = new ArrayList<Concept>();
        headerList.add(d1);
        headerList.add(d2);
        headerList.add(a1);
        headerList.add(v1);
        sm = new SeriesMetadata(headerList);
    }

    @Test
    public void testGetColumnDimensionIndex() {
        assertNotNull(sm.getHeaderList());
        assertEquals(1, sm.getColumnDimensionIndex());
    }

    @Test
    public void testGetValueIndex() {
        assertEquals(3, sm.getValueIndex());
    }

    @Test
    public void testGetAttributeIndices() {
        assertEquals(1, sm.getAttributeIndices().length);
        assertEquals(2, sm.getAttributeIndices()[0]);
    }

    @Test
    public void testGetNumberOfNonColumnDimensionsIndices() {
        assertEquals(1, sm.getNonColumnDimensionsIndices().length);
        assertEquals(0, sm.getNonColumnDimensionsIndices()[0]);
    }

}
