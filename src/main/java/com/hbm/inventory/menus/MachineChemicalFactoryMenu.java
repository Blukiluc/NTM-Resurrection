package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineChemicalFactoryBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineChemicalFactoryMenu extends MenuBase<MachineChemicalFactoryBlockEntity> {

    public MachineChemicalFactoryMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineChemicalFactoryBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level, extraData.readBlockPos()));
    }

    public MachineChemicalFactoryMenu(int id, Inventory inventory, MachineChemicalFactoryBlockEntity be) {
        super(NtmMenuTypes.CHEMICAL_FACTORY.get(), be, id);

        // Battery
        this.addSlot(new SlotNonRetarded(be, 0, 224, 88));
        // Upgrades
        this.addSlots(be, 1, 206, 125, 3, 1);

        for(int i = 0; i < 4; i++) {
            // Template
            this.addSlot(new SlotNonRetarded(be, 4 + i * 7, 93, 20 + i * 22));
            // Solid Input
            this.addSlots(be, 5 + i * 7, 10, 20 + i * 22, 1, 3, 16);
            // Solid Output
            this.addOutputSlots(inventory.player, be, 8 + i * 7, 139, 20 + i * 22, 1, 3, 16);
        }

        this.playerInv(inventory, 26, 134);
    }
}
