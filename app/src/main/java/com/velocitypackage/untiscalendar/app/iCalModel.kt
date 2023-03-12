package com.velocitypackage.untiscalendar.app

import net.fortuna.ical4j.model.*
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.Description
import net.fortuna.ical4j.model.property.DtEnd
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Version
import org.bytedream.untis4j.UntisUtils
import org.bytedream.untis4j.responseObjects.Timetable
import java.time.ZoneId
import java.time.ZoneOffset


fun Timetable.getCalender() : Calendar
{
    val calendar = Calendar()
    calendar.properties.add(ProdId("-//VelocityPackage//UntisCalender 1.0//EN"))
    calendar.properties.add(Version.VERSION_2_0)
    calendar.properties.add(CalScale.GREGORIAN)
    for (lesson in this)
    {
        if (lesson.code == UntisUtils.LessonCode.CANCELLED) continue
        var name = Config.defaultSummary
        try
        {
            name = lesson.subjects.longNames[0]
        } catch (_ : Exception) { }
        val event = VEvent(
            DateTime(
                lesson.startTime.atDate(lesson.date).toString()
            ),
            DateTime(
                lesson.endTime.atDate(lesson.date).toString()
            ),
            name
        )
        try
        {
            event.properties.add(Description("${Config.defaultRoomAlias}: ${lesson.rooms[0].longName} \n${Config.defaultTeacherAlias}: ${lesson.teachers[0].fullName}"))
        } catch (_ : Exception) { }
        calendar.components.add(
            event
        )
    }
    return calendar
}