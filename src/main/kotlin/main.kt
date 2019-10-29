val P = 11699.toBigInteger()
val Q = 7219.toBigInteger()
val X = 64513411.toBigInteger()

fun main() {
    val bbsGenerator = BBSGenerator(P, Q, X)
    val sequence = bbsGenerator.generateSequence(length = 20000)

    println(sequence)
}