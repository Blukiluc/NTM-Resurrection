package com.hbm.inventory.menus;

import com.hbm.blockentity.machine.heater.HeaterFluidBurnerBlockEntity;
import com.hbm.inventory.NtmMenuTypes;
import com.hbm.inventory.SlotNonRetarded;
import com.hbm.util.CompatExternal;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class HeaterFluidBurnerMenu extends MenuBase<HeaterFluidBurnerBlockEntity> {

    public HeaterFluidBurnerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, (HeaterFluidBurnerBlockEntity) CompatExternal.getCoreFromPos(inventory.player.level(), extraData.readBlockPos()));
    }

    public HeaterFluidBurnerMenu(int id, Inventory inventory, HeaterFluidBurnerBlockEntity be) {
        super(NtmMenuTypes.HEATER_OILBURNER.get(), be, id);

        this.addSlot(new SlotNonRetarded(be, 0, 26, 17));
        this.addSlot(new SlotNonRetarded(be, 1, 26, 53));
        this.addSlot(new SlotNonRetarded(be, 2, 44, 71));

        this.playerInv(inventory, 8, 121);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return super.quickMoveStack(player, index);
    }
}
