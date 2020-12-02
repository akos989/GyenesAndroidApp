package hu.gyenes.paintball.app.extensions

import java.util.*

fun ClosedRange<Calendar>.intersect(closedRange: ClosedRange<Calendar>): Boolean {
    val firstFrom = this.start
    val firstTo = this.endInclusive
    val secondFrom = closedRange.start
    val secondTo = closedRange.endInclusive

    return (firstFrom <= secondTo && firstTo >= secondFrom)
}