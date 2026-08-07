package com.hbm.blockentity.machine;

import api.hbm.energymk2.IEnergyReceiverMK2;
import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.interfaces.IControlReceiver;
import com.hbm.inventory.UpgradeManagerNT;
import com.hbm.inventory.menus.MachineCentrifugeMenu;
import com.hbm.inventory.recipes.CentrifugeRecipes;
import com.hbm.inventory.recipes.loader.GenericRecipe;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.lib.Library;
import com.hbm.main.NuclearTechMod;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;

public class MachineCentrifugeBlockEntity extends MachineBaseBlockEntity implements IEnergyReceiverMK2, IUpgradeInfoProvider, IControlReceiver {

    public static final int SLOT_BATTERY = 0;
    public static final int SLOT_UPGRADE_1 = 1;
    public static final int SLOT_UPGRADE_2 = 2;
    public static final int SLOT_INPUT = 3;
    public static final int SLOT_OUTPUT_START = 4;
    public static final int SLOT_OUTPUT_END = 8;

    public long power;
    public static final long maxPower = 100_000;
    public int progress;
    public boolean isProgressing;
    private int audioDuration = 0;

    private AudioWrapper audio;
    public UpgradeManagerNT upgradeManager = new UpgradeManagerNT(this);

    public static int processingSpeed = 200;
    public static int baseConsumption = 200;

    private static final int[] slot_io = new int[] { SLOT_INPUT, 4, 5, 6, 7 };

    public MachineCentrifugeBlockEntity(BlockPos pos, BlockState blockState) {
        super(NtmBlockEntityTypes.CENTRIFUGE.get(), pos, blockState, 8);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.centrifuge");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(!level.isClientSide) {
            this.power = Library.chargeTEFromItems(slots, SLOT_BATTERY, power, maxPower);

            for(Direction dir : Direction.values()) {
                this.trySubscribe(level, new DirPos(
                        worldPosition.getX() + dir.getStepX(),
                        worldPosition.getY() + dir.getStepY(),
                        worldPosition.getZ() + dir.getStepZ(),
                        dir
                ));
            }

            upgradeManager.checkSlots(slots, SLOT_UPGRADE_1, SLOT_UPGRADE_2);

            int consumption = baseConsumption;
            int speed = 1;

            speed += upgradeManager.getLevel(UpgradeType.SPEED);
            consumption += upgradeManager.getLevel(UpgradeType.SPEED) * baseConsumption;

            speed *= (1 + upgradeManager.getLevel(UpgradeType.OVERDRIVE) * 5);
            consumption += upgradeManager.getLevel(UpgradeType.OVERDRIVE) * baseConsumption * 50;

            consumption /= (1 + upgradeManager.getLevel(UpgradeType.POWER));

            if(hasPower() && isProcessing()) {
                this.power -= consumption;
                if(this.power < 0) this.power = 0;
            }

            if(hasPower() && canProcess()) {
                isProgressing = true;
            } else {
                isProgressing = false;
            }

            if(isProgressing) {
                progress += speed;

                if(this.progress >= processingSpeed) {
                    this.progress = 0;
                    this.processItem();
                }
            } else {
                progress = 0;
            }

            this.networkPackNT(50);
        } else {
            if(isProgressing) {
                audioDuration += 2;
            } else {
                audioDuration -= 3;
            }

            audioDuration = Mth.clamp(audioDuration, 0, 60);

            if(audioDuration > 10 && Math.sqrt(NuclearTechMod.proxy.me().distanceToSqr(this.getBlockPos().getCenter())) < 25) {
                if(audio == null) {
                    audio = createAudioLoop();
                    audio.startSound();
                } else if(!audio.isPlaying()) {
                    audio = rebootAudio(audio);
                }

                audio.updateVolume(getVolume(1F));
                audio.updatePitch((audioDuration - 10) / 100F + 0.5F);
                audio.keepAlive();
            } else {
                if(audio != null) {
                    audio.stopSound();
                    audio = null;
                }
            }
        }
    }

    public boolean hasPower() {
        return power > 0;
    }

    public boolean isProcessing() {
        return this.progress > 0;
    }

    public ItemStack[] getOutput() {
        ItemStack input = slots.get(SLOT_INPUT);
        if(input.isEmpty()) return null;

        GenericRecipe recipe = findRecipe();
        if(recipe == null || recipe.outputItem == null) return null;

        ItemStack[] out = new ItemStack[4];
        for(int i = 0; i < Math.min(4, recipe.outputItem.length); i++) {
            out[i] = recipe.outputItem[i].collapse();
        }
        return out;
    }

    public GenericRecipe findRecipe() {
        ItemStack input = slots.get(SLOT_INPUT);
        if(input.isEmpty()) return null;

        for(GenericRecipe recipe : CentrifugeRecipes.INSTANCE.recipeOrderedList) {
            if(recipe.inputItem != null && recipe.inputItem.length > 0) {
                if(recipe.inputItem[0].matchesRecipe(input, true)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    public boolean canProcess() {
        ItemStack input = slots.get(SLOT_INPUT);
        if(input.isEmpty()) return false;

        GenericRecipe recipe = findRecipe();
        if(recipe == null) return false;

        ItemStack[] out = getOutput();
        if(out == null) return false;

        for(int i = 0; i < Math.min(4, out.length); i++) {
            int slot = SLOT_OUTPUT_START + i;

            if(out[i] == null || out[i].isEmpty()) continue;

            ItemStack existing = slots.get(slot);

            // Slot vide → OK
            if(existing.isEmpty()) continue;

            // Même item et place pour stacker → OK
            if(ItemStack.isSameItemSameComponents(existing, out[i])
                    && existing.getCount() + out[i].getCount() <= out[i].getMaxStackSize()) continue;

            // Sinon → pas de place
            return false;
        }

        return true;
    }

    private void processItem() {
        ItemStack[] out = getOutput();
        if(out == null) return;

        for(int i = 0; i < Math.min(4, out.length); i++) {
            if(out[i] == null || out[i].isEmpty()) continue;

            int slot = SLOT_OUTPUT_START + i;
            ItemStack existing = slots.get(slot);

            if(existing.isEmpty()) {
                slots.set(slot, out[i].copy());
            } else {
                existing.grow(out[i].getCount());
            }
        }

        slots.get(SLOT_INPUT).shrink(1);
        if(slots.get(SLOT_INPUT).isEmpty()) {
            slots.set(SLOT_INPUT, ItemStack.EMPTY);
        }
        this.setChanged();
    }

    @Override
    public AudioWrapper createAudioLoop() {
        return AudioWrapper.getLoopedSound(NtmSoundEvents.CENTRIFUGE_LOOP.get(), SoundSource.BLOCKS, this, 1.0F, 10F, 1.0F, 20);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(audio != null) { audio.stopSound(); audio = null; }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return slot_io;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == SLOT_INPUT && this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index >= SLOT_OUTPUT_START && index < SLOT_OUTPUT_END;
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeLong(power);
        buf.writeInt(progress);
        buf.writeBoolean(isProgressing);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        power = buf.readLong();
        progress = buf.readInt();
        isProgressing = buf.readBoolean();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.power = tag.getLong("power");
        this.progress = tag.getInt("progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong("power", power);
        tag.putInt("progress", progress);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_BATTERY) return true;
        if(slot >= SLOT_UPGRADE_1 && slot <= SLOT_UPGRADE_2 && stack.getItem() instanceof MachineUpgradeItem) return true;
        if(slot == SLOT_INPUT) return true;
        return false;
    }

    @Override public long getPower() { return power; }
    @Override public void setPower(long power) { this.power = power; }
    @Override public long getMaxPower() { return maxPower; }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineCentrifugeMenu(id, inventory, this);
    }

    @Override
    public boolean hasPermission(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void receiveControl(CompoundTag tag) {
    }

    @Override
    public boolean canProvideInfo(UpgradeType type, int level, boolean extendedInfo) {
        return type == UpgradeType.SPEED || type == UpgradeType.POWER || type == UpgradeType.OVERDRIVE;
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<String> info, boolean extendedInfo) {
        if(type == UpgradeType.SPEED) {
            info.add(net.minecraft.ChatFormatting.GREEN + "Speed: +" + (100 - 100 / (level + 1)) + "%");
            info.add(net.minecraft.ChatFormatting.RED + "Consumption: +" + (level * 100) + "%");
        }
        if(type == UpgradeType.POWER) {
            info.add(net.minecraft.ChatFormatting.GREEN + "Consumption: -" + (100 - 100 / (level + 1)) + "%");
        }
        if(type == UpgradeType.OVERDRIVE) {
            info.add((BobMathUtil.getBlink() ? net.minecraft.ChatFormatting.RED : net.minecraft.ChatFormatting.DARK_GRAY) + "YES");
        }
    }

    @Override
    public HashMap<UpgradeType, Integer> getValidUpgrades() {
        HashMap<UpgradeType, Integer> upgrades = new HashMap<>();
        upgrades.put(UpgradeType.SPEED, 3);
        upgrades.put(UpgradeType.POWER, 3);
        upgrades.put(UpgradeType.OVERDRIVE, 3);
        return upgrades;
    }

    public int getCentrifugeProgressScaled(int i) {
        return (progress * i) / processingSpeed;
    }

    public long getPowerRemainingScaled(int i) {
        return (power * i) / maxPower;
    }
}