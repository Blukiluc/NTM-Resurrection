package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineCentrifugeBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineCentrifugeMenu extends MenuBase<MachineCentrifugeBlockEntity> {

    public MachineCentrifugeMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCentrifugeBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineCentrifugeMenu(int id, Inventory inventory, MachineCentrifugeBlockEntity be) {
        super(NtmMenuTypes.CENTRIFUGE.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 9, 50));
        // Upgrades
        this.addSlots(be, 1, 149, 22, 2, 1);
        // Input
        this.addSlot(new SlotNonRetarded(be, 3, 36, 50));
        // Outputs
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 4, 63, 50));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 5, 83, 50));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 6, 103, 50));
        this.addSlot(new SlotCraftingOutput(inventory.player, be, 7, 123, 50));

        this.playerInv(inventory, 8, 104);
    }
}
