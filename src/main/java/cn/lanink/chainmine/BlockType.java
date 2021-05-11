package cn.lanink.chainmine;

import cn.nukkit.block.*;
import lombok.Getter;

/**
 * @author lt_name
 */
public enum BlockType {
    
    WOOD("enabledWood", new BlockWood()),
    GOLD("enabledGold", new BlockOreGold()),
    IRON("enabledIron", new BlockOreIron()),
    COAL("enabledCoal", new BlockOreCoal()),
    LAPIS("enabledLapis", new BlockOreLapis()),
    DIAMOND("enabledDiamond", new BlockOreDiamond()),
    REDSTONE("enabledRedstone", new BlockOreRedstone()),
    REDSTONEGLOWING("enabledRedstone", new BlockOreRedstoneGlowing()),
    EMERALD("enabledEmerald", new BlockOreEmerald());
    
    @Getter
    private final String configKey;
    @Getter
    private final Block block;
    
    BlockType(String configKey, Block block) {
        this.configKey = configKey;
        this.block = block;
    }
    
}
