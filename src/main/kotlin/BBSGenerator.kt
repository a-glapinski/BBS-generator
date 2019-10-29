import java.math.BigInteger

class BBSGenerator(
    p: BigInteger,
    q: BigInteger,
    /** Seed */
    private val x: BigInteger
) {
    private val n = p * q

    init {
        require(p.isPrime() && q.isPrime()) { "P and Q have to be prime numbers." }
        require(p % 4.toBigInteger() == 3.toBigInteger() && q % 4.toBigInteger() == 3.toBigInteger()) {
            "Both first and second prime has to be congruent to 3 mod 4." 
        }
        require(x.isCoprimeTo(n)) { "X must be coprime to N = P * Q." }
    }

    fun generateSequence(length: Int) = buildString(length) {
        var generatedValue = x.squared() % n

        for (i in 0 until length) {
            append(generatedValue.lsb())
            generatedValue = generatedValue.squared() % n
        }
    }

    private fun BigInteger.lsb() = this.toString(2).last()
}

private fun BigInteger.isPrime(): Boolean = this.isProbablePrime(10)

private tailrec fun greatestCommonDivisor(a: BigInteger, b: BigInteger): BigInteger {
    if (b == BigInteger.ZERO) return a
    return greatestCommonDivisor(b, a % b)
}

private fun BigInteger.isCoprimeTo(other: BigInteger): Boolean = greatestCommonDivisor(this, other) == BigInteger.ONE

private fun BigInteger.squared() = this * this