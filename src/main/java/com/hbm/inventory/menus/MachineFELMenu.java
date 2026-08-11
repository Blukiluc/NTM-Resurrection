package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineFELBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineFELMenu extends MenuBase<MachineFELBlockEntity> {

    public MachineFELMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineFELBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineFELMenu(int id, Inventory inventory, MachineFELBlockEntity be) {
        super(NtmMenuTypes.FEL.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 182, 144));
        // Crystal
        this.addSlot(new SlotNonRetarded(be, 1, 141, 23));

        this.playerInv(inventory, 8, 83);
    }
}
