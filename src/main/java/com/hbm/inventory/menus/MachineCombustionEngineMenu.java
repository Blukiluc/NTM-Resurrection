package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineCombustionEngineBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.PistonSetItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineCombustionEngineMenu extends MenuBase<MachineCombustionEngineBlockEntity> {

    public MachineCombustionEngineMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCombustionEngineBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineCombustionEngineMenu(int id, Inventory inventory, MachineCombustionEngineBlockEntity be) {
        super(NtmMenuTypes.COMBUSTION_ENGINE.get(), be, id);

        this.be.startOpen(inventory.player);

        this.addSlot(new SlotNonRetarded(be, 0, 17, 17));
        this.addSlot(new SlotTakeOnly(be, 1, 17, 53));
        this.addSlot(new SlotNonRetarded(be, 2, 88, 71));
        this.addSlot(new SlotNonRetarded(be, 3, 143, 71));
        this.addSlot(new SlotNonRetarded(be, 4, 35, 71));

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
            } else if(stack.getItem() instanceof PistonSetItem) {
                if(!this.moveItemStackTo(stack, 2, 3, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.be.stopOpen(player);
    }
}
