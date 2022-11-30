package Component.Type;

import lombok.Getter;

public enum SoundType {
    INGAME("ingame.wav"),
    WAITROOM("waitroom.wav"),
    ORDER("order.wav"),
    ACTION("action.wav"),
    FOODBOX("foodbox.wav"),
    FRYER("fryer.wav"),
    FRYPAN("frypan.wav"),
    KNIFE("knife.wav"),
    POT("pot.wav"),
    TRASH("trash.wav"),
    ORDER_SUCCESS("order_success.wav"),
    ORDER_FAILED("order_failed.wav"),
    ;

    @Getter
    private final String path;

    SoundType(String path) {
        this.path = "/sound/" + path;
    }
}
