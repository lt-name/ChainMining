package cn.lanink.chainmining.config;

import cn.lanink.chainmining.BlockManager;
import cn.lanink.chainmining.ChainMining;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.utils.Config;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lt_name
 */
@Getter
@EqualsAndHashCode(of = "player")
public class PlayerConfig {
    
    private final Player player;
    private final Config config;
    
    private final LinkedHashMap<BlockManager.BlockInfo, Boolean> enabledMap = new LinkedHashMap<>();
    
    public PlayerConfig(@NotNull Player player, @NotNull Config config) {
        this.player = player;
        this.config = config;

        for (BlockManager.BlockInfo blockInfo : ChainMining.getInstance().getBlockManager().getBlockList()) {
            this.enabledMap.put(blockInfo, config.getBoolean(blockInfo.getName()));
        }
    }
    
    public boolean enabledChainMining(@NotNull Block block) {
        for (Map.Entry<BlockManager.BlockInfo, Boolean> entry : this.enabledMap.entrySet()) {
            if (entry.getKey().getBlockClass().isInstance(block)) {
                return entry.getValue();
            }
        }
        return false;
    }
    
    public boolean enabledChainMining(@NotNull BlockManager.BlockInfo blockInfo) {
        return this.enabledMap.getOrDefault(blockInfo, false);
    }
    
    public void save() {
        for (Map.Entry<BlockManager.BlockInfo, Boolean> entry : this.enabledMap.entrySet()) {
            this.config.set(entry.getKey().getName(), entry.getValue());
        }
        this.config.save();
    }
    
}
