package com.hbm.blocks.machine;

import com.hbm.blockentity.machine.IFactoryPortProvider;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineFactoryBlock extends DummyableBlock implements ILookOverlay {

    protected AbstractMachineFactoryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {2, 0, 2, 2, 2, 2};
    }

    @Override
    public int getOffset() {
        return 2;
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        int x = pos.getX() - dir.getStepX() * 2;
        int y = pos.getY();
        int z = pos.getZ() - dir.getStepZ() * 2;

        for(int i = -2; i <= 2; i++) for(int j = -2; j <= 2; j++) {
            if(Math.abs(i) == 2 || Math.abs(j) == 2) this.makeExtra(level, new BlockPos(x + i, y, z + j));
        }

        Direction rot = dir.getClockWise(Direction.Axis.Y);
        for(int i = -2; i <= 2; i++) {
            this.makeExtra(level, new BlockPos(x + dir.getStepX() * i + rot.getStepX() * 2, y + 2, z + dir.getStepZ() * i + rot.getStepZ() * 2));
            this.makeExtra(level, new BlockPos(x + dir.getStepX() * i - rot.getStepX() * 2, y + 2, z + dir.getStepZ() * i - rot.getStepZ() * 2));
        }
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(!(blockEntity instanceof IFactoryPortProvider factory)) return;

        for(DirPos port : factory.getCoolPos()) {
            if(!matchesPort(port, pos)) continue;

            List<Component> text = new ArrayList<>();
            text.add(tankLine("-> ", ChatFormatting.GREEN, factory.getWaterTank()));
            text.add(tankLine("<- ", ChatFormatting.RED, factory.getSpentSteamTank()));
            ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
            return;
        }

        DirPos[] io = factory.getIOPos();
        for(int i = 0; i < io.length; i++) {
            if(!matchesPort(io[i], pos)) continue;

            List<Component> text = new ArrayList<>();
            text.add(Component.empty()
                    .append(Component.literal("-> ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal("Recipe field [" + (i + 1) + "]")));
            ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
            return;
        }
    }

    private static boolean matchesPort(DirPos port, BlockPos pos) {
        return port.compare(
                pos.getX() + port.getDir().getStepX(),
                pos.getY() + port.getDir().getStepY(),
                pos.getZ() + port.getDir().getStepZ());
    }

    private static Component tankLine(String arrow, ChatFormatting color, FluidTank tank) {
        return Component.empty()
                .append(Component.literal(arrow).withStyle(color))
                .append(tank.getTankType().getName());
    }
}
