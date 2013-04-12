package org.fao.fi.chronicles.fishstatj;


/**
 * Reflects all the logic done in FishstatJ, for the chronicles project.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public interface FishstatProcess {

    /**
     * 
     * See for the un codes as well the http://www.fao.org/countryprofiles/geoinfo/ws/countryCodes/ITA
     * 
     * @param startYear
     * @param endYear
     * @param unCodes
     * @return
     */
    public FishstatProcessResult run(int startYear, int endYear, String[] unCodes);


}
