package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {
    public enum ItemType {
        WALLET,
        APPLE,
        ENGLISH,
        BOOK,
    }

    public ItemType type;
}
