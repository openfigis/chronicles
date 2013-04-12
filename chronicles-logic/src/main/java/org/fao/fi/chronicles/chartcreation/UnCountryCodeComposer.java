package org.fao.fi.chronicles.chartcreation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.fao.fi.pivot.PivotTableException;
import org.springframework.core.io.ClassPathResource;

import com.Ostermiller.util.CSVParse;
import com.Ostermiller.util.CSVParser;

public class UnCountryCodeComposer {

    private static Map<String, String> map = new HashMap<String, String>();

    static {
        ClassPathResource cpr = new ClassPathResource("uncountrycodes.csv");

        try {
            CSVParse csvParser = new CSVParser(cpr.getInputStream());
            String[][] table = csvParser.getAllValues();
            for (String[] row : table) {
                String code = row[0];
                String description = row[1];
                map.put(code, description);
                
            }
        } catch (FileNotFoundException e) {
            throw new PivotTableException(e);
        } catch (IOException e) {
            throw new PivotTableException(e);
        }
    }

    public static String composeName(String un[]) {
        String name = map.get(un[0]);
        int oneBeforeTheLast = un.length - 1;
        for (int i = 1; i < oneBeforeTheLast; i++) {
            name = name + ", " + map.get(un[i]);
        }
        if (un.length > 1) {
            name = name + " and " + map.get(un[oneBeforeTheLast]);
        }
        return name;
    }
}
