package org.dimdev.dimdoors.block.door.condition;

import com.google.gson.JsonObject;
import org.dimdev.dimdoors.block.entity.EntranceRiftBlockEntity;

public enum AlwaysTrueCondition implements Condition {
	INSTANCE;

	@Override
	public boolean matches(EntranceRiftBlockEntity rift) {
		return true;
	}

	@Override
	public void toJsonInner(JsonObject json) {
	}

	@Override
	public Condition.ConditionType<?> getType() {
		return null;
	}
}