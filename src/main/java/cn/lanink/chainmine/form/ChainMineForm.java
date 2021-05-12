package cn.lanink.chainmine.form;

import cn.lanink.chainmine.BlockType;
import cn.lanink.chainmine.ChainMine;
import cn.lanink.chainmine.config.PlayerConfig;
import cn.lanink.chainmine.form.element.ElementToggle;
import cn.lanink.chainmine.utils.Utils;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import org.jetbrains.annotations.NotNull;

/**
 * @author lt_name
 */
public class ChainMineForm extends FormWindowCustom {
    
    public ChainMineForm(@NotNull Player player) {
        super("ChainMine");
    
        this.addElement(new ElementLabel("设置连锁方块："));
        
        PlayerConfig config = ChainMine.getInstance().getPlayerConfig(player);
        for (BlockType blockType : BlockType.values()) {
            this.addElement(
                    new ElementToggle(Utils.getBlockTypeShowName(blockType),
                            config.needChainMine(blockType),
                            blockType));
        }
    }
    
}
