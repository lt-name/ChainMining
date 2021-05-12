package cn.lanink.chainmining.config;

import cn.lanink.chainmining.BlockType;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.utils.Config;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author lt_name
 */
@Getter
@EqualsAndHashCode(of = "player")
public class PlayerConfig {
    
    private final Player player;
    private final Config config;
    
    private final EnumMap<BlockType, Boolean> enabledMap = new EnumMap<>(BlockType.class);
    
    public PlayerConfig(@NotNull Player player, @NotNull Config config) {
        this.player = player;
        this.config = config;
    
        for (BlockType blockType : BlockType.values()) {
            this.enabledMap.put(blockType, config.getBoolean(blockType.getConfigKey(), true));
        }
    }
    
    public boolean enabledChainMining(@NotNull Block block) {
        for (Map.Entry<BlockType, Boolean> entry : this.enabledMap.entrySet()) {
            if (entry.getKey().getBlock().isInstance(block)) {
                return entry.getValue();
            }
        }
        return false;
    }
    
    public boolean enabledChainMining(@NotNull BlockType blockType) {
        return this.enabledMap.getOrDefault(blockType, false);
    }
    
    public void save() {
        for (Map.Entry<BlockType, Boolean> entry : this.enabledMap.entrySet()) {
            this.config.set(entry.getKey().getConfigKey(), entry.getValue());
        }
        this.config.save();
    }
    
}
