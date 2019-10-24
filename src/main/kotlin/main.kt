const val P = 11699L
const val Q = 7219L
const val X = 645123L

fun main() {
    val bbsGenerator = BBSGenerator(P, Q, X)
    val sequence = bbsGenerator.generateSequence(length = 20000)

    println(sequence)
}