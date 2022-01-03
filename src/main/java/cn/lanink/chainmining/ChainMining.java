package cn.lanink.chainmining;

import cn.lanink.chainmining.config.PlayerConfig;
import cn.lanink.chainmining.config.PluginConfig;
import cn.lanink.chainmining.form.ChainMiningForm;
import cn.lanink.chainmining.form.FormListener;
import cn.lanink.chainmining.utils.Language;
import cn.lanink.chainmining.utils.MetricsLite;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author lt_name
 */
public class ChainMining extends PluginBase {
    
    public static final String VERSION = "?";
    private static ChainMining chainMining;

    private final HashMap<Player, PlayerConfig> playerConfigMap = new HashMap<>();
    
    @Getter
    private PluginConfig pluginConfig;
    @Getter
    private BlockManager blockManager;
    
    private final HashMap<String, Language> languageMap = new HashMap<>();
    
    public static ChainMining getInstance() {
        return chainMining;
    }
    
    @Override
    public void onLoad() {
        chainMining = this;
    
        File file = new File(this.getDataFolder() + "/PlayerConfig");
        if (!file.exists() && !file.mkdirs()) {
            this.getLogger().error("PlayerConfig 文件夹创建失败，玩家配置可能无法正常保存！");
        }
        
        this.saveDefaultConfig();
        this.saveResource("block.yml");
        this.pluginConfig = new PluginConfig(this.getConfig());
        
        this.loadLanguages();
    }
    
    private void loadLanguages() {
        List<String> list = Arrays.asList("zh_CN", "en_US");
        for (String string : list) {
            this.saveResource("Language/" + string + ".properties");
        }
    
        File[] files = new File(this.getDataFolder() + "/Language").listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName().split("\\.")[0];
                    Language language = new Language(file);
                    this.languageMap.put(name, language);
                    if (list.contains(name)) {
                        Config config = new Config(Config.PROPERTIES);
                        if (config.load(this.getResource("Language/" + name + ".properties"))) {
                            language.update(config);
                        }
                    }
                    this.getLogger().info("§aLanguage: " + name + " loaded !");
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.blockManager = new BlockManager(this);

        this.getServer().getPluginManager().registerEvents(new FormListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        
        try {
            new MetricsLite(this, 11328);
        }catch (Exception ignored) {
        
        }
        
        this.getLogger().info("§eChainMining §aEnabled! Version:" + VERSION);
        this.getLogger().warning("§eChainMining §ais a §efree §aplugin, github: https://github.com/lt-name/ChainMining");
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
    
    public Language getLanguage() {
        return this.getLanguage(null);
    }
    
    public Language getLanguage(Player player) {
        if (player != null) {
            String languageCode = player.getLoginChainData().getLanguageCode();
            if (this.languageMap.containsKey(languageCode)) {
                return this.languageMap.get(languageCode);
            }
        }
        return this.languageMap.get(this.pluginConfig.getDefaultLanguage());
    }
    
}
