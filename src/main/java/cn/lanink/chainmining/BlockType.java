package cn.lanink.chainmining;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import lombok.Getter;

/**
 * @author lt_name
 */
public enum BlockType {
    
    WOOD("enabledWood", "blockShowName_wood",
            new Class[]{BlockWood.class,
                    BlockHyphaeCrimson.class, BlockHyphaeStrippedCrimson.class,
                    BlockHyphaeWarped.class, BlockHyphaeStrippedWarped.class}),
    GOLD("enabledGold", "blockShowName_gold", new Class[]{BlockOreGold.class}),
    IRON("enabledIron", "blockShowName_iron", new Class[]{BlockOreIron.class}),
    COAL("enabledCoal", "blockShowName_coal", new Class[]{BlockOreCoal.class}),
    LAPIS("enabledLapis", "blockShowName_lapis", new Class[]{BlockOreLapis.class}),
    DIAMOND("enabledDiamond", "blockShowName_diamond", new Class[]{BlockOreDiamond.class}),
    REDSTONE("enabledRedstone", "blockShowName_redstone", new Class[]{BlockOreRedstone.class}),
    EMERALD("enabledEmerald", "blockShowName_emerald", new Class[]{BlockOreEmerald.class}),
    OREQUARTZ("enabledOreQuartz", "blockShowName_OreQuartz", new Class[]{BlockOreQuartz.class}),
    OREGOLDNETHER("enabledOreGoldNether", "blockShowName_OreGoldNether", new Class[]{BlockOreGoldNether.class});
    
    @Getter
    private final String configKey;
    private final String blockShowName;
    @Getter
    private final Class<? extends Block>[] blockClass;

    BlockType(String configKey, String blockShowName, Class<? extends Block>[] blockClass) {
        this.configKey = configKey;
        this.blockShowName = blockShowName;
        this.blockClass = blockClass;
    }

    public String getShowName() {
        return this.getShowName(null);
    }

    public String getShowName(Player player) {
        return ChainMining.getInstance().getLanguage(player).translateString(this.blockShowName);
    }
    
}
