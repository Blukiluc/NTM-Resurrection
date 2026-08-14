package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachinePUREXBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachinePUREXMenu extends MenuBase<MachinePUREXBlockEntity> {

    public MachinePUREXMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachinePUREXBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachinePUREXMenu(int id, Inventory inventory, MachinePUREXBlockEntity be) {
        super(NtmMenuTypes.PUREX.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 152, 81));
        // Schematic
        this.addSlot(new SlotNonRetarded(be, 1, 35, 126));
        // Upgrades
        this.addSlots(be, 2, 152, 108, 2, 1);
        // Solid Input
        this.addSlots(be, 4, 8, 90, 1, 3);
        // Solid Output
        this.addOutputSlots(inventory.player, be, 7, 80, 36, 3, 2);

        this.playerInv(inventory, 8, 174);
    }
}
