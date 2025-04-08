package tk.jacobempire.melonmod.client.render.entity;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import tk.jacobempire.melonmod.common.entity.MelonGoblinEntity;

public class MelonGoblinRenderer extends BipedRenderer<MelonGoblinEntity, BipedModel<MelonGoblinEntity>> {

	public MelonGoblinRenderer(EntityRendererManager erm, BipedModel<MelonGoblinEntity> model, float shadow) {
		super(erm, model, shadow);
	}

	public MelonGoblinRenderer(EntityRendererManager erm) {
		super(erm, new BipedModel<>(0), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(MelonGoblinEntity p_110775_1_) {
	    return new ResourceLocation("textures/block/melon_side.png");
	}

}
