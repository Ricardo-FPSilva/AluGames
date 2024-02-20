import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Scanner

fun main() {
    val leitura = Scanner(System.`in`)

    println("Digite um codigo de jogo para buscar: ")
    val busca = leitura.nextLine()

    val url = "https://www.cheapshark.com/api/1.0/games?id=$busca"

    val client: HttpClient = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .build()
    val response = client
        .send(request, HttpResponse.BodyHandlers.ofString())

    val json = response.body()
//    println(json)

    var meuJogo: Jogo? = null
    val result = runCatching {
        val gson = Gson()
        val meuInfoJogo = gson.fromJson(json, InfoJogo::class.java)

        meuJogo = Jogo(meuInfoJogo.info.title, meuInfoJogo.info.thumb)
    }

    result.onFailure {
        println("ID de jogo inexistente. Utilize um ID válido")
    }

    result.onSuccess {
        println("Deseja inserir uma descriçao personalizada? S/N")
        val op = leitura.nextLine()
        if (op.equals("s", true)){
            println("Insira sua descrição: ")
            val descricao = leitura.nextLine()
            meuJogo?.descricao = descricao
        } else {
            meuJogo?.descricao = meuJogo?.titulo
        }
    }
    println(meuJogo)

    result.onSuccess {
        println("Busca Finalizada")
    }

//    try {
//        val gson = Gson()
//        val meuInfoJogo = gson.fromJson(json, InfoJogo::class.java)
//
//        val meuJogo = Jogo(meuInfoJogo.info.title, meuInfoJogo.info.thumb)
//        println(meuJogo)
//
//    } catch (e: JsonSyntaxException){
//        println("ID de jogo inexistente. Utilize um ID válido")
//    } catch (e: NullPointerException){
//        println("ID de jogo inexistente. Utilize um ID válido")
//    }


}