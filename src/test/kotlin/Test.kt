import io.kotlintest.matchers.doubles.shouldBeGreaterThan
import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.matchers.numerics.shouldNotBeGreaterThan
import io.kotlintest.specs.AnnotationSpec

class FIPS1402Test : AnnotationSpec() {
    private val bbsGenerator = BBSGenerator(
        p = 11699L,
        q = 7219L,
        x = 645123L
    )
    private val sequence = bbsGenerator.generateSequence(length = 20000)
    /**
     * Regex to match consecutive 0/1 series
     */
    private val regex = "([01])\\1*".toRegex()


    @Test
    fun singleBitsTest() {
        val numberOf1Bits = sequence.count { it == '1' }

        numberOf1Bits shouldBeInRange (9725 exclusiveRangeTo 10275)
    }

    @Test
    fun seriesTest() {
        val consecutiveBitsSeries = regex.findAll(sequence).map { matchResult ->
            matchResult.value
        }

        val seriesOf0sLengthsCount = consecutiveBitsSeries
            .filter { series -> series.contains('0') }
            .groupingBy { series -> series.length }.eachCount()
            .toSortedMap()

        val seriesOf1sLengthsCount = consecutiveBitsSeries
            .filter { series -> series.contains('1') }
            .groupingBy { series -> series.length }.eachCount()
            .toSortedMap()

        // TODO
    }

    @Test
    fun longSeriesTest() {
        val consecutiveBitsSeries = regex.findAll(sequence).map { matchResult ->
            matchResult.value
        }

        val maxSeriesLength = consecutiveBitsSeries.maxBy { series -> series.length }?.length ?: 0

        maxSeriesLength shouldNotBeGreaterThan 25
    }

    @Test
    fun pokerTest() {
        val segments = sequence.chunked(size = 4)
        val segmentsCount = segments.groupingBy { it.toInt(radix = 2) }.eachCount().toSortedMap()

        val sum = segmentsCount.toList().sumBy { (_, count) ->
            count.squared()
        }
        val x = (16.0 / 5000.0) * sum - 5000.0

        x shouldBeGreaterThan 2.16
        x shouldBeLessThan 46.17
    }
}

infix fun Int.exclusiveRangeTo(other: Int): IntRange = IntRange(this + 1, other - 1)

fun Int.squared() = this * this