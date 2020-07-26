package com.davidm1a2.afraidofthedark.common.world.aabb

import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.MutableBoundingBox
import net.minecraftforge.common.util.INBTSerializable
import java.util.*

/**
 * Tree based on this algorithm: https://www.azurefromthetrenches.com/introductory-guide-to-aabb-tree-collision-detection/
 */
class BoundingBoxTree : INBTSerializable<NBTTagList> {
    private var root: BoundingBoxNode? = null

    fun insert(aabb: MutableBoundingBox) {
        val leaf = BoundingBoxNode(aabb)

        // 3 edge cases
        if (root == null) {
            root = leaf
            return
        }
        if (root!!.leftChild == null) {
            root!!.leftChild = leaf
            root!!.recalculateSize()
            return
        }
        if (root!!.rightChild == null) {
            root!!.rightChild = leaf
            root!!.recalculateSize()
            return
        }

        // Primary case
        val nodePath = mutableListOf<BoundingBoxNode>()
        var currentNode = root!!
        nodePath.add(currentNode)
        while (!currentNode.isLeaf()) {
            val leftAreaDiff = currentNode.leftChild!!.computeAreaDiffIfContaining(leaf)
            val rightAreaDiff = currentNode.rightChild!!.computeAreaDiffIfContaining(leaf)

            currentNode = if (leftAreaDiff < rightAreaDiff) {
                currentNode.leftChild!!
            } else {
                currentNode.rightChild!!
            }
            nodePath.add(currentNode)
        }

        val newBranch = BoundingBoxNode(currentNode, leaf)
        val parentNode = nodePath[nodePath.size - 2]
        if (parentNode.leftChild == currentNode) {
            parentNode.leftChild = newBranch
        } else {
            parentNode.rightChild = newBranch
        }

        nodePath.asReversed().forEach { it.recalculateSize() }
    }

    fun intersects(aabbOther: MutableBoundingBox): Boolean {
        if (root == null) {
            return false
        }

        val stack = Stack<BoundingBoxNode>()
        stack.push(root)
        while (stack.isNotEmpty()) {
            val next = stack.pop()
            if (next.intersects(aabbOther)) {
                if (next.isLeaf()) {
                    return true
                } else {
                    next.leftChild?.let { stack.push(it) }
                    next.rightChild?.let { stack.push(it) }
                }
            }
        }
        return false
    }

    override fun deserializeNBT(nbt: NBTTagList) {
        for (i in 0..nbt.size step 6) {
            insert(
                MutableBoundingBox(
                    nbt.getInt(i + 0),
                    nbt.getInt(i + 1),
                    nbt.getInt(i + 2),
                    nbt.getInt(i + 3),
                    nbt.getInt(i + 4),
                    nbt.getInt(i + 5)
                )
            )
        }
    }

    override fun serializeNBT(): NBTTagList {
        val toReturn = NBTTagList()
        if (root == null) {
            return toReturn
        }

        val stack = Stack<BoundingBoxNode>()
        stack.push(root!!)
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            if (node.isLeaf()) {
                node.writeToList(toReturn)
            } else {
                stack.push(node.leftChild!!)
                stack.push(node.rightChild!!)
            }
        }

        return toReturn
    }
}