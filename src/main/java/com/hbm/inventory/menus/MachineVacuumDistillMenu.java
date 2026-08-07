package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.oil.MachineVacuumDistillBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineVacuumDistillMenu extends MenuBase<MachineVacuumDistillBlockEntity> {

    public MachineVacuumDistillMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineVacuumDistillBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineVacuumDistillMenu(int id, Inventory inventory, MachineVacuumDistillBlockEntity be) {
        super(NtmMenuTypes.VACUUM_REFINERY.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 26, 90));

        // Fluid identifier (reads the current input tank type)
        this.addSlot(new Slot(be, 1, 26, 108));

        // Fluid inputs
        this.addOutputSlots(inventory.player, be, 2, 80, 90, 1, 4);

        // Fluid outputs
        this.addOutputSlots(inventory.player, be, 6, 80, 108, 1, 4);

        this.playerInv(inventory, 8, 156);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 11) {
                if(!this.moveItemStackTo(stack, 12, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(!this.moveItemStackTo(stack, 0, 12, false)) {
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