package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.oil.MachineCatalyticReformerBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineCatalyticReformerMenu extends MenuBase<MachineCatalyticReformerBlockEntity> {

    public MachineCatalyticReformerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCatalyticReformerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineCatalyticReformerMenu(int id, Inventory inventory, MachineCatalyticReformerBlockEntity be) {
        super(NtmMenuTypes.CATALYTIC_REFORMER.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 17, 90));

        // Fluid ID
        this.addSlot(new SlotNonRetarded(be, 1, 17, 108));

        // Catalytic converter
        this.addSlot(new SlotNonRetarded(be, 2, 71, 36));

        // Input fluid
        this.addSlot(new SlotNonRetarded(be, 3, 35, 90));
        this.addSlot(new SlotTakeOnly(be, 4, 35, 108));

        // Output fluids
        this.addSlots(be, 5, 107, 90, 1, 3);
        this.addOutputSlots(inventory.player, be, 8, 107, 108, 1, 3);

        this.playerInv(inventory, 8, 156);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 10) {
                if(!this.moveItemStackTo(stack, 11, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
                } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                    if(!this.moveItemStackTo(stack, 9, 10, false)) return ItemStack.EMPTY;
                } else if(stack.is(NtmItems.CATALYTIC_CONVERTER.get())) {
                    if(!this.moveItemStackTo(stack, 10, 11, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[0].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[1].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[2].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 5, 6, false)) return ItemStack.EMPTY;
                } else if(!FluidContainerRegistry.getFullContainer(stack, this.be.tanks[3].getTankType()).isEmpty()) {
                    if(!this.moveItemStackTo(stack, 7, 8, false)) return ItemStack.EMPTY;
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