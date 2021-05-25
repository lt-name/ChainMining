package cn.lanink.chainmining;

import cn.lanink.chainmining.config.PlayerConfig;
import cn.lanink.chainmining.config.PluginConfig;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public class EventListener implements Listener {

    private final ChainMining chainMining;
    private final PluginConfig pluginConfig;
    
    public EventListener(ChainMining chainMining) {
        this.chainMining = chainMining;
        this.pluginConfig = chainMining.getPluginConfig();
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.chainMining.removePlayerConfigCache(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Item item = event.getItem();
        Item[] drops = event.getDrops();
        if (player == null || block == null || item == null || drops == null) {
            return;
        }

        if (this.pluginConfig.isBanWorld(player.getLevel())) {
            return;
        }
        
        if (player.getGamemode() == Player.CREATIVE && !this.pluginConfig.isCreateModeEnable()) {
            return;
        }
        
        if (drops.length > 0) {
            this.chainMining(event, player, block, item);
        }
    }
    
    private void chainMining(@NotNull BlockBreakEvent event, @NotNull Player player, @NotNull Block block, @NotNull Item item) {
        PlayerConfig config = this.chainMining.getPlayerConfig(player);
        if (!config.enabledChainMining(block)) {
            return;
        }
        Level level = block.getLevel();
        
        ArrayList<Block> blocks = new ArrayList<>();
        LinkedBlockingQueue<Block> needCheckBlocks = new LinkedBlockingQueue<>();
        ArrayList<Block> completeCheck = new ArrayList<>();
        needCheckBlocks.add(block);
        
        Block nowBlock;
        while ((nowBlock = needCheckBlocks.poll()) != null) {
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(0, 0, 1)), nowBlock, block);
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(0, 0, -1)), nowBlock, block);
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(0, 1, 0)), nowBlock, block);
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(0, -1, 0)), nowBlock, block);
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(1, 0, 0)), nowBlock, block);
            check(blocks, needCheckBlocks, completeCheck, level.getBlock(nowBlock.add(-1, 0, 0)), nowBlock, block);
            
            if (config.enabledChainMining(nowBlock)) {
                blocks.add(nowBlock);
            }
            if (blocks.size() >= this.pluginConfig.getMaxLinkageCount()) {
                break;
            }
        }
        
        blocks.remove(block); //不要额外计算正常掉落的方块
        ArrayList<Item> drops = new ArrayList<>(Arrays.asList(event.getDrops()));
        for (Block b : blocks) {
            //耐久
            if (item.useOn(b)) {
                if (item.getDamage() >= item.getMaxDurability()) {
                    break;
                }
            }
            
            level.setBlock(b, Block.get(BlockID.AIR));
            event.setDropExp(event.getDropExp() + b.getDropExp());
            Item[] newDrops = b.getDrops(item);
            for (Item newDrop : newDrops) {
                boolean isNewItem = true;
                for (Item drop : drops) {
                    if (drop.equals(newDrop, true, true)) {
                        drop.setCount(drop.getCount() + newDrop.getCount());
                        isNewItem = false;
                        break;
                    }
                }
                if (isNewItem) {
                    drops.add(newDrop);
                }
            }
            
        }
        event.setDrops(drops.toArray(new Item[0]));
    }
    
    
    private void check(@NotNull ArrayList<Block> blocks,
                       @NotNull LinkedBlockingQueue<Block> needCheckBlocks,
                       @NotNull ArrayList<Block> completeCheck,
                       @NotNull Block nextBlock,
                       @NotNull Block nowBlock,
                       @NotNull Block firstBlock) {
        //防止重复检测
        if (completeCheck.contains(nextBlock) || blocks.contains(nextBlock)) {
            return;
        }
        completeCheck.add(nextBlock);
        
        if (firstBlock.getClass().isInstance(nextBlock)) {
            needCheckBlocks.offer(nextBlock);
            return;
        }
        
        if (this.pluginConfig.isScanNearby()) {
            if (!nextBlock.isNormalBlock()) {
                if (firstBlock.getClass().isInstance(nowBlock)) {
                    needCheckBlocks.offer(nextBlock);
                } else {
                    for (Block block : blocks) {
                        if (block.distance(nextBlock) < 3) {
                            needCheckBlocks.offer(nextBlock);
                            return;
                        }
                    }
                }
            }
        }
    }
    
}
