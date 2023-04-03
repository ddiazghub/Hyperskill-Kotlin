package tasklist

sealed class TasklistException(message: String? = null) : RuntimeException(message) {
    object InvalidAction : TasklistException("The input action is invalid")
    object Empty : TasklistException("No tasks have been input")
    object Blank : TasklistException("The task is blank")
    object InvalidPriority : TasklistException()
}
