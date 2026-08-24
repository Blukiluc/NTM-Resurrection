package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineTurbofanBlockEntity;
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

public class MachineTurbofanMenu extends MenuBase<MachineTurbofanBlockEntity> {

    public MachineTurbofanMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineTurbofanBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineTurbofanMenu(int id, Inventory inventory, MachineTurbofanBlockEntity be) {
        super(NtmMenuTypes.TURBOFAN.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, 0, 17, 17));
        this.addSlot(new SlotTakeOnly(be, 1, 17, 53));
        // slot 2: upgrade slot
        this.addSlot(new SlotNonRetarded(be, 2, 98, 71));
        this.addSlot(new SlotNonRetarded(be, 3, 143, 71));
        this.addSlot(new SlotNonRetarded(be, 4, 44, 71));

        this.playerInv(inventory, 8, 121, 179);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if(index <= 4) {
                if(!this.moveItemStackTo(stack, 5, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, 4, 5, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else {
                // upgrade item check goes here once the upgrade system is ported
                if(!this.moveItemStackTo(stack, 2, 3, false)) return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }
}