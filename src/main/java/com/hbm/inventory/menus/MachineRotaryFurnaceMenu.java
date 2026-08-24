package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineRotaryFurnaceBlockEntity;
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

public class MachineRotaryFurnaceMenu extends MenuBase<MachineRotaryFurnaceBlockEntity> {

    public MachineRotaryFurnaceMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineRotaryFurnaceBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineRotaryFurnaceMenu(int id, Inventory inventory, MachineRotaryFurnaceBlockEntity be) {
        super(NtmMenuTypes.MACHINE_ROTARY_FURNACE.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, MachineRotaryFurnaceBlockEntity.SLOT_INPUT_1, 8, 18));
        this.addSlot(new SlotNonRetarded(be, MachineRotaryFurnaceBlockEntity.SLOT_INPUT_2, 26, 18));
        this.addSlot(new SlotNonRetarded(be, MachineRotaryFurnaceBlockEntity.SLOT_INPUT_3, 44, 18));
        this.addSlot(new SlotNonRetarded(be, MachineRotaryFurnaceBlockEntity.SLOT_FLUID_IDENTIFIER, 8, 54));
        this.addSlot(new SlotNonRetarded(be, MachineRotaryFurnaceBlockEntity.SLOT_FUEL, 44, 54));
        this.addSlot(new SlotTakeOnly(be, MachineRotaryFurnaceBlockEntity.SLOT_OUTPUT, 98, 45));

        this.playerInv(inventory, 8, 104, 162);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index < 6) {
                if(!this.moveItemStackTo(stack, 6, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, MachineRotaryFurnaceBlockEntity.SLOT_FLUID_IDENTIFIER, MachineRotaryFurnaceBlockEntity.SLOT_FLUID_IDENTIFIER + 1, false)) return ItemStack.EMPTY;
            } else if(MachineRotaryFurnaceBlockEntity.burnModule.getBurnTime(stack) > 0) {
                if(!this.moveItemStackTo(stack, MachineRotaryFurnaceBlockEntity.SLOT_FUEL, MachineRotaryFurnaceBlockEntity.SLOT_FUEL + 1, false)) return ItemStack.EMPTY;
            } else if(this.be.canPlaceItem(MachineRotaryFurnaceBlockEntity.SLOT_INPUT_1, stack)) {
                if(!this.moveItemStackTo(stack, MachineRotaryFurnaceBlockEntity.SLOT_INPUT_1, MachineRotaryFurnaceBlockEntity.SLOT_INPUT_3 + 1, false)) return ItemStack.EMPTY;
            } else {
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
