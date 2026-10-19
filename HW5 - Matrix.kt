package org

import kotlin.time.measureTime
import kotlin.random.Random
import kotlin.time.DurationUnit

class Matrix(val size: Int) {
    private val matrix: Array<DoubleArray> = Array(size) { DoubleArray(size) }

    /**
     * Get the (side) length of the target matrix
     * Inputs: None
     * Outputs:
     *     a double representing the side length of the target matrix
     */
    fun len(): Int {
        return matrix.size
    }

    /**
     * View the value of a certain entry of the target matrix
     * Inputs:
     *     row: an integer representing the row of the entry
     *     col: an integer representing the column of the entry
     * Outputs:
     *     a double representing the value of the entry
     */
    operator fun get(row: Int, col: Int): Double {
        return matrix[row][col]
    }

    /**
     * Set the value of a certain entry of the target matrix
     * Inputs:
     *     row: an integer representing the row of the entry
     *     col: an integer representing the column of the entry
     *     value: a double representing the new value of the entry
     * Outputs: None
     */
    operator fun set(row: Int, col: Int, value: Double) {
        matrix[row][col] = value
    }

    /**
     * Add addMatrix to the target matrix
     * Inputs:
     *     addMatrix: a matrix that the target matrix is added to
     * Outputs:
     *     retMatrix: a matrix that is the sum between the target matrix and addMatrix
     */
    operator fun plus(addMatrix: Matrix): Matrix? {
        val retMatrix = Matrix(size)
        if (addMatrix.len() != matrix.size) {
            return null
        }
        for (r in 0..matrix.size - 1) {
            for (c in 0..matrix[0].size - 1) {
                retMatrix[r, c] = matrix[r][c] + addMatrix[r, c]
            }
        }

        return retMatrix
    }

    /**
     * Subtract the target matrix by minusMatrix
     * Inputs:
     *     minusMatrix: a matrix that the target matrix is subtracted by
     * Outputs:
     *     retMatrix: a matrix that is the difference between the target matrix and minusMatrix
     */
    operator fun minus(minusMatrix: Matrix): Matrix? {
        val retMatrix = Matrix(size)
        if (minusMatrix.len() != matrix.size) {
            return null
        }
        for (r in 0..matrix.size - 1) {
            for (c in 0..matrix[0].size - 1) {
                retMatrix[r, c] = matrix[r][c] - minusMatrix[r, c]
            }
        }

        return retMatrix
    }

    /**
     * Split a matrix into four separate matrices of size n / 2.
     * Inputs: None, this is a method for a matrix
     * Outputs:
     *     A list of matrices containing the four submatrices of the used matrix
     */
    fun split(): List<Matrix> {
        val half = size / 2
        val nwMatrix = Matrix(half)
        val neMatrix = Matrix(half)
        val swMatrix = Matrix(half)
        val seMatrix = Matrix(half)

        for (r in 0..half - 1) {
            for (c in 0..half - 1) {
                nwMatrix[r, c] = matrix[r][c]
                neMatrix[r, c] = matrix[r][c + half]
                swMatrix[r, c] = matrix[r + half][c]
                seMatrix[r, c] = matrix[r + half][c + half]
            }
        }

        return listOf(nwMatrix, neMatrix, swMatrix, seMatrix)
    }

    /**
     * Multiply [this] matrix by [other].
     * @return [this]*[other] if the dimensions are compatible and null otherwise
     */
    fun multiply(other: Matrix): Matrix? {
        if (other.size != size) {
            return null
        }

        val retMatrix = Matrix(size)

        for (r in 0..matrix.size - 1) {
            for (c in 0..matrix.size - 1) {
                var total = 0.0
                for (t in 0..matrix.size - 1) {
                    total = total + matrix[r][t] * other[t, c]
                }
                retMatrix[r, c] = total
            }
        }

        return retMatrix
    }

    /**
     * Multiply [this] matrix by [other].
     * Your code should use Strassen's algorithm
     * @return [this]*[other] if the dimensions are compatible and null otherwise
     */
    fun strassenMultiply(other: Matrix): Matrix? {
        if (other.size != size) {
            return null
        }
        val sz = matrix.size

        if (sz <= 64) {
            return multiply(other)
        }

        val A = split()
        val ANW = A[0]
        val ANE = A[1]
        val ASW = A[2]
        val ASE = A[3]

        val B = other.split()
        val BNW = B[0]
        val BNE = B[1]
        val BSW = B[2]
        val BSE = B[3]

        val m1 = (ANW + ASE)?.strassenMultiply((BNW + BSE) ?: return null) ?: return null
        val m2 = (ASW + ASE)?.strassenMultiply(BNW) ?: return null
        val m3 = ANW.strassenMultiply((BNE - BSE) ?: return null) ?: return null
        val m4 = ASE.strassenMultiply((BSW - BNW) ?: return null) ?: return null
        val m5 = (ANW + ANE)?.strassenMultiply(BSE) ?: return null
        val m6 = (ASE - ANW)?.strassenMultiply((BNW + BNE) ?: return null) ?: return null
        val m7 = (ANE - ASE)?.strassenMultiply((BSE + BSW) ?: return null) ?: return null

        val c11 = (((m1 + m4) ?: return null) - m5)?.plus(m7) ?: return null
        val c12 = (m3 + m5) ?: return null
        val c21 = (m2 + m4) ?: return null
        val c22 = ((((m1 - m2) ?: return null) + m3) ?: return null).plus(m6) ?: return null

        val retMatrix = Matrix(size)
        val half = size / 2
        for (r in 0..half - 1) {
            for (c in 0..half - 1) {
                retMatrix[r, c] = c11[r, c]
                retMatrix[r, c + half] = c12[r, c]
                retMatrix[r + half, c] = c21[r, c]
                retMatrix[r + half, c + half] = c22[r, c]
            }
        }

        return retMatrix
    }
}

fun matrixUnitTests() {
    val m1 = Matrix(2)
    assert(m1.len() == 2)
    m1[0, 0] = 8.0
    m1[0, 1] = 9.0
    m1[1, 0] = 1.0
    m1[1, 1] = 3.0

    val m2 = Matrix(2)
    m2[0, 0] = 7.0
    m2[0, 1] = 9.0
    m2[1, 0] = 0.0
    m2[1, 1] = -2.0

    val m3 = m1 + m2
    val checkm3 = Matrix(2)
    checkm3[0, 0] = 15.0
    checkm3[0, 1] = 18.0
    checkm3[1, 0] = 1.0
    checkm3[1, 1] = 1.0

    assert(m3 != null)
    for (r in 0 until 2) {
        for (c in 0 until 2) {
            assert(m3!![r, c] == checkm3[r, c])
        }
    }

    val m4 = m1 - m2
    val checkm4 = Matrix(2)
    checkm4[0, 0] = 1.0
    checkm4[0, 1] = 0.0
    checkm4[1, 0] = 1.0
    checkm4[1, 1] = 5.0

    assert(m4 != null)
    for (r in 0 until 2) {
        for (c in 0 until 2) {
            assert(m4!![r, c] == checkm4[r, c])
        }
    }

    val m5 = m1.multiply(m2)
    val checkm5 = Matrix(2)
    checkm5[0, 0] = 56.0
    checkm5[0, 1] = 54.0
    checkm5[1, 0] = 7.0
    checkm5[1, 1] = 3.0

    assert(m5 != null)
    for (r in 0 until 2) {
        for (c in 0 until 2) {
            assert(m5!![r, c] == checkm5[r, c])
        }
    }

    val m6 = m1.strassenMultiply(m2)
    assert(m6 != null)
    for (r in 0 until 2) {
        for (c in 0 until 2) {
            assert(m6!![r, c] == checkm5[r, c])
        }
    }

    val a4 = Matrix(4)
    val b4 = Matrix(4)
    for (r in 0 until 4) {
        for (c in 0 until 4) {
            a4[r, c] = (r + c + 1).toDouble()
            b4[r, c] = (r * c + 2).toDouble()
        }
    }
    val mult4 = a4.multiply(b4)
    val strassen4 = a4.strassenMultiply(b4)
    assert(mult4 != null)
    assert(strassen4 != null)
    for (r in 0 until 4) {
        for (c in 0 until 4) {
            assert(strassen4!![r, c] == mult4!![r, c])
        }
    }

    val a8 = Matrix(8)
    val b8 = Matrix(8)
    for (r in 0 until 8) {
        for (c in 0 until 8) {
            a8[r, c] = (r - c).toDouble()
            b8[r, c] = (r + c + 1).toDouble()
        }
    }
    val mult8 = a8.multiply(b8)
    val strassen8 = a8.strassenMultiply(b8)
    assert(mult8 != null)
    assert(strassen8 != null)
    for (r in 0 until 8) {
        for (c in 0 until 8) {
            assert(strassen8!![r, c] == mult8!![r, c])
        }
    }

    val m7 = Matrix(3)
    assert(m1 + m7 == null)
    assert(m1 - m7 == null)
    assert(m1.multiply(m7) == null)
    assert(m1.strassenMultiply(m7) == null)

    println("Matrix class passes all unit tests")
}

fun matrixTimings() {
    val sizes = listOf(2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048)
    val mRunTimes = mutableListOf<Double>()
    val sRunTimes = mutableListOf<Double>()

    for (size in sizes) {
        val A = Matrix(size)
        val B = Matrix(size)

        for (r in 0 until size) {
            for (c in 0 until size) {
                A[r, c] = Random.nextDouble(-10.0, 10.0)
                B[r, c] = Random.nextDouble(-10.0, 10.0)
            }
        }

        val runTimeM = measureTime {
            A.multiply(B)
        }
        mRunTimes.add(runTimeM.toDouble(DurationUnit.SECONDS))
        val runTimeS = measureTime {
            A.strassenMultiply(B)
        }
        sRunTimes.add(runTimeS.toDouble(DurationUnit.SECONDS))
    }
    println("Multiply run times are: $mRunTimes")
    println("Strassen run times are: $sRunTimes")
}

fun main() {
    matrixUnitTests()
    matrixTimings()
}