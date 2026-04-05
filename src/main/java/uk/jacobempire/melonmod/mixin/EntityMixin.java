package uk.jacobempire.melonmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import uk.jacobempire.melonmod.common.block.MelonPortalBlock;
import uk.jacobempire.melonmod.common.entity.EntityMixinAccessor;

@Mixin(Entity.class)
public abstract class EntityMixin extends CapabilityProvider<Entity> implements EntityMixinAccessor {
    public boolean isInsideMelonPortal = false;

    @Inject(method = "handleNetherPortal", at = @At("HEAD"), cancellable = true)
    public void handleNetherPortal(CallbackInfo ci) {
        if (MelonPortalBlock.handleMelonPortal((Entity) (Object) this)) {
            processPortalCooldown();
            ci.cancel();
        }
    }

    @Shadow
    protected int portalCooldown;

    @Shadow
    protected int portalTime;

    public int getPortalTime() {
        return portalTime;
    }

    public void setPortalTime(int portalTime) {
        this.portalTime = portalTime;
    }

    @Shadow
    protected BlockPos portalEntrancePos;

    public BlockPos getPortalEntrancePos() {
        return portalEntrancePos;
    }

    public void setPortalEntrancePos(BlockPos portalEntrancePos) {
        this.portalEntrancePos = portalEntrancePos;
    }

    @Override
    public boolean isInsideMelonPortal() {
        return this.isInsideMelonPortal;
    }

    @Override
    public void setInsideMelonPortal(boolean isInsideMelonPortal) {
        this.isInsideMelonPortal = isInsideMelonPortal;
    }

    @Shadow
    protected abstract void processPortalCooldown();

    protected EntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }
}
