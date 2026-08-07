package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.oil.MachineCatalyticCrackingTowerBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.items.machine.IItemFluidIdentifier;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

public class MachineCatalyticCrackingTowerBlock extends DummyableBlock implements ILookOverlay {

    public MachineCatalyticCrackingTowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch (type) {
            case CORE -> new MachineCatalyticCrackingTowerBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineCatalyticCrackingTowerBlock> CODEC = simpleCodec(MachineCatalyticCrackingTowerBlock::new);
    @Override public MapCodec<MachineCatalyticCrackingTowerBlock> codec() { return CODEC; }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!world.isClientSide && !player.isShiftKeyDown()) {
            if (player.getItemInHand(hand).getItem() instanceof IItemFluidIdentifier) {
                BlockPos core = this.findCore(world, pos);
                if (core == null) return ItemInteractionResult.FAIL;

                BlockEntity te = world.getBlockEntity(core);
                if (!(te instanceof MachineCatalyticCrackingTowerBlockEntity cracker)) return ItemInteractionResult.FAIL;

                FluidType type = ((IItemFluidIdentifier) player.getItemInHand(hand).getItem()).getType(world, core, player.getItemInHand(hand));
                cracker.tanks[0].setTankType(type);
                cracker.setChanged();
                player.displayClientMessage(Component.translatable("chat.catalytic_cracking_tower.changed", Component.translatable(type.getUnlocalizedName())), false);

                return ItemInteractionResult.SUCCESS;
            }
            return ItemInteractionResult.FAIL;
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    public int[] getDimensions() {
        return new int[] {0, 0, 3, 3, 2, 3};
    }

    @Override
    public int getOffset() {
        return 3;
    }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        return super.checkRequirement(level, pos, dir, offset) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[]{8, -1, 3, -1, 2, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[]{13, 0, 0, 3, 2, 1}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[]{14, -13, -1, 2, 1, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.relative(dir, offset), new int[]{3, -1, 2, 3, -1, 3}, pos, dir);
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        BlockPos corePos = pos.relative(dir, offset);

        MultiblockHandlerXR.fillSpace(level, corePos, new int[]{8, -1, 3, -1, 2, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[]{13, 0, 0, 3, 2, 1}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[]{14, -13, -1, 2, 1, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, corePos, new int[]{3, -1, 2, 3, -1, 3}, this, dir);

        Direction rot = dir.getClockWise();

        int x = pos.getX() + dir.getStepX() * offset;
        int y = pos.getY();
        int z = pos.getZ() + dir.getStepZ() * offset;

        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 3 + rot.getStepX(), y, z + dir.getStepZ() * 3 + rot.getStepZ()));
        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 3 - rot.getStepX() * 2, y, z + dir.getStepZ() * 3 - rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 3 + rot.getStepX(), y, z - dir.getStepZ() * 3 + rot.getStepZ()));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 3 - rot.getStepX() * 2, y, z - dir.getStepZ() * 3 - rot.getStepZ() * 2));

        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 2 + rot.getStepX() * 2, y, z + dir.getStepZ() * 2 + rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x + dir.getStepX() * 2 - rot.getStepX() * 3, y, z + dir.getStepZ() * 2 - rot.getStepZ() * 3));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 2 + rot.getStepX() * 2, y, z - dir.getStepZ() * 2 + rot.getStepZ() * 2));
        this.makeExtra(level, new BlockPos(x - dir.getStepX() * 2 - rot.getStepX() * 3, y, z - dir.getStepZ() * 2 - rot.getStepZ() * 3));
    }

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if (corePos == null) return;

        BlockEntity te = level.getBlockEntity(corePos);
        if (!(te instanceof MachineCatalyticCrackingTowerBlockEntity cracker)) return;

        List<Component> text = new ArrayList<>();
        for (int i = 0; i < cracker.tanks.length; i++) {
            FluidTank tank = cracker.tanks[i];
            String arrow = (i < 2) ? "-> " : "<- ";
            ChatFormatting color = (i < 2) ? ChatFormatting.GREEN : ChatFormatting.RED;

            Component line = Component.empty()
                    .append(Component.literal(arrow).withStyle(color))
                    .append(Component.translatable(tank.getTankType().getUnlocalizedName()))
                    .append(Component.literal(": " + tank.getFill() + "/" + tank.getMaxFill() + " mB"));
            text.add(line);
        }

        ILookOverlay.printGeneric(event, Component.translatable(this.getDescriptionId()), 0xffff00, 0x404000, text);
    }
}