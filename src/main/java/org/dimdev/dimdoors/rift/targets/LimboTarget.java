package org.dimdev.dimdoors.rift.targets;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.EulerAngle;
import net.minecraft.util.math.Vec3d;

import org.dimdev.dimdoors.api.rift.target.EntityTarget;
import org.dimdev.dimdoors.api.util.TeleportUtil;
import org.dimdev.dimdoors.world.ModDimensions;

import net.minecraft.entity.Entity;
import org.dimdev.dimdoors.world.pocket.VirtualLocation;

public class LimboTarget extends VirtualTarget implements EntityTarget {
	public static final LimboTarget INSTANCE = new LimboTarget();
	public static final Codec<LimboTarget> CODEC = Codec.unit(INSTANCE);

	private LimboTarget() {
	}

	@Override
	public boolean receiveEntity(Entity entity, Vec3d relativePos, EulerAngle relativeAngle, Vec3d relativeVelocity) {
		TeleportUtil.teleport(entity, ModDimensions.LIMBO_DIMENSION, entity.getBlockPos().add(0, 255-entity.getBlockY(), 0), relativeAngle, relativeVelocity);
		return true;
	}

	@Override
	public VirtualTargetType<? extends VirtualTarget> getType() {
		return VirtualTargetType.LIMBO;
	}
}
