package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineRTGBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineRTGMenu extends MenuBase<MachineRTGBlockEntity> {

    public MachineRTGMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineRTGBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineRTGMenu(int id, Inventory inventory, MachineRTGBlockEntity be) {
        super(NtmMenuTypes.RTG.get(), be, id);

        this.addSlots(be, 0, 16, 18, 3, 5);
        this.playerInv(inventory, 8, 106, 164);
    }
}
