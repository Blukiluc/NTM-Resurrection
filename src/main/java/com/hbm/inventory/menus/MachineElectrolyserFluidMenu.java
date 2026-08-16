package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineElectrolyserFluidMenu extends MenuBase<MachineElectrolyserBlockEntity> {

    public MachineElectrolyserFluidMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineElectrolyserBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineElectrolyserFluidMenu(int id, Inventory inventory, MachineElectrolyserBlockEntity be) {
        super(NtmMenuTypes.ELECTROLYSER_FLUID.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 186, 109));
        // Upgrades (not wired yet, slots still exist)
        this.addSlot(new SlotNonRetarded(be, 1, 186, 140));
        this.addSlot(new SlotNonRetarded(be, 2, 186, 158));
        // Fluid ID
        this.addSlot(new SlotNonRetarded(be, 3, 6, 18));
        this.addSlot(new SlotTakeOnly(be, 4, 6, 54));
        // Water in
        this.addSlot(new SlotNonRetarded(be, 5, 24, 18));
        this.addSlot(new SlotTakeOnly(be, 6, 24, 54));
        // Hydrogen out
        this.addSlot(new SlotNonRetarded(be, 7, 78, 18));
        this.addSlot(new SlotTakeOnly(be, 8, 78, 54));
        // Oxygen out
        this.addSlot(new SlotNonRetarded(be, 9, 134, 18));
        this.addSlot(new SlotTakeOnly(be, 10, 134, 54));
        // Byproducts
        this.addSlot(new SlotTakeOnly(be, 11, 154, 18));
        this.addSlot(new SlotTakeOnly(be, 12, 154, 36));
        this.addSlot(new SlotTakeOnly(be, 13, 154, 54));

        this.playerInv(inventory, 8, 122);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 13) {
                if(!this.moveItemStackTo(stack, 14, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;

                    // TODO: re-enable once the upgrade system is wired for this machine
                    // } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    //     if(!this.moveItemStackTo(stack, 1, 3, false)) return ItemStack.EMPTY;

                } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[0].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 5, 6, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[1].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 7, 8, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[2].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 9, 10, false)) return ItemStack.EMPTY;
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if(stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return ret;
    }
}