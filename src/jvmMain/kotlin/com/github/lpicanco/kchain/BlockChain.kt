package com.github.lpicanco.kchain

import java.time.Instant

class BlockChain<T : Any> {
    private val blockList: MutableList<Block<T>> = mutableListOf(generateGenesisBlock())

    val blocks: List<Block<T>>
        get() = blockList

    fun add(data: T): Block<T> {
        val newBlock = Block.mine(data, blockList.last(), DIFFICULTY)
        return blockList.add(newBlock).let { newBlock }
    }

    private fun generateGenesisBlock() = Block<T>(
        index = 0,
        timestamp = Instant.parse("2021-03-28T00:47:41.748Z"),
        data = null,
        previousHash = null
    )

    companion object {
        const val DIFFICULTY = 4
    }

}
