package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineOreAcidizerBlockEntity;
import com.hbm.inventory.FluidContainerRegistry;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.hbm.items.machine.MachineUpgradeItem;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineOreAcidizerMenu extends MenuBase<MachineOreAcidizerBlockEntity> {

    public MachineOreAcidizerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineOreAcidizerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineOreAcidizerMenu(int id, Inventory inventory, MachineOreAcidizerBlockEntity be) {
        super(NtmMenuTypes.ORE_ACIDIZER.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_INPUT, 62, 45));
        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_BATTERY, 152, 72));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineOreAcidizerBlockEntity.SLOT_OUTPUT, 113, 45));
        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_FLUID_INPUT, 17, 18));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineOreAcidizerBlockEntity.SLOT_FLUID_OUTPUT, 17, 54));
        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_UPGRADE_1, 80, 18));
        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_UPGRADE_2, 98, 18));
        this.addSlot(new SlotNonRetarded(be, MachineOreAcidizerBlockEntity.SLOT_FLUID_ID, 35, 72));

        this.playerInv(inventory, 8, 122, 180);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= MachineOreAcidizerBlockEntity.SLOT_FLUID_ID) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, MachineOreAcidizerBlockEntity.SLOT_BATTERY, MachineOreAcidizerBlockEntity.SLOT_BATTERY + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, MachineOreAcidizerBlockEntity.SLOT_FLUID_ID, MachineOreAcidizerBlockEntity.SLOT_FLUID_ID + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof MachineUpgradeItem) {
                if(!this.moveItemStackTo(stack, MachineOreAcidizerBlockEntity.SLOT_UPGRADE_1, MachineOreAcidizerBlockEntity.SLOT_UPGRADE_2 + 1, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, MachineOreAcidizerBlockEntity.SLOT_FLUID_INPUT, MachineOreAcidizerBlockEntity.SLOT_FLUID_INPUT + 1, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, MachineOreAcidizerBlockEntity.SLOT_INPUT, MachineOreAcidizerBlockEntity.SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return ret;
    }
}
