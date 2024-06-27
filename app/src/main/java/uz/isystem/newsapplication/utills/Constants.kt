package uz.isystem.newsapplication.utills

object Constants {
    private val random = (1..4).random()
    val API_KEY = when (random) {
        1 -> "023ce30d78dd48d8a718d23aecbd0067"
        2 -> "ab1069ef01714a9498436db324d65cd1"
        3 -> "c374e9143a384f7a8416cbd609b5b5b2"
        else -> "49c9ccd4c6b74e1885c32947af94ac25"
    }

    const val BASE_URL = "https://newsapi.org/"
}