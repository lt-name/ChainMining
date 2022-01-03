package cn.lanink.chainmining.form.element;

import lombok.Getter;

/**
 * @author lt_name
 */
public class ElementToggle extends cn.nukkit.form.element.ElementToggle {
    
    @Getter
    private final String blockInfoName;
    
    public ElementToggle(String text, boolean defaultValue, String blockInfoName) {
        super(text, defaultValue);
        this.blockInfoName = blockInfoName;
    }
    
}
