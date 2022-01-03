package cn.lanink.chainmining.form;

import cn.lanink.chainmining.BlockManager;
import cn.lanink.chainmining.ChainMining;
import cn.lanink.chainmining.config.PlayerConfig;
import cn.lanink.chainmining.form.element.ElementToggle;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import org.jetbrains.annotations.NotNull;

/**
 * @author lt_name
 */
public class ChainMiningForm extends FormWindowCustom {
    
    public ChainMiningForm(@NotNull Player player) {
        super("§l§7[§1C§2h§3a§4i§5n§6M§ai§cn§bi§dn§9g§7]");
    
        this.addElement(new ElementLabel(
                ChainMining.getInstance().getLanguage(player).translateString("GUI_PlayerConfigSet_tip")));
        
        PlayerConfig config = ChainMining.getInstance().getPlayerConfig(player);
        for (BlockManager.BlockInfo blockInfo : ChainMining.getInstance().getBlockManager().getBlockList()) {
            this.addElement(
                    new ElementToggle(
                            blockInfo.getShowName(player),
                            config.enabledChainMining(blockInfo),
                            blockInfo.getName()));
        }
    }
    
}
