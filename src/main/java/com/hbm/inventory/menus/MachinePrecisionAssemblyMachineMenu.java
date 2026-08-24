package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachinePrecisionAssemblyMachineBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachinePrecisionAssemblyMachineMenu extends MenuBase<MachinePrecisionAssemblyMachineBlockEntity> {

    public MachinePrecisionAssemblyMachineMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachinePrecisionAssemblyMachineBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachinePrecisionAssemblyMachineMenu(int id, Inventory inventory, MachinePrecisionAssemblyMachineBlockEntity be) {
        super(NtmMenuTypes.PREC_ASS.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 152, 81));
        // Schematic
        this.addSlot(new SlotNonRetarded(be, 1, 35, 126));
        // Upgrades
        this.addSlots(be, 2, 152, 108, 2, 1);
        // Input
        this.addSlots(be, 4, 8, 27, 3, 3);
        // Output
        this.addOutputSlots(inventory.player, be, 13, 80, 27, 3, 3);

        this.playerInv(inventory, 8, 174);
    }
}
