package com.illidant.easykanzicapstone.util

import android.graphics.Path
import java.util.regex.Pattern

object SVGFileHelper {

    private const val CHARACTER_M = 'm'
    private const val CHARACTER_L = 'l'
    private const val CHARACTER_V = 'v'
    private const val CHARACTER_H = 'h'
    private const val CHARACTER_C = 'c'
    private const val CHARACTER_S = 's'
    private const val CHARACTER_Z = 'z'

    private val svgInstructionPattern = Pattern.compile("([a-zA-Z])([^a-zA-Z]+)")
    private val svgCoordinatesPattern = Pattern.compile("-?\\d*\\.?\\d*")
    private val svgPathPattern = Pattern.compile("<path .*(?<= )d=\"([^\"]+)\".*/>")

    fun extractPathData(input: String) = svgPathPattern.collect(input, 1)

    fun buildPathData(pathData: String): Path {
        val path = Path()
        try {
            val matcher = svgInstructionPattern.matcher(pathData)

            var lastX = 0f
            var lastY = 0f
            var lastX1 = 0f
            var lastY1 = 0f
            var subPathStartX = 0f
            var subPathStartY = 0f
            var curve = false

            while (matcher.find()) {
                val command = matcher.group(1)[0]
                val coordinatesStr = matcher.group(2)

                when (command.toLowerCase()) {
                    CHARACTER_M -> coordinatesStr.split(',').let {
                        val x = it[0].toFloat()
                        val y = it[1].toFloat()
                        if (command.isUpperCase()) {
                            subPathStartX = x
                            subPathStartY = y
                            path.moveTo(x, y)
                            lastX = x
                            lastY = y
                        } else {
                            subPathStartX += x
                            subPathStartY += y
                            path.rMoveTo(x, y)
                            lastX += x
                            lastY += y
                        }
                    }
                    CHARACTER_L -> coordinatesStr.split(',').let {
                        val x = it[0].toFloat()
                        val y = it[1].toFloat()
                        if (command.isUpperCase()) {
                            path.lineTo(x, y)
                            lastX = x
                            lastY = y
                        } else {
                            path.rLineTo(x, y)
                            lastX += x
                            lastY += y
                        }
                    }
                    CHARACTER_V -> coordinatesStr.forEachCoordinatesGroup(1) { coordinates ->
                        val y = coordinates.first()
                        if (command.isUpperCase()) {
                            path.lineTo(lastX, y)
                            lastY = y
                        } else {
                            path.rLineTo(0f, y)
                            lastY += y
                        }
                    }
                    CHARACTER_H -> coordinatesStr.forEachCoordinatesGroup(1) { coordinates ->
                        val x = coordinates.first()
                        if (command.isUpperCase()) {
                            path.lineTo(x, lastY)
                            lastX = x
                        } else {
                            path.rLineTo(x, 0f)
                            lastX += x
                        }
                    }
                    CHARACTER_C -> {
                        curve = true
                        coordinatesStr.forEachCoordinatesGroup(6) { coordinates ->
                            var x1 = coordinates[0]
                            var y1 = coordinates[1]

                            var x2 = coordinates[2]
                            var y2 = coordinates[3]

                            var x = coordinates[4]
                            var y = coordinates[5]

                            if (command.isLowerCase()) {
                                x1 += lastX
                                x2 += lastX
                                x += lastX
                                y1 += lastY
                                y2 += lastY
                                y += lastY
                            }

                            path.cubicTo(x1, y1, x2, y2, x, y)

                            lastX1 = x2
                            lastY1 = y2
                            lastX = x
                            lastY = y
                        }
                    }
                    CHARACTER_S -> {
                        curve = true
                        coordinatesStr.forEachCoordinatesGroup(4) { coordinates ->
                            var x2 = coordinates[0]
                            var y2 = coordinates[1]

                            var x = coordinates[2]
                            var y = coordinates[3]

                            if (command.isLowerCase()) {
                                x2 += lastX
                                x += lastX
                                y2 += lastY
                                y += lastY
                            }

                            val x1 = 2 * lastX - lastX1
                            val y1 = 2 * lastY - lastY1

                            path.cubicTo(x1, y1, x2, y2, x, y)
                            lastX1 = x2
                            lastY1 = y2
                            lastX = x
                            lastY = y
                        }
                    }
                    CHARACTER_Z -> {
                        path.close()
                        path.moveTo(subPathStartX, subPathStartY)
                        lastX = subPathStartX
                        lastY = subPathStartY
                        lastX1 = subPathStartX
                        lastY1 = subPathStartY
                        curve = true
                    }
                    else -> throw IllegalArgumentException("Unknown command $command for input $pathData")
                }
                if (!curve) {
                    lastX1 = lastX
                    lastY1 = lastY
                }
            }
            return path
        } catch (e: Exception) {
            throw IllegalArgumentException("Incorrect input $pathData", e)
        }
    }

    private fun Pattern.collect(
        input: String,
        groupIndex: Int = 0
    ): List<String> {
        val matcher = matcher(input)
        val list = ArrayList<String>()

        while (matcher.find()) {
            val value = matcher.group(groupIndex)
            if (value.isNotBlank())
                list.add(value)
        }

        return list
    }

    private inline fun String.forEachCoordinatesGroup(
        groupSize: Int,
        action: (List<Float>) -> Unit
    ) {
        svgCoordinatesPattern.collect(this).map { it.toFloat() }
            .withIndex().groupBy({ it.index / groupSize }, { it.value })
            .forEach { (_, coordinates) -> action.invoke(coordinates) }
    }
}
