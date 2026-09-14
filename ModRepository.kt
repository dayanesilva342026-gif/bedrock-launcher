package com.seunome.bedrocklauncher.data

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Busca addons/mods de Bedrock usando a API OFICIAL do CurseForge.
 * É preciso gerar uma chave gratuita em https://console.curseforge.com/
 * ("3rd party API consumer") e colar abaixo. Sem essa chave a busca não
 * funciona — é exigência do próprio CurseForge, não dá pra contornar.
 */
object ModRepository {

    // 👉 Cole aqui sua chave gratuita do CurseForge (console.curseforge.com)
    private const val API_KEY = "COLOQUE_SUA_API_KEY_AQUI"

    // classId 4471 = "Bedrock Add-Ons" na CurseForge; gameId 432 = Minecraft
    private const val BASE_URL =
        "https://api.curseforge.com/v1/mods/search?gameId=432&classId=4471&pageSize=25&searchFilter="

    private val client = OkHttpClient()

    fun search(query: String): List<ModItem> {
        val url = BASE_URL + URLEncoder.encode(query, "UTF-8")
        val request = Request.Builder()
            .url(url)
            .addHeader("x-api-key", API_KEY)
            .addHeader("Accept", "application/json")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Erro na busca (código ${response.code})")
            val body = response.body?.string() ?: return emptyList()
            val json = JSONObject(body)
            val data = json.getJSONArray("data")

            val results = mutableListOf<ModItem>()
            for (i in 0 until data.length()) {
                val mod = data.getJSONObject(i)
                val latestFiles = mod.getJSONArray("latestFiles")
                if (latestFiles.length() == 0) continue

                val file = latestFiles.getJSONObject(0)
                val downloadUrl = file.optString("downloadUrl", "")
                if (downloadUrl.isEmpty()) continue

                results.add(
                    ModItem(
                        id = mod.getInt("id").toString(),
                        name = mod.optString("name", "Sem nome"),
                        summary = mod.optString("summary", ""),
                        downloadUrl = downloadUrl,
                        fileName = file.optString("fileName", "addon.mcpack")
                    )
                )
            }
            return results
        }
    }
}
