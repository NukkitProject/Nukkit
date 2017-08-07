package cn.nukkit.level.generator;

import cn.nukkit.block.*;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.biome.Biome;
import cn.nukkit.level.generator.biome.BiomeSelector;
import cn.nukkit.level.generator.noise.Simplex;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.*;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

import java.util.*;

/**
 * This generator was written by Creeperface and NycuRO
 * <p>
 * The following classes are theirs and are intended for NUKKIT USAGE and should not be copied/translated to other software
 * such as BukkitPE, ClearSky, Genisys , Pocketmine-MP
 * <p>
 * Normal.java
 * MushroomPopulator.java
 * DarkOakTreePopulator.java
 * JungleBigTreePopulator.java
 * JungleTreePopulaotr.java
 * SavannaTreePopulator.java
 * SwampTreePopulator.java
 * BasicPopulator.java
 * MesaBiome.java
 * JungleBiome.java
 * SavannahBiome.java
 * RoofedForestBiome.java
 * RoofedForestMBiome.java
 * MushroomIslandBiome.java
 * TreeGenerator.java
 * HugeTreesGenerator.java
 * <p>
 * Created by CreeperFace on 26. 10. 2016.
 */
public class Normal extends Generator {

    @Override
    public int getId() {
        return TYPE_INFINITE;
    }

    private final List<Populator> populators = new ArrayList<>();

    private ChunkManager level;

    Random random = new Random();
    private NukkitRandom nukkitRandom;

    private long localSeed1;
    private long localSeed2;

    private final List<Populator> generationPopulators = new ArrayList<>();

    private Simplex noiseSeaFloor;
    private Simplex noiseLand;
    private Simplex noiseMountains;
    private Simplex noiseBaseGround;
    private Simplex noiseRiver;

    private BiomeSelector selector;

    private int heightOffset;

    private final int seaHeight = 62;
    private final int seaFloorHeight = 48;
    private final int beathStartHeight = 60;
    private final int beathStopHeight = 64;
    private final int bedrockDepth = 5;
    private final int seaFloorGenerateRange = 5;
    private final int landHeightRange = 18;
    private final int mountainHeight = 13;
    private final int basegroundHeight = 3;

    public Normal() {
        this(new HashMap<>());
    }

    public Normal(Map<String, Object> options) {
        //Nothing here. Just used for future update.
    }

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public String getName() {
        return "normal";
    }

    @Override
    public Map<String, Object> getSettings() {
        return new HashMap<>();
    }

    public Biome pickBiome(int x, int z) {
        long hash = x * 2345803 ^ z * 9236449 ^ this.level.getSeed();
        hash *= hash + 223;

        long xNoise = hash >> 20 & 3;
        long zNoise = hash >> 22 & 3;

        if (xNoise == 3) {
            xNoise = 1;
        }
        if (zNoise == 3) {
            zNoise = 1;
        }

        return this.selector.pickBiome(x + xNoise - 1, z + zNoise - 1);
    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
        this.nukkitRandom = random;
        this.random = new Random();
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.localSeed1 = this.random.nextLong();
        this.localSeed2 = this.random.nextLong();
        this.noiseSeaFloor = new Simplex(this.nukkitRandom, 1F, 1F / 8F, 1F / 64F);
        this.noiseLand = new Simplex(this.nukkitRandom, 2F, 1F / 8F, 1F / 512F);
        this.noiseMountains = new Simplex(this.nukkitRandom, 4F, 1F, 1F / 500F);
        this.noiseBaseGround = new Simplex(this.nukkitRandom, 4F, 1F / 4F, 1F / 64F);
        this.noiseRiver = new Simplex(this.nukkitRandom, 2F, 1F, 1F / 512F);
        this.nukkitRandom.setSeed(this.level.getSeed());
        this.selector = new BiomeSelector(this.nukkitRandom, Biome.getBiome(Biome.OCEAN));
        this.heightOffset = random.nextRange(-5, 3);

        this.selector.addBiome(Biome.getBiome(Biome.OCEAN));
        this.selector.addBiome(Biome.getBiome(Biome.PLAINS));
        this.selector.addBiome(Biome.getBiome(Biome.DESERT));
        this.selector.addBiome(Biome.getBiome(Biome.EXTREME_HILLS));
        this.selector.addBiome(Biome.getBiome(Biome.FOREST));
        this.selector.addBiome(Biome.getBiome(Biome.TAIGA));
        this.selector.addBiome(Biome.getBiome(Biome.SWAMPLAND));
        this.selector.addBiome(Biome.getBiome(Biome.RIVER));
        this.selector.addBiome(Biome.getBiome(Biome.HELL));
        /**
         * 9
         * 10
         */
        this.selector.addBiome(Biome.getBiome(Biome.FROZEN_RIVER));
        this.selector.addBiome(Biome.getBiome(Biome.ICE_FLATS));
        /** 13 **/
        this.selector.addBiome(Biome.getBiome(Biome.MUSHROOM_ISLAND));
        this.selector.addBiome(Biome.getBiome(Biome.MUSHROOM_ISLAND_SHORE));
        this.selector.addBiome(Biome.getBiome(Biome.BEACHES));
        /**
         * 17
         * 18
         * 19
         * 20
         */
        this.selector.addBiome(Biome.getBiome(Biome.JUNGLE));
        /** 22 */
        this.selector.addBiome(Biome.getBiome(Biome.JUNGLE_EDGE));
        this.selector.addBiome(Biome.getBiome(Biome.DEEP_OCEAN));
        this.selector.addBiome(Biome.getBiome(Biome.STONE_BEACH));
        this.selector.addBiome(Biome.getBiome(Biome.COLD_BEACH));
        this.selector.addBiome(Biome.getBiome(Biome.BIRCH_FOREST));
        /** 28 */
        this.selector.addBiome(Biome.getBiome(Biome.ROOFED_FOREST));
        this.selector.addBiome(Biome.getBiome(Biome.TAIGA_COLD));
        /** 31 */
        this.selector.addBiome(Biome.getBiome(Biome.MEGA_TAIGA));
        /** 33 */
        this.selector.addBiome(Biome.getBiome(Biome.EXTREME_HILLS_WITH_TREES));
        this.selector.addBiome(Biome.getBiome(Biome.SAVANNA));
        /** 36 */
        this.selector.addBiome(Biome.getBiome(Biome.MESA));
        /** 38 */
        this.selector.addBiome(Biome.getBiome(Biome.MESA_PLATEAU));
        /**
         * 40 - 126
         */
        this.selector.addBiome(Biome.getBiome(Biome.THE_VOID));
        /** 128 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_PLAINS));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_DESERT));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_EXTREME_HILLS));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_FOREST));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_TAIGA));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_SWAMPLAND));
        /**
         * 135 - 139
         */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_ICE_FLATS));
        /**
         * 141 - 148
         */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_JUNGLE));
        /** 150 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_JUNGLE_EDGE));
        /**
         * 152 - 156
         */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_ROOFED_FOREST));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_TAIGA_COLD));
        /** 159 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_REDWOOD_TAIGA));
        /** 161 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_EXTREME_HILLS_WITH_TREES));
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_SAVANNA));
        /** 164 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_MESA));
        /** 166 */
        this.selector.addBiome(Biome.getBiome(Biome.MUTATED_MESA_CLEAR_ROCK));
        this.selector.recalculate();


        PopulatorCaves caves = new PopulatorCaves();
        this.populators.add(caves);

        PopulatorRavines ravines = new PopulatorRavines();
        this.populators.add(ravines);

        //PopulatorDungeon dungeons = new PopulatorDungeon();
        //this.populators.add(dungeons);

        PopulatorGroundCover cover = new PopulatorGroundCover();
        this.generationPopulators.add(cover);

        PopulatorOre ores = new PopulatorOre();
        ores.setOreTypes(new OreType[]{
                new OreType(new BlockOreCoal(), 20, 17, 0, 128),
                new OreType(new BlockOreIron(), 20, 9, 0, 64),
                new OreType(new BlockOreRedstone(), 8, 8, 0, 16),
                new OreType(new BlockOreLapis(), 1, 7, 0, 16),
                new OreType(new BlockOreGold(), 2, 9, 0, 32),
                new OreType(new BlockOreDiamond(), 1, 8, 0, 16),
                new OreType(new BlockDirt(), 10, 33, 0, 128),
                new OreType(new BlockGravel(), 8, 33, 0, 128),
                new OreType(new BlockStone(BlockStone.GRANITE), 10, 33, 0, 80),
                new OreType(new BlockStone(BlockStone.DIORITE), 10, 33, 0, 80),
                new OreType(new BlockStone(BlockStone.ANDESITE), 10, 33, 0, 80)
        });
        this.populators.add(ores);
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(chunkX * localSeed1 ^ chunkZ * localSeed2 ^ this.level.getSeed());

        double[][] seaFloorNoise = Generator.getFastNoise2D(this.noiseSeaFloor, 16, 16, 4, chunkX * 16, 0, chunkZ * 16);
        double[][] landNoise = Generator.getFastNoise2D(this.noiseLand, 16, 16, 4, chunkX * 16, 0, chunkZ * 16);
        double[][] mountainNoise = Generator.getFastNoise2D(this.noiseMountains, 16, 16, 4, chunkX * 16, 0, chunkZ * 16);
        double[][] baseNoise = Generator.getFastNoise2D(this.noiseBaseGround, 16, 16, 4, chunkX * 16, 0, chunkZ * 16);
        double[][] riverNoise = Generator.getFastNoise2D(this.noiseRiver, 16, 16, 4, chunkX * 16, 0, chunkZ * 16);

        FullChunk chunk = this.level.getChunk(chunkX, chunkZ);

        for (int genx = 0; genx < 16; genx++) {
            for (int genz = 0; genz < 16; genz++) {

                Biome biome;
                boolean canBaseGround = false;
                boolean canRiver = true;

                //using a quadratic function which smooth the world
                //y = (2.956x)^2 - 0.6,  (0 <= x <= 2)
                double landHeightNoise = landNoise[genx][genz] + 1F;
                landHeightNoise *= 2.956;
                landHeightNoise = landHeightNoise * landHeightNoise;
                landHeightNoise = landHeightNoise - 0.6F;
                landHeightNoise = landHeightNoise > 0 ? landHeightNoise : 0;

                //generate mountains
                double mountainHeightGenerate = mountainNoise[genx][genz] - 0.2F;
                mountainHeightGenerate = mountainHeightGenerate > 0 ? mountainHeightGenerate : 0;
                int landHeightGenerate = (int) (landHeightRange * landHeightNoise);
                int mountainGenerate = (int) (mountainHeight * mountainHeightGenerate);

                if (landHeightGenerate > landHeightRange) {
                    if (landHeightGenerate > landHeightRange) {
                        canBaseGround = true;
                    }
                    landHeightGenerate = landHeightRange;
                }
                int genyHeight = seaFloorHeight + landHeightGenerate;
                genyHeight += mountainGenerate;

                //prepare for generate ocean, desert, and land
                if (genyHeight < beathStartHeight) {
                    if (genyHeight < beathStartHeight - 5) {
                        genyHeight += (int) (seaFloorGenerateRange * seaFloorNoise[genx][genz]);
                    }
                    biome = Biome.getBiome(Biome.OCEAN);
                    if (genyHeight < seaFloorHeight - seaFloorGenerateRange) {
                        genyHeight = seaFloorHeight;
                    }
                    canRiver = false;
                } else if (genyHeight <= beathStopHeight && genyHeight >= beathStartHeight) {
                    biome = Biome.getBiome(Biome.BEACHES);
                } else {
                    biome = this.pickBiome(chunkX * 16 + genx, chunkZ * 16 + genz);
                    if (canBaseGround) {
                        int baseGroundHeight = (int) (landHeightRange * landHeightNoise) - landHeightRange;
                        int baseGroundHeight2 = (int) (basegroundHeight * (baseNoise[genx][genz] + 1F));
                        if (baseGroundHeight2 > baseGroundHeight) baseGroundHeight2 = baseGroundHeight;
                        if (baseGroundHeight2 > mountainGenerate)
                            baseGroundHeight2 = baseGroundHeight2 - mountainGenerate;
                        else baseGroundHeight2 = 0;
                        genyHeight += baseGroundHeight2;
                    }
                }
                if (canRiver && genyHeight <= seaHeight - 5) {
                    canRiver = false;
                }
                //generate river
                if (canRiver) {
                    double riverGenerate = riverNoise[genx][genz];
                    if (riverGenerate > -0.25F && riverGenerate < 0.25F) {
                        riverGenerate = riverGenerate > 0 ? riverGenerate : -riverGenerate;
                        riverGenerate = 0.25F - riverGenerate;
                        //y=x^2 * 4 - 0.0000001
                        riverGenerate = riverGenerate * riverGenerate * 4F;
                        //smooth again
                        riverGenerate = riverGenerate - 0.0000001F;
                        riverGenerate = riverGenerate > 0 ? riverGenerate : 0;
                        genyHeight -= riverGenerate * 64;
                        if (genyHeight < seaHeight) {
                            biome = Biome.getBiome(Biome.RIVER);
                            //to generate river floor
                            if (genyHeight <= seaHeight - 8) {
                                int genyHeight1 = seaHeight - 9 + (int) (basegroundHeight * (baseNoise[genx][genz] + 1F));
                                int genyHeight2 = genyHeight < seaHeight - 7 ? seaHeight - 7 : genyHeight;
                                genyHeight = genyHeight1 > genyHeight2 ? genyHeight1 : genyHeight2;
                            }
                        }
                    }
                }
                chunk.setBiomeId(genx, genz, biome.getId());
                //biome color
                //todo: smooth chunk color
                int biomecolor = biome.getColor();
                chunk.setBiomeColor(genx, genz, (biomecolor >> 16), (biomecolor >> 8) & 0xff, (biomecolor & 0xff));
                //generating
                int generateHeight = genyHeight > seaHeight ? genyHeight : seaHeight;
                for (int geny = 0; geny <= generateHeight; geny++) {
                    if (geny <= bedrockDepth && (geny == 0 || nukkitRandom.nextRange(1, 5) == 1)) {
                        chunk.setBlock(genx, geny, genz, Block.BEDROCK);
                    } else if (geny > genyHeight) {
                        if ((biome.getId() == Biome.ICE_FLATS || biome.getId() == Biome.TAIGA) && geny >= seaHeight) {
                            chunk.setBlock(genx, geny, genz, Block.ICE);
                        } else {
                            chunk.setBlock(genx, geny, genz, Block.STILL_WATER);
                        }
                    /*} else if (biome.getId() == Biome.getBiome(Biome.RIVER)) {
                        if (biome.getId() == Biome.getBiome(Biome.SWAMPLAND || biome.getId() == Biome.getBiome(Biome.MUTATED_SWAMPLAND))) {
                            if (biome.getId() == Biome.getBiome(Biome.SWAMPLAND) || biome.getId() == Biome.getBiome(Biome.MUTATED_SWAMPLAND >=
                            biome.getId() == Biome.getBiome(Biome.RIVER))){
                                chunk.setBiomeColor(genx, genz, 106, 112, 57);
                            } else {
                                // Nothing.
                            }
                        } else {
                            if (biome.getId() == Biome.getBiome(Biome.ICE_FLATS) || biome.getId() == Biome.getBiome(Biome.TAIGA) && geny == seaHeight) {
                                chunk.setBlock(genx, geny, genz, Block.ICE);
                            }
                        }*/
                    } else {
                            chunk.setBlock(genx, geny, genz, Block.STONE);
                    }
                }
            }
        }

        //populator chunk
        for (Populator populator : this.generationPopulators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom);
        }

    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        this.nukkitRandom.setSeed(0xdeadbeef ^ (chunkX << 8) ^ chunkZ ^ this.level.getSeed());
        for (Populator populator : this.populators) {
            populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom);
        }

        FullChunk chunk = this.level.getChunk(chunkX, chunkZ);
        Biome biome = Biome.getBiome(chunk.getBiomeId(7, 7));
        biome.populateChunk(this.level, chunkX, chunkZ, this.nukkitRandom);
    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(127.5, 256, 127.5);
    }
}