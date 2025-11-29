package api.hbm.entity;

public interface IRadarDetectableNT {

    int TIER0 =		0;
    int TIER1 =		1;
    int TIER2 =		2;
    int TIER3 =		3;
    int TIER4 =		4;
    int TIER10 =	5;
    int TIER10_15 =	6;
    int TIER15 =	7;
    int TIER15_20 =	8;
    int TIER20 =	9;
    int TIER_AB =	10;
    int PLAYER =	11;
    int ARTY =		12;
    /** Reserved type that shows a unique purple blip. Used for when nothing else applies. */
    int SPECIAL =	13;

    /** Name use for radar display, uses I18n for lookup */
    String getUnlocalizedName();
    /** The type of dot to show on the radar as well as the redstone level in tier mode */
    int getBlipLevel();
    /** Whether the object can be seen by this type of radar */
    boolean canBeSeenBy(Object radar);
    /** Whether the object is currently visible, as well as whether the radar's setting allow for picking this up */
    boolean paramsApplicable(RadarScanParams params);
    /** Whether this radar entry should be counted for the redstone output */
    boolean suppliesRedstone(RadarScanParams params);

    class RadarScanParams {
        public boolean scanMissiles;
        public boolean scanShells;
        public boolean scanPlayers;
        public boolean smartMode;

        public RadarScanParams(boolean m, boolean s, boolean p, boolean smart) {
            this.scanMissiles = m;
            this.scanShells = s;
            this.scanPlayers = p;
            this.smartMode = smart;
        }
    }
}
