package cryptography

import java.awt.image.BufferedImage

fun Byte.bits(): Sequence<Int> {
    val value = this.toInt()

    return sequence {
        for (i in 7 downTo 0)
            yield((value shr i) and 1)
    }
}

fun Sequence<Int>.toImage(width: Int, height: Int, type: Int): BufferedImage {
    val image = BufferedImage(width, height, type)
    val iterator = this.iterator()

    outer@ for (j in 0 until height) {
        for (i in 0 until width) {
            if (iterator.hasNext())
                image.setRGB(i, j, iterator.next())
            else
                break@outer
        }
    }

    return image
}

fun <T> Sequence<T>.repeat(): Sequence<T> = sequence {
    while (true) {
        yieldAll(this@repeat)
    }
}

fun BufferedImage.asSequence(): Sequence<Int> {
    return sequence {
        for (j in 0 until this@asSequence.height)
            for (i in 0 until this@asSequence.width)
                yield(this@asSequence.getRGB(i, j))
    }
}

fun BufferedImage.hide(message: String, password: String): BufferedImage {
    val bytes = message.toByteArray()

    if (bytes.size * 8 > this.width * this.height - 3)
        throw IndexOutOfBoundsException("The input image is not large enough to hold this message.")

    val encoded = bytes.asSequence()
        .zip(password.toByteArray().asSequence().repeat())
        .map { (it.first.toInt() xor it.second.toInt()).toByte() }
        .toList()

    val bits = (encoded + listOf(0, 0, 3))
        .asSequence()
        .flatMap { it.bits() }
        .iterator()

    val output = this.asSequence()
        .map { pixel ->
            if (bits.hasNext()) {
                val bit = bits.next()
                if (bit == 0) pixel and 1.inv() else pixel or bit
            } else pixel
        }
        .toImage(this.width, this.height, this.type)

    return output
}

fun BufferedImage.show(password: String): String {
    val bytes = ByteArray(this.width * this.height / 8) { 0 }
    var byteIndex = 0
    val iterator = password.toByteArray()
        .asSequence()
        .repeat()
        .iterator()

    for ((i, pixel) in this.asSequence().withIndex()) {
        byteIndex = i / 8
        val bitIndex = 7 - i % 8
        bytes[byteIndex] = (bytes[byteIndex] + ((pixel and 1) shl bitIndex)).toByte()

        if (bitIndex == 0 && byteIndex > 1 && bytes.slice((byteIndex - 2)..byteIndex) == listOf<Byte>(0, 0, 3)) {
            break
        }
    }

    for (i in 0..byteIndex - 2)
        bytes[i] = (bytes[i].toInt() xor iterator.next().toInt()).toByte()

    return String(bytes, 0, byteIndex - 2)
}