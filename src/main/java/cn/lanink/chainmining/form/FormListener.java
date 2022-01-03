package cn.lanink.chainmining.form;

import cn.lanink.chainmining.BlockManager;
import cn.lanink.chainmining.ChainMining;
import cn.lanink.chainmining.config.PlayerConfig;
import cn.lanink.chainmining.form.element.ElementToggle;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.Element;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public class FormListener implements Listener {
    
    private final ChainMining chainMining;
    
    public FormListener(ChainMining chainMining) {
        this.chainMining = chainMining;
    }
    
    @EventHandler
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getResponse() == null) {
            return;
        }
        if (event.getWindow() instanceof ChainMiningForm) {
            PlayerConfig config = this.chainMining.getPlayerConfig(player);
            ChainMiningForm window = (ChainMiningForm) event.getWindow();
            
            int i = 0;
            for (Element element: window.getElements()) {
                if (element instanceof ElementToggle) {
                    BlockManager.BlockInfo blockInfo = this.chainMining.getBlockManager().getBlockInfoByName(((ElementToggle) element).getBlockInfoName());
                    if (blockInfo != null) {
                        config.getEnabledMap().put(blockInfo, window.getResponse().getToggleResponse(i));
                    }
                }
                i++;
            }
            config.save();
            
            //重新打开设置界面 显示修改后的配置
            player.showFormWindow(new ChainMiningForm(player));
        }
        
        
    }
    
}
