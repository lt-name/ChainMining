package cn.lanink.chainmine.form.element;

import cn.lanink.chainmine.BlockType;
import lombok.Getter;

/**
 * @author lt_name
 */
public class ElementToggle extends cn.nukkit.form.element.ElementToggle {
    
    @Getter
    private final BlockType blockType;
    
    public ElementToggle(String text, boolean defaultValue, BlockType blockType) {
        super(text, defaultValue);
        this.blockType = blockType;
    }
    
}
