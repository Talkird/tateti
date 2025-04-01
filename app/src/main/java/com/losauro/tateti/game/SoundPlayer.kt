package com.losauro.tateti.game

import android.content.Context
import android.media.MediaPlayer

object SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    private fun playSound(context: Context, soundResId: Int) {
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
    }

    fun playVictorySound(context: Context) {
        playSound(context, com.losauro.tateti.R.raw.victory)
    }

    fun playDefeatSound(context: Context) {
        playSound(context, com.losauro.tateti.R.raw.defeat)
    }

    fun playDrawSound(context: Context) {
        playSound(context, com.losauro.tateti.R.raw.bubble)
    }

}
