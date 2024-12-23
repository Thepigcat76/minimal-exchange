package com.thepigcat.minimal_exchange.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public final class LevelUtils {
    public static AABB getAABBbox(BlockPos pos, Direction direction, int depth) {
        return switch (direction) {
            case EAST -> new AABB(pos.getX() - depth, pos.getY() - 1, pos.getZ() - 1, pos.getX(), pos.getY() + 1,
                    pos.getZ() + 1);
            case WEST -> new AABB(pos.getX(), pos.getY() - 1, pos.getZ() - 1, pos.getX() + depth, pos.getY() + 1,
                    pos.getZ() + 1);
            case UP -> new AABB(pos.getX() - 1, pos.getY() - depth, pos.getZ() - 1, pos.getX() + 1, pos.getY(),
                    pos.getZ() + 1);
            case DOWN -> new AABB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 1, pos.getY() + depth,
                    pos.getZ() + 1);
            case SOUTH -> new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - depth, pos.getX() + 1, pos.getY() + 1,
                    pos.getZ());
            case NORTH -> new AABB(pos.getX() - 1, pos.getY() - 1, pos.getZ(), pos.getX() + 1, pos.getY() + 1,
                    pos.getZ() + depth);
        };
    }

    public static Iterable<BlockPos> getPositionsFromBox(AABB box) {
        return getPositionsFromBox(new BlockPos((int) box.minX, (int) box.minY, (int) box.minZ), new BlockPos(
                (int) box.maxX, (int) box.maxY, (int) box.maxZ));
    }

    public static Iterable<BlockPos> getPositionsFromBox(BlockPos corner1, BlockPos corner2) {
        return () -> BlockPos.betweenClosedStream(corner1, corner2).iterator();
    }
}
