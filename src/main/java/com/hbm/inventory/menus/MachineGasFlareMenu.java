package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.oil.MachineGasFlareBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineGasFlareMenu extends MenuBase<MachineGasFlareBlockEntity> {

    public MachineGasFlareMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineGasFlareBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineGasFlareMenu(int id, Inventory inventory, MachineGasFlareBlockEntity be) {
        super(NtmMenuTypes.GAS_FLARE.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, 0, 143, 71));
        this.addSlot(new SlotNonRetarded(be, 1, 17, 17));
        this.addSlot(new SlotTakeOnly(be, 2, 17, 53));
        this.addSlot(new SlotNonRetarded(be, 3, 35, 71));
        this.addSlot(new SlotNonRetarded(be, 4, 80, 71));
        this.addSlot(new SlotNonRetarded(be, 5, 98, 71));

        this.playerInv(inventory, 8, 121, 179);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if(index <= 5) {
                if(!this.moveItemStackTo(stack, 6, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof MachineUpgradeItem) {
                if(!this.moveItemStackTo(stack, 4, 6, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
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
