package com.example.aquaminder.core.domain.use_case

import com.example.aquaminder.core.utils.SoundManager
import javax.inject.Inject

class PlaySoundUseCase @Inject constructor(
    private val soundManager: SoundManager
) {
    fun invoke(key: String) {
        soundManager.playSound(key)
    }
}