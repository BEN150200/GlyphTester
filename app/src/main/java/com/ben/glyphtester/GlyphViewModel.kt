package com.ben.glyphtester

import androidx.lifecycle.ViewModel
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GlyphKeyFrame (
    val channels: Array<Int>,
    val time: Int,
    val intensity: Int,
)

enum class GlyphLightEnum { A1, B1, C1, C2,C3,C4,E1,D1_1,
    D1_2,
    D1_3,
    D1_4,
    D1_5,
    D1_6,
    D1_7,
    D1_8, }


object GlyphLight {
    var A1 = 0
    var B1 = 1
    var C1 = 2
    var C2 = 3
    var C3 = 4
    var C4 = 5
    var E1 = 6
    var D1_1 = 7
    var D1_2 = 8
    var D1_3 = 9
    var D1_4 = 10
    var D1_5 = 11
    var D1_6 = 12
    var D1_7 = 13
    var D1_8 = 14
}


class GlyphViewModel(private val mGM: GlyphManager) : ViewModel() {
    private val _glyphManger = MutableStateFlow(mGM)

    val glyphManager: StateFlow<GlyphManager> = _glyphManger.asStateFlow()
    private val _keepOnSecondPlane = MutableStateFlow(false)
    val keepOnSecondPlane: StateFlow<Boolean> = _keepOnSecondPlane.asStateFlow()


    fun toggleKeepOn() {
       _keepOnSecondPlane.value = !_keepOnSecondPlane.value
    }

    fun turnAllOff() {
        val builder = _glyphManger.value.glyphFrameBuilder
        val frame = builder.build();
        glyphManager.value.toggle(frame)
    }

    fun turnAllOn() {
        val builder = _glyphManger.value.glyphFrameBuilder
        val frame = builder.buildChannelA().buildChannelB().buildChannelC().buildChannelD().buildChannelE().build();
        glyphManager.value.toggle(frame)
    }

    fun turnOn(channel : Int) {
        val builder = _glyphManger.value.glyphFrameBuilder
        val frame = builder.buildChannel(channel).build();
        glyphManager.value.toggle(frame)
    }


}