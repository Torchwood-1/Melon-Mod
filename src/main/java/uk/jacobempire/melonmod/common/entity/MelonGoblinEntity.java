package uk.jacobempire.melonmod.common.entity;

import java.util.Collections;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class MelonGoblinEntity extends MonsterEntity {

	public static final ITag<Item> STEALABLE = ITag.fromSet(Collections.singleton(Items.MELON_SLICE));

	Inventory inventory = new Inventory(16);

	private static final DataParameter<Boolean> DATA_AVOID_PLAYERS = EntityDataManager
			.defineId(MelonGoblinEntity.class, DataSerializers.BOOLEAN);

	public MelonGoblinEntity(EntityType<? extends MonsterEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_AVOID_PLAYERS, false);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
		this.addBehaviourGoals();
	}

	protected void addBehaviourGoals() {
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, IronGolemEntity.class, 32, 2, 2));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, PlayerEntity.class, 32, 1, 2,
				entity -> this.isAvoidingPlayers()));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
		this.targetSelector.addGoal(2,
				new NearestAttackableTargetWithMelonsGoal<>(this, PlayerEntity.class, true));
	}

	public boolean doHurtTarget(Entity target) {
		if (!super.doHurtTarget(target))
			return false;

		if (target instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) target;
			player.inventory.items.forEach(this::stealItem);

			this.setAvoidPlayers(true);
		}

		return true;
	}

	public static boolean canSteal(ItemStack stack) {
		return stack.getItem().is(STEALABLE);
	}

	public boolean stealItem(ItemStack stack) {
		if (!canSteal(stack))
			return false;

		if (this.inventory.canAddItem(stack)) {
			this.inventory.addItem(stack);
		} else {
			this.spawnAtLocation(stack.copy());
		}

		stack.setCount(0);
		return true;
	}

	@Override
	protected void pickUpItem(ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		if (canSteal(stack) && inventory.canAddItem(stack)) {
			inventory.addItem(stack);
			itemEntity.remove();

			World world = itemEntity.level;
			world.playSound(null, this.getX(), this.getY() + 0.5, this.getZ(),
					SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
					((world.random.nextFloat() - world.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);

			return;
		}
		super.pickUpItem(itemEntity);
	}

	@Override
	public boolean canPickUpLoot() {
		return true;
	}

	protected void dropAllDeathLoot(DamageSource source) {
		super.dropAllDeathLoot(source);

		for (int i = 0; i < this.inventory.getMaxStackSize(); i++) {
			this.spawnAtLocation(this.inventory.getItem(i));
		}
		this.inventory.removeAllItems();

		this.getHandSlots().forEach(this::spawnAtLocation);
	};

	public Inventory getInventory() {
		return inventory;
	}

	public void setAvoidPlayers(boolean avoid) {
		this.entityData.set(DATA_AVOID_PLAYERS, avoid);
	}

	public boolean isAvoidingPlayers() {
		return this.entityData.get(DATA_AVOID_PLAYERS);
	}

	protected static class NearestAttackableTargetWithMelonsGoal<T extends PlayerEntity>
			extends NearestAttackableTargetGoal<T> {

		public NearestAttackableTargetWithMelonsGoal(MelonGoblinEntity mob, Class<T> targetType,
				boolean mustSee) {
			super(mob, targetType, mustSee);
			this.targetConditions = (new EntityPredicate()).range(this.getFollowDistance())
					.selector(entity -> ((PlayerEntity) entity).inventory.contains(STEALABLE));
		}

		@Override
		public boolean canContinueToUse() {
			PlayerEntity player = (PlayerEntity) this.mob.getTarget();

			return super.canContinueToUse() && player.inventory.contains(STEALABLE);
		}

		@Override
		public void setTarget(LivingEntity newTarget) {
			super.setTarget(newTarget);

			MelonGoblinEntity melonGoblinEntity = (MelonGoblinEntity) mob;
			if (melonGoblinEntity.isAvoidingPlayers())
				melonGoblinEntity.setAvoidPlayers(false);

		}

	}

	public static AttributeModifierMap.MutableAttribute createAttributes() {
		return MonsterEntity.createMonsterAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, (double) 0.23F)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ARMOR, 2.0D)
				.add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);

		nbt.put("Inventory", inventory.createTag());
		nbt.putBoolean("AvoidingPlayers", this.isAvoidingPlayers());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);

		inventory.fromTag(nbt.getList("Inventory", 10));
		this.setAvoidPlayers(nbt.getBoolean("AvoidingPlayers"));
	}

}
