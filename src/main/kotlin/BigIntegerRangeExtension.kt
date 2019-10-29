import java.math.BigInteger

class BigIntegerRange(
    override val start: BigInteger,
    override val endInclusive: BigInteger
) : ClosedRange<BigInteger>, Iterable<BigInteger> {
    override operator fun iterator(): Iterator<BigInteger> = BigIntegerRangeIterator(this)
}

class BigIntegerRangeIterator(
    private val range: ClosedRange<BigInteger>
) : Iterator<BigInteger> {
    private var current = range.start

    override fun hasNext(): Boolean = current <= range.endInclusive

    override fun next(): BigInteger {
        if (!hasNext()) throw NoSuchElementException()
        return current++
    }
}

operator fun BigInteger.rangeTo(other: BigInteger) = BigIntegerRange(this, other)