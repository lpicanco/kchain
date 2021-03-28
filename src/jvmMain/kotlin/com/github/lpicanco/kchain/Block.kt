package com.github.lpicanco.kchain

import java.security.MessageDigest
import java.time.Instant

data class Block<T>(
    val index: Int,
    val timestamp: Instant,
    val data: T?,
    val previousHash: String?,
    val nonce: Int = 0
) {
    val hash = computeHash()

    private fun computeHash(): String {
        return digest.digest(
            (
                index.toString() +
                timestamp.epochSecond.toString() +
                data.hashCode().toString() +
                previousHash.orEmpty() +
                nonce.toString()
            ).encodeToByteArray()
        ).joinToString("") { "%02x".format(it) }
    }

    override fun toString(): String {
        return "Block(index=$index, timestamp=$timestamp, data=$data, previousHash=$previousHash, nonce=$nonce, hash='$hash')"
    }

    companion object {
        fun <T> mine(data: T, previousBlock: Block<T>, difficulty: Int): Block<T> {
            var block = Block(
                index = previousBlock.index + 1,
                timestamp = Instant.now(),
                data = data,
                previousHash = previousBlock.hash
            )

            while(!block.hash.startsWith("0".repeat(difficulty))) {
                block = block.copy(nonce = block.nonce + 1)
            }

            return block
        }

        private val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
    }
}
