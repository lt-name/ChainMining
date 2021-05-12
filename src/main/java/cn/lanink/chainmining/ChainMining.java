package cn.lanink.chainmining;

import cn.lanink.chainmining.config.PlayerConfig;
import cn.lanink.chainmining.config.PluginConfig;
import cn.lanink.chainmining.form.ChainMiningForm;
import cn.lanink.chainmining.form.FormListener;
import cn.lanink.chainmining.utils.MetricsLite;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

/**
 * @author lt_name
 */
public class ChainMining extends PluginBase {
    
    public static final String VERSION = "?";
    private static ChainMining chainMining;
    
    private final HashMap<Player, PlayerConfig> playerConfigMap = new HashMap<>();
    
    @Getter
    private PluginConfig pluginConfig;
    
    public static ChainMining getInstance() {
        return chainMining;
    }
    
    @Override
    public void onLoad() {
        chainMining = this;
    
        File file = new File(this.getDataFolder() + "/PlayerConfig");
        if (!file.exists() && !file.mkdirs()) {
            this.getLogger().error("PlayerConfig 文件夹创建失败");
        }
        
        this.saveDefaultConfig();
        this.pluginConfig = new PluginConfig(this.getConfig());
    }
    
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new FormListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        
        try {
            new MetricsLite(this, 11328);
        }catch (Exception ignored) {
        
        }
        
        this.getLogger().info("插件加载完成！版本：" + VERSION);
    }
    
    @Override
    public void onDisable() {
        for (PlayerConfig config : this.playerConfigMap.values()) {
            config.save();
        }
        this.playerConfigMap.clear();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isPlayer()) {
            Player player = (Player) sender;
            player.showFormWindow(new ChainMiningForm(player));
        }else {
            sender.sendMessage("§c请在游戏内使用此命令！");
        }
        return true;
    }
    
    public PlayerConfig getPlayerConfig(@NotNull Player player) {
        if (!this.playerConfigMap.containsKey(player)) {
            Config config = new Config(
                    this.getDataFolder() + "/PlayerConfig/" + player.getName() + ".yml", Config.YAML);
            this.playerConfigMap.put(player, new PlayerConfig(player, config));
        }
        return this.playerConfigMap.get(player);
    }
    
    public void removePlayerConfigCache(@NotNull Player player) {
        if (this.playerConfigMap.containsKey(player)) {
            PlayerConfig config = this.playerConfigMap.get(player);
            config.save();
            this.playerConfigMap.remove(player);
        }
    }
    
}
