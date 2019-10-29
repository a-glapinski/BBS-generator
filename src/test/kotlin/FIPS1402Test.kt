import io.kotlintest.matchers.doubles.shouldBeGreaterThan
import io.kotlintest.matchers.doubles.shouldBeLessThan
import io.kotlintest.matchers.numerics.shouldBeGreaterThan
import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.matchers.numerics.shouldBeLessThan
import io.kotlintest.matchers.numerics.shouldNotBeGreaterThan
import io.kotlintest.specs.AnnotationSpec

class FIPS1402Test : AnnotationSpec() {
    private val bbsGenerator = BBSGenerator(
        p = 11699L,
        q = 7219L,
        x = 6451L
    )
    private val sequence = bbsGenerator.generateSequence(length = 20000)
    /**
     * Regex to match consecutive 0/1 series
     */
    private val regex = "([01])\\1*".toRegex()

    @Test
    fun singleBitsTest() {
        val numberOf1Bits = sequence.count { it == '1' }

        numberOf1Bits shouldBeGreaterThan 9725
        numberOf1Bits shouldBeLessThan 10275
    }

    @Test
    fun seriesTest() {
        val consecutiveBitsSeries = regex.findAll(sequence).map { matchResult ->
            matchResult.value
        }

        val frequenciesBySeriesOf0sLengths: Map<Int, Int> = consecutiveBitsSeries
            .filter { series -> series.contains('0') }
            .groupingBy { series -> series.length }.eachCount()
            .toSortedMap()

        val frequencyBySeriesOf0sLengthsGreaterThan5 = frequenciesBySeriesOf0sLengths.filter { (length, _) ->
            length > 5
        }.values.sum()

        frequenciesBySeriesOf0sLengths[1]?.shouldBeInRange(2315..2685)
        frequenciesBySeriesOf0sLengths[2]?.shouldBeInRange(1114..1386)
        frequenciesBySeriesOf0sLengths[3]?.shouldBeInRange(527..723)
        frequenciesBySeriesOf0sLengths[4]?.shouldBeInRange(240..384)
        frequenciesBySeriesOf0sLengths[5]?.shouldBeInRange(103..209)
        frequencyBySeriesOf0sLengthsGreaterThan5 shouldBeInRange 103..209
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

        val sum = segmentsCount.values.sumBy { count -> count.squared() }
        val x = (16.0 / 5000.0) * sum - 5000.0

        x shouldBeGreaterThan 2.16
        x shouldBeLessThan 46.17
    }
}

fun Int.squared() = this * this