package com.example.guitar_music_app.general
//E = Exceptions; V = Value, hodnota pri uspechu
sealed class GeneralResult<out E, out V> {
    data class Value<out V>(val value: V) : GeneralResult<Nothing, V>()
    data class Error<out E>(val error: E) : GeneralResult<E, Nothing>()

    companion object Factory{
        inline fun <V> build(function: () -> V): GeneralResult<Exception, V> =
            try {
                Value(function.invoke())
            } catch (e: java.lang.Exception) {
                Error(e)
            }
    }
}