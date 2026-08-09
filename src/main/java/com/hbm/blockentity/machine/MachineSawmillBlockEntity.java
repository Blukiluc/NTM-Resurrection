package com.hbm.blockentity.machine;

import api.hbm.tile.IHeatSource;
import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.items.NtmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class MachineSawmillBlockEntity extends MachineBaseBlockEntity {

    public int heat;
    public int progress;
    public int overspeed;
    public boolean hasBlade = true;
    public float bladeRotation;
    public float lastBladeRotation;

    public MachineSawmillBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.SAWMILL.get(), pos, state, 3);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.machine_sawmill");
    }

    @Override
    public void updateEntity() {
        if(this.level == null) return;

        if(this.level.isClientSide) {
            this.lastBladeRotation = this.bladeRotation;
            if(this.hasBlade) {
                this.bladeRotation += this.heat * 25F / 300F;
                if(this.bladeRotation >= 360F) {
                    this.bladeRotation -= 360F;
                    this.lastBladeRotation -= 360F;
                }
            }
            return;
        }

        this.drawHeat();
        if(this.hasBlade && this.heat >= 100 && this.canProcess()) {
            this.progress += Math.max(1, this.heat / 10);
            if(this.progress >= 600) {
                this.processInput();
                this.progress = 0;
            }
        }

        if(this.heat > 300) {
            this.overspeed++;
            if(this.overspeed > 300) {
                this.hasBlade = false;
                this.overspeed = 0;
                this.level.explode(null, this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 1D, this.worldPosition.getZ() + 0.5D, 2F, Level.ExplosionInteraction.BLOCK);
            }
        } else {
            this.overspeed = Math.max(0, this.overspeed - 1);
        }

        this.networkPackNT(50);
        this.heat = 0;
        this.setChanged();
    }

    private void drawHeat() {
        if(this.level == null) return;
        if(this.level.getBlockEntity(this.worldPosition.below()) instanceof IHeatSource source) {
            int accepted = Math.max(1, source.getHeatStored() / 10);
            source.useUpHeat(accepted);
            this.heat += accepted;
        }
    }

    private boolean canProcess() {
        ItemStack result = this.getResult(this.getItem(0));
        if(result.isEmpty()) return false;
        ItemStack output = this.getItem(1);
        return output.isEmpty() || ItemStack.isSameItemSameComponents(output, result) && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void processInput() {
        ItemStack input = this.getItem(0);
        ItemStack result = this.getResult(input);
        if(result.isEmpty()) return;

        ItemStack output = this.getItem(1);
        if(output.isEmpty()) {
            this.setItem(1, result);
        } else {
            output.grow(result.getCount());
        }

        if((input.is(ItemTags.LOGS) && this.level.random.nextFloat() < 0.5F) ||
                ((input.is(ItemTags.PLANKS) || input.is(ItemTags.SAPLINGS) || input.is(Items.STICK)) && this.level.random.nextFloat() < 0.1F)) {
            ItemStack sawdust = this.getItem(2);
            if(sawdust.isEmpty()) this.setItem(2, new ItemStack(NtmItems.POWDER_SAWDUST.get()));
            else if(sawdust.is(NtmItems.POWDER_SAWDUST.get()) && sawdust.getCount() < sawdust.getMaxStackSize()) sawdust.grow(1);
        }

        input.shrink(1);
        if(input.isEmpty()) this.setItem(0, ItemStack.EMPTY);
    }

    private ItemStack getResult(ItemStack input) {
        if(this.level == null || input.isEmpty()) return ItemStack.EMPTY;
        if(input.is(Items.STICK)) return new ItemStack(NtmItems.POWDER_SAWDUST.get());
        if(input.is(ItemTags.PLANKS)) return new ItemStack(Items.STICK, 6);
        if(input.is(ItemTags.SAPLINGS)) return new ItemStack(Items.STICK, 2);
        if(!input.is(ItemTags.LOGS)) return ItemStack.EMPTY;

        CraftingInput craftingInput = CraftingInput.of(1, 1, java.util.List.of(input.copyWithCount(1)));
        ItemStack crafted = this.level.getRecipeManager()
                .getRecipeFor(RecipeType.CRAFTING, craftingInput, this.level)
                .map(recipe -> recipe.value().assemble(craftingInput, this.level.registryAccess()))
                .orElse(ItemStack.EMPTY);
        if(!crafted.isEmpty()) crafted.setCount(Math.min(crafted.getMaxStackSize(), crafted.getCount() * 6));
        return crafted;
    }

    public void giveOutputs(Player player) {
        for(int slot = 1; slot <= 2; slot++) {
            ItemStack output = this.removeItemNoUpdate(slot);
            if(!output.isEmpty() && !player.getInventory().add(output)) player.drop(output, false);
        }
        this.setChanged();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot == 0 && !this.getResult(stack).isEmpty();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {0, 1, 2};
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot > 0;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.heat = tag.getInt("heat");
        this.progress = tag.getInt("progress");
        this.overspeed = tag.getInt("overspeed");
        this.hasBlade = tag.getBoolean("hasBlade");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("heat", this.heat);
        tag.putInt("progress", this.progress);
        tag.putInt("overspeed", this.overspeed);
        tag.putBoolean("hasBlade", this.hasBlade);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.heat);
        buf.writeInt(this.progress);
        buf.writeBoolean(this.hasBlade);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.heat = buf.readInt();
        this.progress = buf.readInt();
        this.hasBlade = buf.readBoolean();
    }
}
