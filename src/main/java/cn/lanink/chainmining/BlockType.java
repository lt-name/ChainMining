package cn.lanink.chainmining;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import lombok.Getter;

/**
 * @author lt_name
 */
public enum BlockType {
    
    WOOD("enabledWood", BlockWood.class, "blockShowName_wood"),
    GOLD("enabledGold", BlockOreGold.class, "blockShowName_gold"),
    IRON("enabledIron", BlockOreIron.class, "blockShowName_iron"),
    COAL("enabledCoal", BlockOreCoal.class, "blockShowName_coal"),
    LAPIS("enabledLapis", BlockOreLapis.class, "blockShowName_lapis"),
    DIAMOND("enabledDiamond", BlockOreDiamond.class, "blockShowName_diamond"),
    REDSTONE("enabledRedstone", BlockOreRedstone.class, "blockShowName_redstone"),
    EMERALD("enabledEmerald", BlockOreEmerald.class, "blockShowName_emerald"),
    OREQUARTZ("enabledOreQuartz", BlockOreQuartz.class, "blockShowName_OreQuartz"),
    OREGOLDNETHER("enabledOreGoldNether", BlockOreGoldNether.class, "blockShowName_OreGoldNether");
    
    @Getter
    private final String configKey;
    @Getter
    private final Class<? extends Block> block;
    private final String blockShowName;
    
    BlockType(String configKey, Class<? extends Block> block, String blockShowName) {
        this.configKey = configKey;
        this.block = block;
        this.blockShowName = blockShowName;
    }

    public String getShowName() {
        return this.getShowName(null);
    }

    public String getShowName(Player player) {
        return ChainMining.getInstance().getLanguage(player).translateString(this.blockShowName);
    }
    
}
