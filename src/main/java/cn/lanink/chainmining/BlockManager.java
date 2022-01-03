package cn.lanink.chainmining;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

/**
 * @author lt_name
 */
public class BlockManager {

    private final String classPrefix = "cn.nukkit.block.";
    private final ChainMining chainMining;

    private final ArrayList<String> blockClassNameList = new ArrayList<>();
    @Getter
    private final ArrayList<BlockInfo> blockList = new ArrayList<>();

    public BlockManager(ChainMining chainMining) {
        this.chainMining = chainMining;
        this.load();
    }

    private void load() {
        Config config = new Config(this.chainMining.getDataFolder() + "/block.yml", Config.YAML);
        this.blockClassNameList.clear();
        for (String className : config.getStringList("block")) {
            if (!className.contains(this.classPrefix)) {
                className = this.classPrefix + className;
            }
            if (!this.blockClassNameList.contains(className)) {
                this.blockClassNameList.add(className);
            }
        }
        for (String className : this.blockClassNameList) {
            try {
                this.blockList.add(new BlockInfo(className.replace(this.classPrefix, ""), className, Class.forName(className)));
            } catch (Exception e) {
                this.chainMining.getLogger().warning("Block: " + className + " does not exist or fails to load, chaining cannot be enabled!");
            }
        }
    }

    public BlockInfo getBlockInfoByName(String name) {
        for (BlockInfo blockInfo : this.blockList) {
            if (blockInfo.getName().equals(name)) {
                return blockInfo;
            }
        }
        return null;
    }

    @Data
    public static class BlockInfo {

        private final String name;
        private final String className;
        private final Class<?> blockClass;

        public String getShowName() {
            return this.getShowName(null);
        }

        public String getShowName(Player player) {
            return ChainMining.getInstance().getLanguage(player).translateString(this.name);
        }

    }

}
