package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineCrystallizerBlockEntity;
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

public class MachineCrystallizerMenu extends MenuBase<MachineCrystallizerBlockEntity> {

    public MachineCrystallizerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCrystallizerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineCrystallizerMenu(int id, Inventory inventory, MachineCrystallizerBlockEntity be) {
        super(NtmMenuTypes.CRYSTALLIZER.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_INPUT, 62, 45));
        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_BATTERY, 152, 72));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineCrystallizerBlockEntity.SLOT_OUTPUT, 113, 45));
        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_FLUID_INPUT, 17, 18));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, MachineCrystallizerBlockEntity.SLOT_FLUID_OUTPUT, 17, 54));
        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_UPGRADE_1, 80, 18));
        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_UPGRADE_2, 98, 18));
        this.addSlot(new SlotNonRetarded(be, MachineCrystallizerBlockEntity.SLOT_FLUID_ID, 35, 72));

        this.playerInv(inventory, 8, 122, 180);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= MachineCrystallizerBlockEntity.SLOT_FLUID_ID) {
                if(!this.moveItemStackTo(stack, 8, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IBatteryItem) {
                if(!this.moveItemStackTo(stack, MachineCrystallizerBlockEntity.SLOT_BATTERY, MachineCrystallizerBlockEntity.SLOT_BATTERY + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof IItemFluidIdentifier) {
                if(!this.moveItemStackTo(stack, MachineCrystallizerBlockEntity.SLOT_FLUID_ID, MachineCrystallizerBlockEntity.SLOT_FLUID_ID + 1, false)) return ItemStack.EMPTY;
            } else if(stack.getItem() instanceof MachineUpgradeItem) {
                if(!this.moveItemStackTo(stack, MachineCrystallizerBlockEntity.SLOT_UPGRADE_1, MachineCrystallizerBlockEntity.SLOT_UPGRADE_2 + 1, false)) return ItemStack.EMPTY;
            } else if(FluidContainerRegistry.getFluidContent(stack, this.be.tank.getTankType()) > 0) {
                if(!this.moveItemStackTo(stack, MachineCrystallizerBlockEntity.SLOT_FLUID_INPUT, MachineCrystallizerBlockEntity.SLOT_FLUID_INPUT + 1, false)) return ItemStack.EMPTY;
            } else if(!this.moveItemStackTo(stack, MachineCrystallizerBlockEntity.SLOT_INPUT, MachineCrystallizerBlockEntity.SLOT_INPUT + 1, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return ret;
    }
}
