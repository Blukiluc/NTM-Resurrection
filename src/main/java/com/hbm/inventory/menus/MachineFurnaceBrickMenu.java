package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineFurnaceBrickBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineFurnaceBrickMenu extends MenuBase<MachineFurnaceBrickBlockEntity> {

    public MachineFurnaceBrickMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFurnaceBrickBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineFurnaceBrickMenu(int id, Inventory inventory, MachineFurnaceBrickBlockEntity be) {
        super(NtmMenuTypes.FURNACE_BRICK.get(), be, id);
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceBrickBlockEntity.SLOT_INPUT, 62, 35));
        this.addSlot(new SlotNonRetarded(be, MachineFurnaceBrickBlockEntity.SLOT_FUEL, 35, 17));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceBrickBlockEntity.SLOT_OUTPUT, 116, 35));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineFurnaceBrickBlockEntity.SLOT_ASH, 35, 53));
        this.playerInv(inventory, 8, 84, 142);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(!slot.hasItem()) return result;

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if(index <= MachineFurnaceBrickBlockEntity.SLOT_ASH) {
            if(!this.moveItemStackTo(stack, 4, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if(MachineFurnaceBrickBlockEntity.burnModule.getBurnTime(stack) > 0) {
            if(!this.moveItemStackTo(stack, MachineFurnaceBrickBlockEntity.SLOT_FUEL, MachineFurnaceBrickBlockEntity.SLOT_FUEL + 1, false)
                    && !this.moveItemStackTo(stack, MachineFurnaceBrickBlockEntity.SLOT_INPUT, MachineFurnaceBrickBlockEntity.SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if(!this.moveItemStackTo(stack, MachineFurnaceBrickBlockEntity.SLOT_INPUT, MachineFurnaceBrickBlockEntity.SLOT_INPUT + 1, false)) {
            return ItemStack.EMPTY;
        }

        if(stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
        else slot.setChanged();
        return result;
    }
}
