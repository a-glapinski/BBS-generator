const val P = 11L
const val Q = 19L
const val X = 13L

fun main() {
    val bbsGenerator = BBSGenerator(P, Q, X)
    val sequence = bbsGenerator.generateSequence(length = 20000)

    println(sequence)
}