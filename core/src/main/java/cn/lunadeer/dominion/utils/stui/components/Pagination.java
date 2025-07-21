package cn.lunadeer.dominion.utils.stui.components;

import cn.lunadeer.dominion.utils.stui.components.buttons.Button;
import cn.lunadeer.dominion.utils.stui.components.buttons.FunctionalButton;
import cn.lunadeer.dominion.utils.stui.components.buttons.ListViewButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

import static cn.lunadeer.dominion.utils.stui.ViewStyles.PRIMARY;
import static cn.lunadeer.dominion.utils.stui.ViewStyles.SECONDARY;


public class Pagination {
    public static TextComponent create(int page, int item_size, int page_size, ListViewButton command) {
        int page_count = (int) Math.ceil((double) item_size / page_size);
        if (page_count == 0) {
            page_count = 1;
        }
        List<Component> componentList = new ArrayList<>();
        componentList.add(Component.text("[", PRIMARY));
        componentList.add(Component.text(page, SECONDARY));
        componentList.add(Component.text("/", PRIMARY));
        componentList.add(Component.text(page_count, SECONDARY));
        componentList.add(Component.text("]", PRIMARY));
        if (page > 1) {
            componentList.add(new FunctionalButton("◀") {
                @Override
                public void function() {
                    command.function(String.valueOf(page - 1));
                }
            }.build());
        } else {
            componentList.add(new Button("◀").setColor(SECONDARY).build());
        }
        if (page < page_count) {
            componentList.add(new FunctionalButton("▶") {
                @Override
                public void function() {
                    command.function(String.valueOf(page + 1));
                }
            }.build());
        } else {
            componentList.add(new Button("▶").setColor(SECONDARY).build());
        }
        TextComponent.Builder builder = Component.text();
        for (Component component : componentList) {
            builder.append(component);
        }
        return builder.build();
    }
}
