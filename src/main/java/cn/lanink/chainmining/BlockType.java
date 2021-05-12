package cn.lanink.chainmining;

import cn.nukkit.block.*;
import lombok.Getter;

/**
 * @author lt_name
 */
public enum BlockType {
    
    WOOD("enabledWood", BlockWood.class),
    GOLD("enabledGold", BlockOreGold.class),
    IRON("enabledIron", BlockOreIron.class),
    COAL("enabledCoal", BlockOreCoal.class),
    LAPIS("enabledLapis", BlockOreLapis.class),
    DIAMOND("enabledDiamond", BlockOreDiamond.class),
    REDSTONE("enabledRedstone", BlockOreRedstone.class),
    EMERALD("enabledEmerald", BlockOreEmerald.class);
    
    @Getter
    private final String configKey;
    @Getter
    private final Class<? extends Block> block;
    
    BlockType(String configKey, Class<? extends Block> block) {
        this.configKey = configKey;
        this.block = block;
    }
    
}
