package com.hbm.blockentity.machine;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.menus.MachinePressMenu;
import com.hbm.inventory.recipes.PressRecipes;
import com.hbm.items.machine.ItemStamp;
import com.hbm.registry.NtmSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MachinePressBlockEntity extends MachineBaseBlockEntity {

    public int speed = 0; // speed ticks up once (or four times if preheated) when operating
    public static final int maxSpeed = 400; // max speed ticks for acceleration
    public static final int progressAtMax = 25; // max press advance per tick when at full speed
    public int burnTime = 0; // burn ticks of the loaded fuel, 200 ticks equal one operation

    public int press; // extension of the press, operation completes when maxPress is reached
    public static final int maxPress = 200; // press distance per operation at speed = maxSpeed

    // client-side smoothing for the press animation
    public double renderPress;
    public double lastPress;
    private int syncPress;
    private int turnProgress;

    private boolean isRetracting = false; // direction the press is currently going
    private int delay; // small pause between direction changes, purely cosmetic

    public MachinePressBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.PRESS.get(), pos, state, 13);
    }

    @Override
    public Component getDefaultName() { return Component.translatable("container.machine_press"); }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(level.isClientSide) {
            this.lastPress = this.renderPress;
            if(this.turnProgress > 0) {
                this.renderPress += (this.syncPress - this.renderPress) / (double) this.turnProgress;
                this.turnProgress--;
            } else {
                this.renderPress = this.syncPress;
            }
            return;
        }

        boolean preheated = false;

        for(Direction dir : Direction.values()) {
            BlockPos neighbour = this.worldPosition.relative(dir);
            if(level.getBlockState(neighbour).is(NtmBlocks.PRESS_PREHEATER)) {
                preheated = true;
                System.out.println(preheated);
                break;
            }
        }

        boolean canProcess = this.canProcess();

        if(canProcess && this.burnTime >= 200) {
            this.speed += preheated ? 4 : 1;
            if(this.speed > maxSpeed) this.speed = maxSpeed;
        } else {
            this.speed -= 1;
            if(this.speed < 0) this.speed = 0;
        }

        if(this.delay <= 0) {
            if(this.isRetracting) {
                int stampSpeed = Math.max(1, this.speed * progressAtMax / maxSpeed);
                this.press -= stampSpeed;
                if(this.press <= 0) {
                    this.press = 0;
                    this.isRetracting = false;
                    this.delay = 5;
                }
            } else if(canProcess) {
                int stampSpeed = this.speed * progressAtMax / maxSpeed;
                this.press += stampSpeed;
                if(this.press >= maxPress) {
                    this.press = maxPress;

                    level.playSound(null, this.worldPosition, NtmSoundEvents.PRESS_OPERATE.get(),
                            SoundSource.BLOCKS, this.getVolume(1.5F), 1.0F);

                    this.doOutput();

                    this.isRetracting = true;
                    this.delay = 5;

                    if(this.burnTime >= 200) this.burnTime -= 200;
                    this.setChanged();
                }
            } else if(this.press > 0) {
                this.isRetracting = true;
            }
        } else {
            this.delay--;
        }

        ItemStack fuelStack = this.slots.get(1);
        if(!fuelStack.isEmpty() && this.burnTime < 200) {
            int burn = AbstractFurnaceBlockEntity.getFuel().getOrDefault(fuelStack.getItem(), 0);
            if(burn > 0) {
                this.burnTime += burn;
                fuelStack.shrink(1);
                this.setChanged();
            }
        }

        this.networkPackNT(50);
    }

    public boolean canProcess() {
        if(burnTime < 200) return false;

        ItemStack ingredient = this.slots.get(0);
        ItemStack stamp = this.slots.get(2);
        if(ingredient.isEmpty() || stamp.isEmpty()) return false;

        ItemStack output = PressRecipes.getOutput(ingredient, stamp);
        if(output.isEmpty()) return false;

        ItemStack outSlot = this.slots.get(3);
        if(outSlot.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(outSlot, output)
                && outSlot.getCount() + output.getCount() <= outSlot.getMaxStackSize();
    }

    private void doOutput() {
        ItemStack ingredient = this.slots.get(0);
        ItemStack stamp = this.slots.get(2);
        ItemStack output = PressRecipes.getOutput(ingredient, stamp);
        if(output.isEmpty()) return;

        ItemStack outSlot = this.slots.get(3);
        if(outSlot.isEmpty()) {
            this.slots.set(3, output.copy());
        } else {
            outSlot.grow(output.getCount());
        }

        ingredient.shrink(1);

        if(stamp.getMaxDamage() > 0) {
            stamp.setDamageValue(stamp.getDamageValue() + 1);
            if(stamp.getDamageValue() >= stamp.getMaxDamage()) {
                this.slots.set(2, ItemStack.EMPTY);
            }
        }

        this.setChanged();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.speed);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.press);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.speed = buf.readInt();
        this.burnTime = buf.readInt();
        this.syncPress = buf.readInt();
        this.turnProgress = 2;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == 0) return true; // input
        if(slot == 1) return AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0) > 0; // fuel
        if(slot == 2 && stack.getItem() instanceof ItemStamp) return true; // plate
        if(slot >= 4 && slot <= 12) return true; // storage
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachinePressMenu(id, inventory, this);
    }
}