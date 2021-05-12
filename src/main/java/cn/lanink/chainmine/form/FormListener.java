package cn.lanink.chainmine.form;

import cn.lanink.chainmine.ChainMine;
import cn.lanink.chainmine.config.PlayerConfig;
import cn.lanink.chainmine.form.element.ElementToggle;
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
    
    private final ChainMine chainMine;
    
    public FormListener(ChainMine chainMine) {
        this.chainMine = chainMine;
    }
    
    @EventHandler
    public void onPlayerFormResponded(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getResponse() == null) {
            return;
        }
        if (event.getWindow() instanceof ChainMineForm) {
            PlayerConfig config = this.chainMine.getPlayerConfig(player);
            ChainMineForm window = (ChainMineForm) event.getWindow();
            
            int i = 0;
            for (Element element: window.getElements()) {
                if (element instanceof ElementToggle) {
                    config.getEnabledMap().put(((ElementToggle) element).getBlockType(),
                            window.getResponse().getToggleResponse(i));
                }
                i++;
            }
            config.save();
            
            //重新打开设置界面 显示修改后的配置
            player.showFormWindow(new ChainMineForm(player));
        }
        
        
    }
    
}
