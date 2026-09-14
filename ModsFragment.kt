package com.seunome.bedrocklauncher.ui

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seunome.bedrocklauncher.R
import com.seunome.bedrocklauncher.data.ModItem
import com.seunome.bedrocklauncher.data.ModRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModsFragment : Fragment(R.layout.fragment_mods) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchInput = view.findViewById<EditText>(R.id.search_input)
        val searchButton = view.findViewById<Button>(R.id.btn_search)
        val loading = view.findViewById<ProgressBar>(R.id.loading_indicator)
        val recyclerView = view.findViewById<RecyclerView>(R.id.mods_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isEmpty()) return@setOnClickListener

            loading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val results = withContext(Dispatchers.IO) { ModRepository.search(query) }
                    recyclerView.adapter = ModAdapter(results) { mod -> downloadMod(mod) }
                    if (results.isEmpty()) {
                        Toast.makeText(requireContext(), "Nenhum mod encontrado.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Erro na busca: ${e.message}", Toast.LENGTH_LONG).show()
                } finally {
                    loading.visibility = View.GONE
                }
            }
        }
    }

    private fun downloadMod(mod: ModItem) {
        val downloadManager = requireContext().getSystemService(DownloadManager::class.java)
        val request = DownloadManager.Request(Uri.parse(mod.downloadUrl))
            .setTitle(mod.name)
            .setDescription("Baixando addon para o Minecraft Bedrock")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mod.fileName)

        downloadManager.enqueue(request)
        Toast.makeText(
            requireContext(),
            "Baixando \"${mod.name}\"... Toque na notificação de download quando terminar para abrir com o Minecraft.",
            Toast.LENGTH_LONG
        ).show()
    }
}
