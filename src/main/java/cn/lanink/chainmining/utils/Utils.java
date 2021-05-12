package cn.lanink.chainmining.utils;

import cn.lanink.chainmining.BlockType;
import cn.lanink.chainmining.ChainMining;
import cn.nukkit.Player;

/**
 * @author lt_name
 */
public class Utils {
    
    private Utils() {
        throw new RuntimeException("Utils 不允许实例化");
    }
    
    /**
     * 获取方块类型显示名称
     *
     * @param blockType 方块类型
     * @param player 玩家
     * @return 名称
     */
    public static String getBlockTypeShowName(BlockType blockType, Player player) {
        switch (blockType) {
            case WOOD:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_wood");
            case GOLD:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_gold");
            case IRON:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_iron");
            case COAL:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_coal");
            case LAPIS:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_lapis");
            case DIAMOND:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_diamond");
            case REDSTONE:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_redstone");
            case EMERALD:
                return ChainMining.getInstance().getLanguage(player).translateString("blockShowName_emerald");
            default:
                return blockType.getConfigKey();
        }
    }
    
}
