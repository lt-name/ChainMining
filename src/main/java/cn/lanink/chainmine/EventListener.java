package cn.lanink.chainmine;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;

import java.util.*;

/**
 * @author lt_name
 */
@SuppressWarnings("unused")
public class EventListener implements Listener {

    private final ChainMine chainMine;
    
    public EventListener(ChainMine chainMine) {
        this.chainMine = chainMine;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.chainMine.removePlayerConfigCache(event.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (player == null || block == null) {
            return;
        }
        
        PlayerConfig config = this.chainMine.getPlayerConfig(player);
        if (!config.needChainMine(block)) {
            return;
        }
        Level level = block.getLevel();
        
        ArrayList<Block> blocks = new ArrayList<>();
        LinkedList<Block> needCheckBlocks = new LinkedList<>();
        needCheckBlocks.add(block);
        Block b2;
        do {
            ListIterator<Block> iterator = needCheckBlocks.listIterator();
            while (iterator.hasNext()) {
                Block b = iterator.next();
                iterator.remove();
                
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(0, 0, 1)), iterator, b);
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(0, 0, -1)), iterator, b);
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(0, 1, 0)), iterator, b);
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(0, -1, 0)), iterator, b);
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(1, 0, 0)), iterator, b);
                check(config, blocks, needCheckBlocks, level.getBlock(b.add(-1, 0, 0)), iterator, b);
                
                if (config.needChainMine(b)) {
                    blocks.add(b);
                }
            }
        } while (!needCheckBlocks.isEmpty());
        
        blocks.remove(block); //不要额外计算正常掉落的方块
        ArrayList<Item> drops = new ArrayList<>(Arrays.asList(event.getDrops()));
        for (Block b : blocks) {
            level.setBlock(b, Block.get(BlockID.AIR));
            event.setDropExp(event.getDropExp() + b.getDropExp());
            Item newDropItem = Item.get(b.getId(), b.getDamage(), 1);
            boolean isNewItem = true;
            for (Item item : drops) {
                if (item.equals(newDropItem, true, true)) {
                    item.setCount(item.getCount() + newDropItem.getCount());
                    isNewItem = false;
                    break;
                }
            }
            if (isNewItem) {
                drops.add(newDropItem);
            }
        }
        event.setDrops(drops.toArray(new Item[0]));
    }
    
    private void check(PlayerConfig config, ArrayList<Block> blocks, LinkedList<Block> needCheckBlocks, Block b2, ListIterator<Block> iterator, Block b) {
        if (config.needChainMine(b2) || (config.needChainMine(b) && !b2.isNormalBlock())) {
            if (!needCheckBlocks.contains(b2) && !blocks.contains(b2)) {
                iterator.add(b2);
            }
        }
    }
    
}
