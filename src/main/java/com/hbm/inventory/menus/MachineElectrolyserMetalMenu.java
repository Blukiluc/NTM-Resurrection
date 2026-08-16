package com.hbm.inventory.menus;

import api.hbm.energymk2.IBatteryItem;
import com.hbm.blockentity.machine.MachineElectrolyserBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.inventory.SlotTakeOnly;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MachineElectrolyserMetalMenu extends MenuBase<MachineElectrolyserBlockEntity> {

    public MachineElectrolyserMetalMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineElectrolyserBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineElectrolyserMetalMenu(int id, Inventory inventory, MachineElectrolyserBlockEntity be) {
        super(NtmMenuTypes.ELECTROLYSER_METAL.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 186, 109));
        // Upgrades (not wired yet, slots still exist)
        this.addSlot(new SlotNonRetarded(be, 1, 186, 140));
        this.addSlot(new SlotNonRetarded(be, 2, 186, 158));
        // Crystal input
        this.addSlot(new SlotNonRetarded(be, 14, 10, 22));
        // Metal outputs
        this.addSlot(new SlotTakeOnly(be, 15, 136, 18));
        this.addSlot(new SlotTakeOnly(be, 16, 154, 18));
        this.addSlot(new SlotTakeOnly(be, 17, 136, 36));
        this.addSlot(new SlotTakeOnly(be, 18, 154, 36));
        this.addSlot(new SlotTakeOnly(be, 19, 136, 54));
        this.addSlot(new SlotTakeOnly(be, 20, 154, 54));

        this.playerInv(inventory, 8, 122);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            ret = stack.copy();

            if(index <= 9) {
                if(!this.moveItemStackTo(stack, 10, this.slots.size(), true)) return ItemStack.EMPTY;
            } else {
                if(stack.getItem() instanceof IBatteryItem) {
                    if(!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;

                    // TODO: re-enable once the upgrade system is wired for this machine
                    // } else if(stack.getItem() instanceof MachineUpgradeItem) {
                    //     if(!this.moveItemStackTo(stack, 1, 3, false)) return ItemStack.EMPTY;

                } else {
                    if(!this.moveItemStackTo(stack, 3, 4, false)) return ItemStack.EMPTY;
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