package medisant.me.furnacelogic.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceBlockEntity {

    @ModifyVariable(method = "createFuelTimeMap", at = @At("TAIL"))
    private static Map<ItemConvertible, Integer> createFuelTimeMap(Map<ItemConvertible, Integer> value) {
        value.put(Items.GUNPOWDER, 2400);
        return value;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private static void tick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        ItemStack fuel = blockEntity.getStack(1);
        if (fuel.getItem() == Items.GUNPOWDER && !blockEntity.getStack(0).isEmpty()) {
            fuel.decrement(1);
            int power;
            if (blockEntity instanceof BlastFurnaceBlockEntity) power = 8;
            else power = 4;
            world.createExplosion(null, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ(), power, true, Explosion.DestructionType.DESTROY);
        }
    }

}
