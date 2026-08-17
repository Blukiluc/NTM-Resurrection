package com.hbm.blockentity.machine;

import com.hbm.blockentity.MachineBaseBlockEntity;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.machine.MachineFurnaceBrickBlock;
import com.hbm.inventory.menus.MachineFurnaceBrickMenu;
import com.hbm.items.NtmItems;
import com.hbm.modules.ModuleBurnTime;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MachineFurnaceBrickBlockEntity extends MachineBaseBlockEntity {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int SLOT_ASH = 3;
    public static final int BASE_TIME = 200;

    public static final ModuleBurnTime burnModule = new ModuleBurnTime();

    public int burnTime;
    public int maxBurnTime;
    public int progress;
    public int ashLevelWood;
    public int ashLevelCoal;
    public int ashLevelMisc;

    public MachineFurnaceBrickBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.FURNACE_BRICK.get(), pos, state, 4);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.furnace_brick");
    }

    @Override
    public void updateEntity() {
        if(this.level == null || this.level.isClientSide) return;

        boolean wasBurning = this.burnTime > 0;
        boolean changed = false;

        if(this.burnTime > 0) {
            this.burnTime--;
            changed = true;
        }

        if(this.burnTime == 0 && this.canSmelt()) {
            ItemStack fuel = this.slots.get(SLOT_FUEL);
            int burn = burnModule.getBurnTime(fuel);

            if(burn > 0) {
                this.maxBurnTime = this.burnTime = burn;

                Item ash = MachineWoodBurnerBlockEntity.getAshFromFuel(fuel);
                if(ash == NtmItems.POWDER_ASH_WOOD.get()) this.ashLevelWood += burn;
                if(ash == NtmItems.POWDER_ASH_COAL.get()) this.ashLevelCoal += burn;
                if(ash == NtmItems.POWDER_ASH.get()) this.ashLevelMisc += burn;

                int threshold = 2_000;
                if(this.processAsh(this.ashLevelWood, NtmItems.POWDER_ASH_WOOD.get(), threshold)) this.ashLevelWood -= threshold;
                if(this.processAsh(this.ashLevelCoal, NtmItems.POWDER_ASH_COAL.get(), threshold)) this.ashLevelCoal -= threshold;
                if(this.processAsh(this.ashLevelMisc, NtmItems.POWDER_ASH.get(), threshold)) this.ashLevelMisc -= threshold;

                ItemStack remainder = fuel.hasCraftingRemainingItem() ? fuel.getCraftingRemainingItem().copy() : ItemStack.EMPTY;
                fuel.shrink(1);
                if(fuel.isEmpty()) this.slots.set(SLOT_FUEL, remainder);
                changed = true;
            }
        }

        if(this.burnTime > 0 && this.canSmelt()) {
            this.progress += this.getBurnSpeed();
            changed = true;

            if(this.progress >= BASE_TIME) {
                this.progress = 0;
                this.smeltItem();
            }
        } else if(this.progress != 0) {
            this.progress = 0;
            changed = true;
        }

        boolean isBurning = this.burnTime > 0;
        if(wasBurning != isBurning) {
            BlockState state = this.getBlockState();
            if(state.hasProperty(MachineFurnaceBrickBlock.LIT)) {
                this.level.setBlock(this.worldPosition, state.setValue(MachineFurnaceBrickBlock.LIT, isBurning), Block.UPDATE_ALL);
            }
            changed = true;
        }

        if(changed) this.setChanged();
        this.networkPackNT(15);
    }

    public int getBurnSpeed() {
        ItemStack input = this.slots.get(SLOT_INPUT);
        if(input.isEmpty()) return 1;

        if(input.is(Items.CLAY_BALL)
                || input.is(NtmItems.BALL_FIRECLAY.get())
                || input.is(Blocks.NETHERRACK.asItem())) {
            return 4;
        }

        if(input.is(Blocks.COBBLESTONE.asItem())
                || input.is(Blocks.SAND.asItem())
                || input.is(ItemTags.LOGS)) {
            return 2;
        }

        return 1;
    }

    private boolean processAsh(int level, Item ash, int threshold) {
        if(level < threshold) return false;

        ItemStack output = this.slots.get(SLOT_ASH);
        if(output.isEmpty()) {
            this.slots.set(SLOT_ASH, new ItemStack(ash));
            return true;
        }

        if(output.is(ash) && output.getCount() < output.getMaxStackSize()) {
            output.grow(1);
            return true;
        }

        return false;
    }

    private boolean canSmelt() {
        ItemStack result = this.getSmeltingResult(this.slots.get(SLOT_INPUT));
        if(result.isEmpty()) return false;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) return true;

        return ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void smeltItem() {
        ItemStack result = this.getSmeltingResult(this.slots.get(SLOT_INPUT));
        if(result.isEmpty()) return;

        ItemStack output = this.slots.get(SLOT_OUTPUT);
        if(output.isEmpty()) {
            this.slots.set(SLOT_OUTPUT, result.copy());
        } else {
            output.grow(result.getCount());
        }

        ItemStack input = this.slots.get(SLOT_INPUT);
        input.shrink(1);
        if(input.isEmpty()) this.slots.set(SLOT_INPUT, ItemStack.EMPTY);
    }

    private ItemStack getSmeltingResult(ItemStack stack) {
        if(this.level == null || stack.isEmpty()) return ItemStack.EMPTY;

        SingleRecipeInput input = new SingleRecipeInput(stack);
        return this.level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, input, this.level)
                .map(recipe -> recipe.value().assemble(input, this.level.registryAccess()))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if(slot == SLOT_INPUT) return true;
        if(slot == SLOT_FUEL) return burnModule.getBurnTime(stack) > 0;
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if(direction == Direction.DOWN) return new int[] { SLOT_OUTPUT, SLOT_FUEL, SLOT_ASH };
        if(direction == Direction.UP) return new int[] { SLOT_INPUT };
        return new int[] { SLOT_FUEL };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == SLOT_OUTPUT || slot == SLOT_ASH;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.burnTime = tag.getInt("burnTime");
        this.maxBurnTime = tag.getInt("maxBurnTime");
        this.progress = tag.getInt("progress");
        this.ashLevelWood = tag.getInt("ashLevelWood");
        this.ashLevelCoal = tag.getInt("ashLevelCoal");
        this.ashLevelMisc = tag.getInt("ashLevelMisc");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("burnTime", this.burnTime);
        tag.putInt("maxBurnTime", this.maxBurnTime);
        tag.putInt("progress", this.progress);
        tag.putInt("ashLevelWood", this.ashLevelWood);
        tag.putInt("ashLevelCoal", this.ashLevelCoal);
        tag.putInt("ashLevelMisc", this.ashLevelMisc);
    }

    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);
        buf.writeInt(this.burnTime);
        buf.writeInt(this.maxBurnTime);
        buf.writeInt(this.progress);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);
        this.burnTime = buf.readInt();
        this.maxBurnTime = buf.readInt();
        this.progress = buf.readInt();
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineFurnaceBrickMenu(id, inventory, this);
    }
}
