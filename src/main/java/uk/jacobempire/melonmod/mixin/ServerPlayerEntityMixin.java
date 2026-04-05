package uk.jacobempire.melonmod.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import uk.jacobempire.melonmod.common.entity.ServerPlayerEntityMixinAccessor;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityMixinAccessor {
    public Vector3d enteredMelonDimPosition;

    public Vector3d getEnteredMelonDimPosition() {
        return enteredMelonDimPosition;
    }

    public void setEnteredMelonDimPosition(Vector3d enteredMelonDimPosition) {
        this.enteredMelonDimPosition = enteredMelonDimPosition;
    }

}
