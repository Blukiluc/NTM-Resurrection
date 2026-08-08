package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineElectricPressBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.machine.ItemStamp;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineElectricPressMenu extends MenuBase<MachineElectricPressBlockEntity> {

    public MachineElectricPressMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineElectricPressBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineElectricPressMenu(int id, Inventory inventory, MachineElectricPressBlockEntity be) {
        super(NtmMenuTypes.ELECTRIC_PRESS.get(), be, id);

        //Battery
        this.addSlot(new SlotNonRetarded(be, MachineElectricPressBlockEntity.SLOT_BATTERY, 152, 54));
        //Stamp
        this.addSlot(new SlotNonRetarded(be, MachineElectricPressBlockEntity.SLOT_STAMP, 19, 15));
        //Input
        this.addSlot(new SlotNonRetarded(be, MachineElectricPressBlockEntity.SLOT_INPUT, 19, 51));
        //Output
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineElectricPressBlockEntity.SLOT_OUTPUT, 79, 33));
        //Upgrade
        this.addSlot(new SlotNonRetarded(be, MachineElectricPressBlockEntity.SLOT_UPGRADE, 111, 32));

        this.playerInv(inventory, 8, 104, 162);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if(index <= MachineElectricPressBlockEntity.SLOT_UPGRADE) {
                if(!this.moveItemStackTo(stack, 5, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, MachineElectricPressBlockEntity.SLOT_BATTERY, MachineElectricPressBlockEntity.SLOT_BATTERY + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof MachineUpgradeItem) {
                if(!this.moveItemStackTo(stack, MachineElectricPressBlockEntity.SLOT_UPGRADE, MachineElectricPressBlockEntity.SLOT_UPGRADE + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof ItemStamp) {
                if(!this.moveItemStackTo(stack, MachineElectricPressBlockEntity.SLOT_STAMP, MachineElectricPressBlockEntity.SLOT_STAMP + 1, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, MachineElectricPressBlockEntity.SLOT_INPUT, MachineElectricPressBlockEntity.SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return result;
    }
}
