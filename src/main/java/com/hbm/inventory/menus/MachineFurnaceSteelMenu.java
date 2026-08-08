package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineFurnaceSteelBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineFurnaceSteelMenu extends MenuBase<MachineFurnaceSteelBlockEntity> {

    public MachineFurnaceSteelMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFurnaceSteelBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineFurnaceSteelMenu(int id, Inventory inventory, MachineFurnaceSteelBlockEntity be) {
        super(NtmMenuTypes.FURNACE_STEEL.get(), be, id);
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceSteelBlockEntity.SLOT_INPUT_1, 35, 17));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceSteelBlockEntity.SLOT_INPUT_2, 35, 35));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceSteelBlockEntity.SLOT_INPUT_3, 35, 53));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceSteelBlockEntity.SLOT_OUTPUT_1, 125, 17));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceSteelBlockEntity.SLOT_OUTPUT_2, 125, 35));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceSteelBlockEntity.SLOT_OUTPUT_3, 125, 53));
        this.playerInv(inventory, 8, 84, 142);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(!slot.hasItem()) return result;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if(index <= MachineFurnaceSteelBlockEntity.SLOT_OUTPUT_3) {
            if(!this.moveItemStackTo(stack, 6, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if(!this.moveItemStackTo(stack, MachineFurnaceSteelBlockEntity.SLOT_INPUT_1, MachineFurnaceSteelBlockEntity.SLOT_INPUT_3 + 1, false)) {
            return ItemStack.EMPTY;
        }

        if(stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return result;
    }
}
