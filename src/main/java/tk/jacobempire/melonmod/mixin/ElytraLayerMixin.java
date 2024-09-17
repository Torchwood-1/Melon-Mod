package tk.jacobempire.melonmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tk.jacobempire.melonmod.common.init.ModItems;
import static tk.jacobempire.melonmod.MelonMod.MODID;

@Mixin({ ElytraLayer.class })
public abstract class ElytraLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
	private static final ResourceLocation MELON_WINGS_LOCATION = new ResourceLocation(MODID,
			"textures/entity/melon_elytra.png");

	public ElytraLayerMixin(IEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true, remap = false)
	public void shouldRender(ItemStack stack, T entity, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() == ModItems.MELON_ELYTRA.get()) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}

	@Inject(method = "getElytraTexture", at = @At("HEAD"), cancellable = true, remap = false)
	public void getElytraTexture(ItemStack stack, T entity, CallbackInfoReturnable<ResourceLocation> cir) {
		if (stack.getItem() == ModItems.MELON_ELYTRA.get()) {
			cir.setReturnValue(MELON_WINGS_LOCATION);
			cir.cancel();
		}
	}

}
