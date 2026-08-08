package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineMixerBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineMixerMenu extends MenuBase<MachineMixerBlockEntity> {

    public MachineMixerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineMixerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineMixerMenu(int id, Inventory inventory, MachineMixerBlockEntity be) {
        super(NtmMenuTypes.MIXER.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 12, 72));
        // Item Input
        this.addSlot(new SlotNonRetarded(be, 1, 52, 72));
        // Fluid ID
        this.addSlot(new SlotNonRetarded(be, 2, 126, 72));
        // Upgrades
        this.addSlot(new SlotNonRetarded(be, 3, 148, 18));
        this.addSlot(new SlotNonRetarded(be, 4, 148, 36));

        this.playerInv(inventory, 8, 122, 180);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 4) {
                if(!this.moveItemStackTo(stack, 5, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, 2, 3, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof MachineUpgradeItem) {
                if(!this.moveItemStackTo(stack, 3, 5, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, 1, 2, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return ret;
    }
}
