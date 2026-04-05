package uk.jacobempire.melonmod.common.entity;

import net.minecraft.util.math.BlockPos;

public interface EntityMixinAccessor {
    public boolean isInsideMelonPortal = false;
    public boolean isInsideMelonPortal();
    public void setInsideMelonPortal(boolean isInsideMelonPortal);

    public int getPortalTime();
    public void setPortalTime(int portalTime);

    public BlockPos getPortalEntrancePos();
    public void setPortalEntrancePos(BlockPos portalEntrancePos);
}
