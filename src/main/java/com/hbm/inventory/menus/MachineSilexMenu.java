package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineSilexBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineSilexMenu extends MenuBase<MachineSilexBlockEntity> {

    public MachineSilexMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineSilexBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineSilexMenu(int id, Inventory inventory, MachineSilexBlockEntity be) {
        super(NtmMenuTypes.SILEX.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, MachineSilexBlockEntity.SLOT_INPUT, 80, 12));
        this.addSlot(new SlotNonRetarded(be, MachineSilexBlockEntity.SLOT_FLUID_ID, 8, 24));
        this.addSlot(new SlotNonRetarded(be, MachineSilexBlockEntity.SLOT_FLUID_INPUT, 26, 24));
        this.addSlot(new SlotTakeOnly(be, MachineSilexBlockEntity.SLOT_FLUID_OUTPUT, 44, 24));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineSilexBlockEntity.SLOT_OUTPUT, 116, 90));
        this.addOutputSlots(inventory.player, be, MachineSilexBlockEntity.SLOT_QUEUE_START, 134, 72, 3, 2);

        this.playerInv(inventory, 8, 140);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < MachineSilexBlockEntity.SLOT_QUEUE_END) {
                if(!this.moveItemStackTo(stack, MachineSilexBlockEntity.SLOT_QUEUE_END, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, MachineSilexBlockEntity.SLOT_FLUID_ID, MachineSilexBlockEntity.SLOT_FLUID_ID + 1, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, MachineSilexBlockEntity.SLOT_FLUID_INPUT, MachineSilexBlockEntity.SLOT_FLUID_INPUT + 1, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, MachineSilexBlockEntity.SLOT_INPUT, MachineSilexBlockEntity.SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return ret;
    }
}
