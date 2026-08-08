package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineWoodBurnerBlockEntity;
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

public class MachineWoodBurnerMenu extends MenuBase<MachineWoodBurnerBlockEntity> {

    public MachineWoodBurnerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineWoodBurnerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineWoodBurnerMenu(int id, Inventory inventory, MachineWoodBurnerBlockEntity be) {
        super(NtmMenuTypes.WOOD_BURNER.get(), be, id);

        // Fuel
        this.addSlot(new SlotNonRetarded(be, 0, 26, 18));
        // Ashes
        this.addSlot(new SlotTakeOnly(be, 1, 26, 54));
        // Fluid ID
        this.addSlot(new SlotNonRetarded(be, 2, 98, 54));
        // Fluid Container
        this.addSlot(new SlotNonRetarded(be, 3, 98, 18));
        this.addSlot(new SlotTakeOnly(be, 4, 98, 36));
        // Battery
        this.addSlot(new SlotNonRetarded(be, 5, 143, 54));

        this.playerInv(inventory, 8, 104, 162);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 5) {
                if(!this.moveItemStackTo(stack, 6, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, 5, 6, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, 2, 3, false)) return ItemStack.EMPTY;
            } else if(MachineWoodBurnerBlockEntity.burnModule.getBurnTime(stack) > 0) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, 3, 4, false)) {
                return ItemStack.EMPTY;
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
