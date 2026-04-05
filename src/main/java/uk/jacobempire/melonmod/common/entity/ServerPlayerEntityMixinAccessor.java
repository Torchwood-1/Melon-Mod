package uk.jacobempire.melonmod.common.entity;

import net.minecraft.util.math.vector.Vector3d;

public interface ServerPlayerEntityMixinAccessor {
    public Vector3d enteredMelonDimPosition = null;

    public Vector3d getEnteredMelonDimPosition();

    public void setEnteredMelonDimPosition(Vector3d enteredMelonDimPosition);
}
