package cn.lunadeer.dominion.utils.scui.configuration;

import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;
import org.bukkit.Material;

import java.util.List;

public class ListViewConfiguration extends ConfigurationPart {
    public ListViewConfiguration(
            String title,
            char itemSymbol,
            List<String> layout,
            ButtonConfiguration previewButton,
            ButtonConfiguration nextButton
    ) {
        this.title = title;
        this.itemSymbol = String.valueOf(itemSymbol);
        this.layout = layout;
        this.previewButton = previewButton;
        this.nextButton = nextButton;
    }

    public ListViewConfiguration(
            String title,
            char itemSymbol,
            List<String> layout
    ) {
        this.title = title;
        this.itemSymbol = String.valueOf(itemSymbol);
        this.layout = layout;
    }

    public String title = "List";
    public String itemSymbol = "i";
    public List<String> layout = List.of(
            "#########",
            "#iiiiiii#",
            "#iiiiiii#",
            "#iiiiiii#",
            "#p#####n#"
    );
    public ButtonConfiguration previewButton = new ButtonConfiguration('p', Material.ARROW, "<<<",
            List.of("Page: {0}/{1}"));
    public ButtonConfiguration nextButton = new ButtonConfiguration('n', Material.ARROW, ">>>",
            List.of("Page: {0}/{1}"));
}
