package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineFurnaceIronBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineFurnaceIronMenu extends MenuBase<MachineFurnaceIronBlockEntity> {

    public MachineFurnaceIronMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFurnaceIronBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineFurnaceIronMenu(int id, Inventory inventory, MachineFurnaceIronBlockEntity be) {
        super(NtmMenuTypes.FURNACE_IRON.get(), be, id);
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceIronBlockEntity.SLOT_INPUT, 53, 17));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceIronBlockEntity.SLOT_FUEL_1, 53, 53));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceIronBlockEntity.SLOT_FUEL_2, 71, 53));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceIronBlockEntity.SLOT_OUTPUT, 125, 35));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceIronBlockEntity.SLOT_UPGRADE, 17, 35));
        this.playerInv(inventory, 8, 84, 142);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(!slot.hasItem()) return result;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if(index <= MachineFurnaceIronBlockEntity.SLOT_UPGRADE) {
            if(!this.moveItemStackTo(stack, 5, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if(stack.getItem() instanceof MachineUpgradeItem) {
            if(!this.moveItemStackTo(stack, MachineFurnaceIronBlockEntity.SLOT_UPGRADE, MachineFurnaceIronBlockEntity.SLOT_UPGRADE + 1, false)) return ItemStack.EMPTY;
        } else if(MachineFurnaceIronBlockEntity.burnModule.getBurnTime(stack) > 0) {
            if(!this.moveItemStackTo(stack, MachineFurnaceIronBlockEntity.SLOT_FUEL_1, MachineFurnaceIronBlockEntity.SLOT_FUEL_2 + 1, false)) return ItemStack.EMPTY;
        } else if(!this.moveItemStackTo(stack, MachineFurnaceIronBlockEntity.SLOT_INPUT, MachineFurnaceIronBlockEntity.SLOT_INPUT + 1, false)) {
            return ItemStack.EMPTY;
        }

        if(stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return result;
    }
}
