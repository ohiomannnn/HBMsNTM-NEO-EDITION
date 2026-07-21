package api.hbm.tile;

public interface IHeatSource {
    int getHeatStored();
    void useUpHeat(int heat);
}
