package cn.leomc.pvzmultiplayer.common.game.content.entity.zombie;

public enum ZombieState {
    IDLE,
    WALKING,
    WALKING_HALF_HEALTH,
    WALKING_DYING,
    EATING,
    EATING_HALF_HEALTH,
    EATING_DYING,
    DEAD;


    public boolean isDying() {
        return this == WALKING_DYING || this == EATING_DYING;
    }

    public boolean isWalking() {
        return this == WALKING || this == WALKING_HALF_HEALTH || this == WALKING_DYING;
    }

    public boolean isEating() {
        return this == EATING || this == EATING_HALF_HEALTH || this == EATING_DYING;
    }

    public boolean isFullHealth() {
        return this == WALKING || this == EATING;
    }

    public boolean isHalfHealth() {
        return this == WALKING_HALF_HEALTH || this == EATING_HALF_HEALTH;
    }

    public boolean isDead() {
        return this == DEAD;
    }
}
