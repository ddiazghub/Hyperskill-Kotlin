package tasklist

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

object Json  {
    val types = Types.newParameterizedType(MutableList::class.java, Task::class.java)
    val adapter: JsonAdapter<MutableList<Task>> = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(LocalDatetimeJsonAdapter)
        .build()
        .adapter(types)

    object LocalDatetimeJsonAdapter {
        @ToJson
        fun toJson(value: LocalDateTime): String = value.toString()

        @FromJson
        fun fromJson(value: String): LocalDateTime = value.toLocalDateTime()
    }
}