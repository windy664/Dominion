package cn.lunadeer.dominion.utils.scui.configuration;

import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;

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
    public ButtonConfiguration previewButton = new ButtonConfiguration('p',
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmVkOWQ1YzJiNDgwNzA1OGQ5ODdjNmUxZDYzMDBhMWNjNGI5ZWVlN2IxNmYxZjBhY2FjMTRmZmNkMWE5Njk5ZiJ9fX0=",
            "<<<",
            List.of("Page: {0}/{1}"));
    public ButtonConfiguration nextButton = new ButtonConfiguration('n',
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg3YmFhNDc2NzIzNGMwMWMwNGI4YmJlYjUxOGEwNTNkY2U3MzlmNGEwNDM1OGE0MjQzMDJmYjRhMDE3MmY4In19fQ==",
            ">>>",
            List.of("Page: {0}/{1}"));
}
