package com.seunome.bedrocklauncher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.seunome.bedrocklauncher.R
import com.seunome.bedrocklauncher.data.ModItem

class ModAdapter(
    private val items: List<ModItem>,
    private val onDownloadClick: (ModItem) -> Unit
) : RecyclerView.Adapter<ModAdapter.ModViewHolder>() {

    class ModViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.mod_name)
        val summary: TextView = view.findViewById(R.id.mod_summary)
        val downloadButton: Button = view.findViewById(R.id.btn_download)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mod, parent, false)
        return ModViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModViewHolder, position: Int) {
        val mod = items[position]
        holder.name.text = mod.name
        holder.summary.text = mod.summary
        holder.downloadButton.setOnClickListener { onDownloadClick(mod) }
    }

    override fun getItemCount() = items.size
}
