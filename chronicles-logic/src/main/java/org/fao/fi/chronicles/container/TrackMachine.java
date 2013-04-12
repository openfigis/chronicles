package org.fao.fi.chronicles.container;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.fao.fi.chronicles.aesthetic.AestheticProcess;
import org.fao.fi.chronicles.calcuation.CalculationProcess1;
import org.fao.fi.chronicles.calcuation.CalculationProcess2;
import org.fao.fi.chronicles.calcuation.CalculationProcess3;
import org.fao.fi.chronicles.calcuation.CatchCalculationProcess2;
import org.fao.fi.chronicles.chartcreation.CaptureChartCreationProcess;
import org.fao.fi.chronicles.chartcreation.DevChartCreationProcess;
import org.fao.fi.chronicles.domain.CatchParms;
import org.fao.fi.chronicles.fishstatj.FishstatProcess;
import org.fao.fi.chronicles.fishstatj.FishstatProcessResult;
import org.fao.fi.pivot.model.PivotTable;
import org.fao.fi.tabular2pivot.TabularSeries2Pivot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * A track represents an intermediate result. A track is the cumulation of certain steps, expressed in a csv file. There
 * are 6 calculation steps performed in order to produce a chart.
 * 
 * This class is used to produce intermediate results from the 6 steps.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
@Component
public class TrackMachine {

    private FishstatProcess fishstatProcess;
    private CalculationProcess1 calculationProcess1 = new CalculationProcess1();
    private AestheticProcess aestheticProcess = new AestheticProcess();
    private CalculationProcess2 calculationProcess2 = new CalculationProcess2();
    private CalculationProcess3 calculationProcess3 = new CalculationProcess3();
    private DevChartCreationProcess devChartCreationProcess = new DevChartCreationProcess();

    // capture stuff
    private CatchCalculationProcess2 catchCalculationProcess2 = new CatchCalculationProcess2();
    private CaptureChartCreationProcess captureChartCreationProcess = new CaptureChartCreationProcess();

    private Map<Class<?>, Object> classMap = new HashMap<Class<?>, Object>();

    private static Map<Integer, Class<?>> order = new HashMap<Integer, Class<?>>();
    private static Map<Integer, Class<?>> captureOrder = new HashMap<Integer, Class<?>>();
    private static Map<Track, Map<Integer, Class<?>>> csvType = new HashMap<Track, Map<Integer, Class<?>>>();

    private static Map<Class<?>, Class<?>[]> paramTypes = new HashMap<Class<?>, Class<?>[]>();
    static {
        order.put(2, CalculationProcess1.class);
        order.put(3, AestheticProcess.class);
        order.put(4, CalculationProcess2.class);
        order.put(5, CalculationProcess3.class);
        order.put(6, DevChartCreationProcess.class);

        // capture stuff
        captureOrder.put(2, CatchCalculationProcess2.class);
        captureOrder.put(3, CaptureChartCreationProcess.class);

        // different parameter types.
        Class<?>[] t1 = { PivotTable.class, int.class };
        Class<?>[] t2 = { PivotTable.class };
        Class<?>[] t3 = { PivotTable.class, String[].class };

        paramTypes.put(CalculationProcess1.class, t1);
        paramTypes.put(AestheticProcess.class, t2);
        paramTypes.put(CalculationProcess2.class, t1);
        paramTypes.put(CalculationProcess3.class, t2);
        paramTypes.put(DevChartCreationProcess.class, t3);

        // capture stuff
        paramTypes.put(CatchCalculationProcess2.class, t2);
        paramTypes.put(CaptureChartCreationProcess.class, t3);
        
        //
        csvType.put(Track.DEVELOPMENT, order);
        csvType.put(Track.CAPTURE, captureOrder);

    }

    public TrackMachine() {
        classMap.put(CalculationProcess1.class, calculationProcess1);
        classMap.put(AestheticProcess.class, aestheticProcess);
        classMap.put(CalculationProcess2.class, calculationProcess2);
        classMap.put(CalculationProcess3.class, calculationProcess3);
        classMap.put(DevChartCreationProcess.class, devChartCreationProcess);

        // capture stuff
        classMap.put(CatchCalculationProcess2.class, catchCalculationProcess2);
        classMap.put(CaptureChartCreationProcess.class, captureChartCreationProcess);
    }

    /**
     * 
     * Get the pivot table for a given track and params
     * 
     * 
     * @param developmentParms
     * @param track
     * @return
     */
    @Cacheable(cacheName = "ChroniclesCache")
    public PivotTable run(CatchParms parms, int trackNumber, Track track) {
        // do the fishstatJ stuff -track1
        FishstatProcessResult result = fishstatProcess.run(parms.getStartYear(),
                parms.getEndYear(), parms.getUn());
        // convert tabular series data to a pivot table
        TabularSeries2Pivot tabularSeries2Pivot = new TabularSeries2Pivot(result.getSeriesMetadata());
        PivotTable pivotTable = tabularSeries2Pivot.convert(result.getTabularSeries());

        if (trackNumber > 1) {
            for (int i = 2; i <= trackNumber; i++) {
                Class<?> clazz = csvType.get(track).get(i);
                try {
                    Method method = clazz.getMethod("run", paramTypes.get(clazz));
                    Object params[] = defineParams(clazz, pivotTable, parms);
                    Object object = method.invoke(classMap.get(clazz), params);
                    if (clazz.equals(CalculationProcess3.class)) {
                        pivotTable = (PivotTable) object;
                    }
                } catch (SecurityException e) {
                    throw new ChroniclesException(e);
                } catch (NoSuchMethodException e) {
                    throw new ChroniclesException(e);
                } catch (IllegalArgumentException e) {
                    throw new ChroniclesException(e);
                } catch (IllegalAccessException e) {
                    throw new ChroniclesException(e);
                } catch (InvocationTargetException e) {
                    throw new ChroniclesException(e);
                }
            }
        }
        return pivotTable;
    }

    private Object[] defineParams(Class<?> clazz, PivotTable pivotTable, CatchParms p) {
        Object[] params = null;
        if (clazz.equals(CalculationProcess1.class) || clazz.equals(CalculationProcess2.class)) {
            Object[] foundParams = { pivotTable, p.getTop() };
            params = foundParams;
        }
        if (clazz.equals(AestheticProcess.class) || clazz.equals(CalculationProcess3.class)) {
            Object[] foundParams = { pivotTable };
            params = foundParams;
        }
        if (clazz.equals(DevChartCreationProcess.class)) {
            Object[] foundParams = { pivotTable, p.getUn() };
            params = foundParams;
        }
        return params;
    }

    @Autowired
    public void setFishstatProcess(FishstatProcess fishstatProcess) {
        classMap.put(FishstatProcess.class, fishstatProcess);
        this.fishstatProcess = fishstatProcess;
    }

}
