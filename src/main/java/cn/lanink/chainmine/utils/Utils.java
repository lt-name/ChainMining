package cn.lanink.chainmine.utils;

import cn.lanink.chainmine.BlockType;

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
     * @return 名称
     */
    public static String getBlockTypeShowName(BlockType blockType) {
        switch (blockType) {
            case WOOD:
                return "木头";
            case GOLD:
                return "金矿";
            case IRON:
                return "铁矿";
            case COAL:
                return "煤矿";
            case LAPIS:
                return "青金石矿";
            case DIAMOND:
                return "钻石矿";
            case REDSTONE:
                return "红石矿";
            case EMERALD:
                return "绿宝石矿";
            default:
                return blockType.getConfigKey();
        }
    }
    
}
