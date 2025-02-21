package net.frozenblock.zgmobs.mixin;

import net.frozenblock.zgmobs.Germonium;
import net.frozenblock.zgmobs.IGermonium;
import net.frozenblock.zgmobs.ZGMobs;
import net.minecraft.world.entity.monster.Monster;
import org.lwjgl.system.NonnullDefault;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
@NonnullDefault
public class MonsterMixin implements IGermonium {
    @Override
    public void setVariant(Germonium germonium) {
        ((Monster)(Object)this).getEntityData().set(ZGMobs.DATA_TYPE_ID, germonium.getId());
    }

    @Override
    public Germonium getVariant() {
        return Germonium.byId(((Monster)(Object)this).getEntityData().get(ZGMobs.DATA_TYPE_ID));
    }
}
