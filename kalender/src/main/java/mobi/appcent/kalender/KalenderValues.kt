package mobi.appcent.kalender

data class KalenderValues(
    val month: String? = null,
    val year: String? = null,
    val days: ArrayList<KalenderDateModel> = arrayListOf()
)

data class KalenderDateModel(
    val day: Int,
    val isActive: Boolean = false
)