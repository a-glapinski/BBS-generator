import kotlin.math.sqrt

class BBSGenerator(
    p: Long,
    q: Long,
    /** Seed */
    private val x: Long
) {
    private val n = p * q

    init {
        require(p.isPrime() && q.isPrime()) { "P and Q have to be prime numbers." }
        require(p % 4 == 3L && q % 4 == 3L) { "Both first and second prime has to be congruent to 3 mod 4." }
        require(x.isCoprimeTo(n)) { "X must be coprime to N = P * Q." }
    }

    fun generateSequence(length: Int) = buildString(length) {
        var generatedValue = x.squared() % n

        for (i in 0 until length) {
            append(generatedValue.lsb())
            generatedValue = generatedValue.squared() % n
        }
    }

    private fun Long.lsb() = this.toString(radix = 2).last()
}

private fun Long.isPrime(): Boolean = (2..sqrt(this.toDouble()).toLong()).none { this % it == 0L }

private tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    if (b == 0L) return a
    return greatestCommonDivisor(b, a % b)
}

private fun Long.isCoprimeTo(other: Long): Boolean = greatestCommonDivisor(this, other) == 1L

private fun Long.squared() = this * this