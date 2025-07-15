package cn.lunadeer.dominion.utils.scui.configuration;

import cn.lunadeer.dominion.utils.configuration.ConfigurationPart;

import java.util.List;

public class ListViewConfiguration extends ConfigurationPart {
    public ListViewConfiguration(
            char itemSymbol,
            List<String> layout,
            ButtonConfiguration previewButton,
            ButtonConfiguration nextButton
    ) {
        this.itemSymbol = String.valueOf(itemSymbol);
        this.layout = layout;
        this.previewButton = previewButton;
        this.nextButton = nextButton;
    }

    public ListViewConfiguration(
            char itemSymbol,
            List<String> layout
    ) {
        this.itemSymbol = String.valueOf(itemSymbol);
        this.layout = layout;
    }

    public String itemSymbol = "i";
    public List<String> layout = List.of(
            "#########",
            "#iiiiiii#",
            "#iiiiiii#",
            "#iiiiiii#",
            "#p#####n#"
    );
    public ButtonConfiguration previewButton = ButtonConfiguration.createHeadByB64('p',
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWRiZWIwZmJjNWU2NjAxZmU1MDQ3MDJjYWZmZGFhYzBlOGVhMTllMTFiY2FkZmJlZTBkMThjODlmNDZiYzFmZCJ9fX0=",
            "<<<",
            List.of("Page: {0}/{1}"));
    public ButtonConfiguration nextButton = ButtonConfiguration.createHeadByB64('n',
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI5Y2VjYzIxZDY3MGMxZmYyNzc4MTc2MjI1ZTI4NTBlMmVlMmY3Y2Y1NDEzYmIxNTY2N2Q5OGRiYjNjZjhiNSJ9fX0=",
            ">>>",
            List.of("Page: {0}/{1}"));
}
