package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.blockentity.machine.MachineGasCentrifugeBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineGasCentrifugeMenu extends MenuBase<MachineGasCentrifugeBlockEntity> {

    public MachineGasCentrifugeMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineGasCentrifugeBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineGasCentrifugeMenu(int id, Inventory inventory, MachineGasCentrifugeBlockEntity be) {
        super(NtmMenuTypes.GAS_CENTRIFUGE.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 182, 71));
        // Fluid Identifier
        this.addSlot(new SlotNonRetarded(be, 1, 91, 15));
        // Upgrades
        this.addSlot(new SlotNonRetarded(be, 2, 69, 15));
        // Outputs
        this.addOutputSlots(inventory.player, be, 3, 71, 53, 2, 2);

        this.playerInv(inventory, 8, 122);
    }
}
