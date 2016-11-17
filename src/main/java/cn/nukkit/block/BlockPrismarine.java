package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;


public class BlockPrismarine extends BlockSolidMeta {
	

    public static final int NORMAL = 0;
    public static final int BRICKS = 1;
    public static final int DARK = 2;


    public BlockPrismarine() {
        this(0);
    }

    public BlockPrismarine(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PRISMARINE;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Prismarine",
                "Prismarine bricks",
                "Dark prismarine"
        };
        return names[this.meta & 0x07];
    }

    @Override
    public int[][] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new int[][]{
                    {Item.PRISMARINE, this.getDamage(), 1}
            };
        } else {
            return new int[0][0];
        }
    }

}
