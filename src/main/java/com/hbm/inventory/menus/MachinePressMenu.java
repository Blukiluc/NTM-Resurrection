package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachinePressBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachinePressMenu extends MenuBase<MachinePressBlockEntity> {

    public MachinePressMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachinePressBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachinePressMenu(int id, Inventory inventory, MachinePressBlockEntity be) {
        super(NtmMenuTypes.PRESS.get(), be, id);

        // Input
        this.addSlot(new SlotNonRetarded(be, 0, 80, 53));

        // Fuel
        this.addSlot(new SlotNonRetarded(be, 1, 26, 53));

        // Plate
        this.addSlot(new SlotNonRetarded(be, 2, 80, 17));

        // Output
        this.addOutputSlots(inventory.player, be, 3, 140, 35, 1, 1);

        // Storage
        this.addSlots(be, 4, 8, 84, 1, 9);

        this.playerInv(inventory, 8, 120);
    }
}
