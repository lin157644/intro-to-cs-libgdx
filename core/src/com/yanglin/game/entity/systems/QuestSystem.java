package com.yanglin.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class QuestSystem extends EntitySystem implements PlayerInteractionSystem.PlayerInteractionListener {
    public enum QUESTS {
        EAT,
        ENGLISH,
        GIFT2PROF,
        RETURN_BOOK,
        SERVICE_LEARNING,
        GRADUATE_PROC,
        DIPLOMA;
    }
    public QuestSystem() {

    }

    @Override
    public void triggerEvent(EventType eventType) {
        switch (eventType){}
    }
}
