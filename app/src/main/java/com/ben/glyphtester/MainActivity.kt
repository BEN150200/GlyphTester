package com.ben.glyphtester

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ben.glyphtester.ui.theme.GlyphTesterTheme
import com.nothing.ketchum.Common
import com.nothing.ketchum.GlyphException
import com.nothing.ketchum.GlyphFrame
import com.nothing.ketchum.GlyphManager


class MainActivity : ComponentActivity() {
    private lateinit var mGM: GlyphManager
    private lateinit var mCallback: GlyphManager.Callback
    private lateinit var viewModel: GlyphViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        mGM = GlyphManager.getInstance(applicationContext)
        mGM.init(mCallback)
        viewModel = GlyphViewModel(mGM)


        setContent {
            GlyphTesterTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        SmallTopAppBar(
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text("Glyph Tester")
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainContent(viewModel, it)
                }
            }
        }
    }

    override fun onDestroy() {
        viewModel.turnAllOff()
        try {
            mGM.closeSession()
        } catch (e: GlyphException) {
            e.message?.let { Log.e(TAG, it) }
        }
        mGM.unInit()
        super.onDestroy()
    }

    override fun onStop() {
        if (!viewModel.keepOnSecondPlane.value) {
            viewModel.turnAllOff()
        }
        super.onStop()
    }

    private fun init() {
        mCallback = object : GlyphManager.Callback {
            override fun onServiceConnected(componentName: ComponentName) {
                if (Common.is20111()) mGM.register(Common.DEVICE_20111)
                if (Common.is22111()) mGM.register(Common.DEVICE_22111)
                try {
                    mGM.openSession()
                } catch (e: GlyphException) {
                    e.message?.let { Log.e(TAG, it) }
                }
            }

            override fun onServiceDisconnected(componentName: ComponentName) {
                mGM.closeSession()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainContent(viewModel: GlyphViewModel, paddingValues: PaddingValues = PaddingValues(8.dp, 8.dp)) {
    val keepOn by viewModel.keepOnSecondPlane.collectAsState()
    Surface(modifier = Modifier
        .padding(top = paddingValues.calculateTopPadding()),
        color = MaterialTheme.colorScheme.background) {
        Column() {
            Button(onClick = {
               viewModel.turnAllOn()
                Log.d("Button", "Turn on")
            }) {
                Text(text = "Turn all ON")
            }

            Button(onClick = {
                viewModel.turnOn(6)
                Log.d("Button", "Turn on")
            }) {
                Text(text = "Turn dot ON")
            }
            Button(onClick = {
                viewModel.turnAllOff()
                Log.d("Button", "Turn off")
            }) {
                Text(text = "Turn All Off")
            }
            Row (verticalAlignment = Alignment.CenterVertically){
                Checkbox(checked = keepOn, onCheckedChange = {
                    viewModel.toggleKeepOn()
                })
                Text(text = "Keep light's state on second plane")
            }
        }

    }
}