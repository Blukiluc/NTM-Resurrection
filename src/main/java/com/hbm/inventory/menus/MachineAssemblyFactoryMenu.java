package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineAssemblyFactoryBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotCraftingOutput;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineAssemblyFactoryMenu extends MenuBase<MachineAssemblyFactoryBlockEntity> {

    public MachineAssemblyFactoryMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineAssemblyFactoryBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineAssemblyFactoryMenu(int id, Inventory inventory, MachineAssemblyFactoryBlockEntity be) {
        super(NtmMenuTypes.ASSEMBLY_FACTORY.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 234, 112));
        // Upgrades
        this.addSlots(be, 1, 214, 149, 3, 1);

        for(int i = 0; i < 4; i++) {
            // Template
            this.addSlot(new SlotNonRetarded(be, 4 + i * 14, 25 + (i % 2) * 109, 54 + (i / 2) * 56));
            // Solid Input
            this.addSlots(be, 5 + i * 14, 7 + (i % 2) * 109, 20 + (i / 2) * 56, 2, 6, 16);
            // Solid Output
            this.addSlot(new SlotCraftingOutput(inventory.player, be, 17 + i * 14, 87 + (i % 2) * 109, 54 + (i / 2) * 56));
        }

        this.playerInv(inventory, 33, 158);
    }
}
