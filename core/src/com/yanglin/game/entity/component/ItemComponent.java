package com.yanglin.game.entity.component;

import com.badlogic.ashley.core.Component;

public class ItemComponent implements Component {
    public enum ItemType {
        APPLE,
        WALLET,
        TOEIC,
        BOOK,
    }
    public ItemType type;
}
