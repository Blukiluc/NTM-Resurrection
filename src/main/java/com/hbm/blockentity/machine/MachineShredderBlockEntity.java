package com.hbm.blockentity.machine;

import api.hbm.energymk2.IBatteryItem;
import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.inventory.menus.MachineShredderMenu;
import com.hbm.inventory.recipes.ShredderRecipes;
import com.hbm.items.machine.ItemBlades;
import com.hbm.lib.Library;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Shredder: shreds items in the input slots into their shredded equivalent (per ShredderRecipes),
 * distributing output across the 18 output slots. Speed/throughput is gated by blade wear:
 * both blades need to be present and between "fresh" and "almost broken" for processing to occur.
 * Blades lose 1 durability every completed processing cycle.
 *
 * Slot layout (matches the original 1.7.10 machine):
 *  0-8   : inputs (9)
 *  9-26  : outputs (18)
 *  27-28 : blades (left/right)
 *  29    : battery
 */
public class MachineShredderBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2 {

    public static final int SLOT_INPUT_START = 0;
    public static final int SLOT_INPUT_END = 9; // exclusive
    public static final int SLOT_OUTPUT_START = 9;
    public static final int SLOT_OUTPUT_END = 27; // exclusive
    public static final int SLOT_BLADE_LEFT = 27;
    public static final int SLOT_BLADE_RIGHT = 28;
    public static final int SLOT_BATTERY = 29;

    public long power;
    public static final long maxPower = 10_000;
    public static final int processingSpeed = 60;

    public int progress;
    private int soundCycle = 0;
    private boolean wasProcessing = false;

    public MachineShredderBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SHREDDER.get(), pos, state, 30);
    }

    @Override
    public Component getDefaultName() {
        return Component.translatable("container.machine_shredder");
    }

    @Override
    public void updateEntity() {
        if (this.level == null) return;

        boolean dirty = false;

        if (!level.isClientSide) {

            for (Direction dir : Direction.values()) {
                this.trySubscribe(level, new DirPos(this.worldPosition.relative(dir), dir));
            }

            boolean processingNow = hasPower() && canProcess();

            if (this.progress == 0) this.soundCycle = 0;

            if (processingNow) {
                progress++;
                power -= 5;

                if (this.progress >= processingSpeed) {

                    if (this.slots.get(SLOT_BLADE_LEFT).getMaxDamage() > 0) {
                        this.slots.get(SLOT_BLADE_LEFT).setDamageValue(this.slots.get(SLOT_BLADE_LEFT).getDamageValue() + 1);
                    }
                    if (this.slots.get(SLOT_BLADE_RIGHT).getMaxDamage() > 0) {
                        this.slots.get(SLOT_BLADE_RIGHT).setDamageValue(this.slots.get(SLOT_BLADE_RIGHT).getDamageValue() + 1);
                    }

                    this.progress = 0;
                    this.processItem();
                    dirty = true;
                }

                if (soundCycle == 0) {
                    level.playSound(null, this.worldPosition, SoundEvents.MINECART_RIDING, SoundSource.BLOCKS, this.getVolume(1.0F), 0.75F);
                }
                soundCycle++;
                if (soundCycle >= 50) soundCycle = 0;

            } else {
                progress = 0;
            }

            // The minecart sound clip runs longer than our tick loop, so just letting it
            // play out leaves an audible tail after the shredder stops - cut it short instead.
            if (this.wasProcessing && !processingNow) {
                this.stopGrindingSound();
            }
            this.wasProcessing = processingNow;

            this.power = Library.chargeTEFromItems(slots, SLOT_BATTERY, power, maxPower);

            this.networkPackNT(50);
        }

        if (dirty) this.setChanged();
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(this.power);
        buf.writeInt(this.progress);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.power = buf.readLong();
        this.progress = buf.readInt();
    }

    public void processItem() {

        for (int inSlot = SLOT_INPUT_START; inSlot < SLOT_INPUT_END; inSlot++) {

            ItemStack input = this.slots.get(inSlot);
            if (input.isEmpty() || !hasSpace(input)) continue;

            ItemStack outp = ShredderRecipes.getShredderResult(input);
            boolean placed = false;

            for (int outSlot = SLOT_OUTPUT_START; outSlot < SLOT_OUTPUT_END; outSlot++) {
                ItemStack existing = this.slots.get(outSlot);
                if (!existing.isEmpty() && ItemStack.isSameItemSameComponents(existing, outp) && existing.getCount() + outp.getCount() <= outp.getMaxStackSize()) {
                    existing.grow(outp.getCount());
                    input.shrink(1);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                for (int outSlot = SLOT_OUTPUT_START; outSlot < SLOT_OUTPUT_END; outSlot++) {
                    if (this.slots.get(outSlot).isEmpty()) {
                        this.slots.set(outSlot, outp.copy());
                        input.shrink(1);
                        break;
                    }
                }
            }
        }
    }

    public boolean canProcess() {

        ItemStack left = this.slots.get(SLOT_BLADE_LEFT);
        ItemStack right = this.slots.get(SLOT_BLADE_RIGHT);

        if (!left.isEmpty() && !right.isEmpty()) {

            int gearLeft = getGear(left);
            int gearRight = getGear(right);

            if (gearLeft > 0 && gearLeft < 3 && gearRight > 0 && gearRight < 3) {
                for (int i = SLOT_INPUT_START; i < SLOT_INPUT_END; i++) {
                    ItemStack stack = this.slots.get(i);
                    if (!stack.isEmpty() && hasSpace(stack)) return true;
                }
            }
        }

        return false;
    }

    public boolean hasSpace(ItemStack stack) {

        ItemStack result = ShredderRecipes.getShredderResult(stack);
        if (result.isEmpty()) return false;

        for (int i = SLOT_OUTPUT_START; i < SLOT_OUTPUT_END; i++) {
            ItemStack existing = this.slots.get(i);
            if (existing.isEmpty()) return true;
            if (ItemStack.isSameItemSameComponents(existing, result) && existing.getCount() + result.getCount() <= result.getMaxStackSize()) return true;
        }

        return false;
    }

    public boolean hasPower() {
        return power > 0;
    }

    public boolean isProcessing() {
        return this.progress > 0;
    }

    /**
     * Cuts off the in-progress minecart sound clip for everyone nearby. Needed because
     * level.playSound() fires a clip that plays to completion regardless of game state,
     * so without this the sound keeps going for a moment after the shredder actually stops.
     */
    private void stopGrindingSound() {
        if (!(this.level instanceof ServerLevel serverLevel)) return;

        ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(SoundEvents.MINECART_RIDING.getLocation(), SoundSource.BLOCKS);

        for (ServerPlayer player : serverLevel.getServer().getPlayerList().getPlayers()) {
            if (player.level() == serverLevel && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64 * 64) {
                player.connection.send(packet);
            }
        }
    }

    public int getProgressScaled(int scale) {
        return (this.progress * scale) / processingSpeed;
    }

    public long getPowerScaled(long scale) {
        return (this.power * scale) / maxPower;
    }

    /**
     * Returns the gear state of a blade stack:
     * 0 = not a blade, 1 = fresh (below half durability used), 2 = worn, 3 = almost broken (cannot process)
     */
    public static int getGear(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemBlades)) return 0;

        if (stack.getMaxDamage() == 0) return 1;

        int damage = stack.getDamageValue();
        int maxDamage = stack.getMaxDamage();

        if (damage < maxDamage / 2) return 1;
        if (damage != maxDamage) return 2;
        return 3;
    }

    public int getGearLeft() { return getGear(this.slots.get(SLOT_BLADE_LEFT)); }
    public int getGearRight() { return getGear(this.slots.get(SLOT_BLADE_RIGHT)); }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot >= SLOT_INPUT_START && slot < SLOT_INPUT_END) {
            return !ShredderRecipes.getShredderResult(stack).isEmpty() && !(stack.getItem() instanceof ItemBlades);
        }
        if (slot == SLOT_BLADE_LEFT || slot == SLOT_BLADE_RIGHT) {
            return stack.getItem() instanceof ItemBlades;
        }
        if (slot == SLOT_BATTERY) {
            return stack.getItem() instanceof IBatteryItem;
        }
        return false; // output slots: machine-only, not player-insertable
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        // all 30 slots are exposed; canPlaceItemThroughFace/canTakeItemThroughFace gate actual behavior
        int[] io = new int[30];
        for (int i = 0; i < io.length; i++) io[i] = i;
        return io;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        if (index >= SLOT_OUTPUT_START && index < SLOT_OUTPUT_END) return false; // outputs: extraction only
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (index >= SLOT_OUTPUT_START && index < SLOT_OUTPUT_END) return true;
        if (index == SLOT_BLADE_LEFT || index == SLOT_BLADE_RIGHT) {
            return stack.getDamageValue() == stack.getMaxDamage() && stack.getMaxDamage() > 0; // only extract spent blades
        }
        return false;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineShredderMenu(id, inventory, this);
    }
}
