package com.seunome.bedrocklauncher.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.seunome.bedrocklauncher.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    companion object {
        const val MINECRAFT_PACKAGE = "com.mojang.minecraftpe"
        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=$MINECRAFT_PACKAGE"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statusLabel = view.findViewById<TextView>(R.id.status_label)
        val playButton = view.findViewById<Button>(R.id.btn_play)
        val buyButton = view.findViewById<Button>(R.id.btn_buy)

        val installed = isMinecraftInstalled()

        statusLabel.text = if (installed)
            "✅ Minecraft Bedrock encontrado no aparelho."
        else
            "❌ Minecraft Bedrock não encontrado. Compre para jogar."

        playButton.visibility = if (installed) View.VISIBLE else View.GONE
        buyButton.visibility = if (installed) View.GONE else View.VISIBLE

        playButton.setOnClickListener {
            val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(MINECRAFT_PACKAGE)
            if (launchIntent != null) startActivity(launchIntent)
        }

        buyButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL)))
        }
    }

    private fun isMinecraftInstalled(): Boolean {
        return try {
            requireContext().packageManager.getPackageInfo(MINECRAFT_PACKAGE, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
