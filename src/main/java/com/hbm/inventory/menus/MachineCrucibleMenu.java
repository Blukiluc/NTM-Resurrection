package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.MachineCrucibleBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class MachineCrucibleMenu extends MenuBase<MachineCrucibleBlockEntity> {

    public MachineCrucibleMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (MachineCrucibleBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public MachineCrucibleMenu(int id, Inventory inventory, MachineCrucibleBlockEntity be) {
        super(NtmMenuTypes.CRUCIBLE.get(), be, id);
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                this.addSlot(new SlotNonRetarded(be, column + row * 3 + 1, 107 + column * 18, 18 + row * 18));
            }
        }
        this.playerInv(inventory, 8, 132, 190);
    }
}
