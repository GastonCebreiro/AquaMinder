package com.example.aquaminder.core.utils

import android.content.Context
import android.media.SoundPool
import com.example.aquaminder.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val SOUND_SWITCH_ON = "switch_on"
        const val SOUND_SWITCH_OFF = "switch_off"
    }

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<String, Int>()

    fun init() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        soundMap[SOUND_SWITCH_ON] = soundPool?.load(context, R.raw.switch_on, 1) ?: 0
        soundMap[SOUND_SWITCH_OFF] = soundPool?.load(context, R.raw.switch_off, 1) ?: 0
    }

    fun playSound(key: String) {
        val soundId = soundMap[key] ?: return
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
