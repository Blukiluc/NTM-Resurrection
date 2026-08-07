package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineShredderBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineShredderMenu extends MenuBase<MachineShredderBlockEntity> {

    public MachineShredderMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineShredderBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineShredderMenu(int id, Inventory inventory, MachineShredderBlockEntity be) {
        super(NtmMenuTypes.SHREDDER.get(), be, id);

        // Inputs (9)
        this.addSlots(be, 0, 44, 18, 3, 3);

        // Outputs (18)
        this.addOutputSlots(inventory.player, be, 9, 116, 18, 6, 3);

        // Blades (left/right)
        this.addSlot(new SlotNonRetarded(be, 27, 44, 108));
        this.addSlot(new SlotNonRetarded(be, 28, 80, 108));

        // Battery
        this.addSlot(new SlotNonRetarded(be, 29, 8, 108));

        this.playerInv(inventory, 8, 151);
    }
}
