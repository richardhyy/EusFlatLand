package cc.eumc.eusflatland;

import cc.eumc.eusflatland.blueprint.EusBlueprint;
import org.bukkit.Material;
import org.bukkit.block.Biome;

public class FlatLandStructure {
    EusBlueprint blueprint;
    int yOffset;
    Material[] placeOnMaterials;
    Material fillBaseWith;
    Biome[] biomes;
    int chance;

    public FlatLandStructure(EusBlueprint blueprint, int yOffset, Material[] placeOnMaterials, Material fillBaseWith, Biome[] biomes, int chance) {
        this.blueprint = blueprint;
        this.yOffset = yOffset;
        this.placeOnMaterials = placeOnMaterials;
        this.fillBaseWith = fillBaseWith;
        this.biomes = biomes;
        this.chance = chance;
    }

    public EusBlueprint getBlueprint() {
        return blueprint;
    }

    public int getYOffset() {
        return yOffset;
    }

    public Material[] getPlaceOnMaterials() {
        return placeOnMaterials;
    }

    public Material getFillBaseWith() {
        return fillBaseWith;
    }

    public Biome[] getBiomes() {
        return biomes;
    }

    public int getChance() {
        return chance;
    }
}
