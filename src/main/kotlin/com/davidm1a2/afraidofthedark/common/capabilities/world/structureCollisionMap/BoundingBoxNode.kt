package com.davidm1a2.afraidofthedark.common.capabilities.world.structureCollisionMap

import net.minecraft.nbt.IntNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.util.math.MutableBoundingBox
import kotlin.math.max
import kotlin.math.min

internal class BoundingBoxNode(
    private var aabb: MutableBoundingBox,
    var leftChild: BoundingBoxNode? = null,
    var rightChild: BoundingBoxNode? = null
) {
    constructor(leftChild: BoundingBoxNode, rightChild: BoundingBoxNode) : this(leftChild.aabb.union(rightChild.aabb), leftChild, rightChild)

    fun isLeaf(): Boolean {
        return leftChild == null && rightChild == null
    }

    fun intersects(aabbOther: MutableBoundingBox): Boolean {
        return aabb.intersectsWith(aabbOther)
    }

    fun recalculateSize() {
        leftChild?.let { aabb = aabb.union(it.aabb) }
        rightChild?.let { aabb = aabb.union(it.aabb) }
    }

    fun computeAreaDiffIfContaining(nodeOther: BoundingBoxNode): Int {
        val oldArea = aabb.area()
        val newArea = aabb.union(nodeOther.aabb).area()
        return newArea - oldArea
    }

    fun writeToList(nbtTagList: ListNBT) {
        nbtTagList.add(IntNBT.valueOf(aabb.minX))
        nbtTagList.add(IntNBT.valueOf(aabb.minY))
        nbtTagList.add(IntNBT.valueOf(aabb.minY))
        nbtTagList.add(IntNBT.valueOf(aabb.maxX))
        nbtTagList.add(IntNBT.valueOf(aabb.maxY))
        nbtTagList.add(IntNBT.valueOf(aabb.maxZ))
    }

    companion object {
        private fun MutableBoundingBox.area(): Int {
            return (maxX - minX) * (maxY - minY) * (maxZ - minZ)
        }

        private fun MutableBoundingBox.union(other: MutableBoundingBox): MutableBoundingBox {
            val minX = min(minX, other.minX)
            val minY = min(minY, other.minY)
            val minZ = min(minZ, other.minZ)
            val maxX = max(maxX, other.maxX)
            val maxY = max(maxY, other.maxY)
            val maxZ = max(maxZ, other.maxZ)
            return MutableBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
        }
    }
}