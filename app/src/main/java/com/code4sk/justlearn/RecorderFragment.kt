package com.code4sk.justlearn

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_recorder.*
import java.io.IOException
import java.lang.IllegalStateException

class RecorderFragment: Fragment() {
    var record: MediaRecorder? = null
    var output = ""
    var state = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun recordAudio(view: View){
        if (ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this.activity!!, permissions,0)
            Log.d(logTag, "perm wrong")
        }
        else{
            Log.d(logTag, "perm right")
            Toast.makeText(this.context, "Recording started!", Toast.LENGTH_SHORT).show()
            record = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(output)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)

                try {
                    prepare()
                    start()
                } catch (e: IOException) {
                    Log.e(logTag, "prepare() failed")
                } catch (e: IllegalStateException) {
                    Log.e(logTag, "illegal")
                }
                Log.d(logTag, "start")
                state = true
            }
        }
    }
    private fun stopAudio(view: View){
        if(state){
            record?.stop()
            record?.release()
            state = false
            Toast.makeText(this.context, "Recording stopped!", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this.context, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordButton.setOnClickListener { recordAudio(it) }
        stopButton.setOnClickListener { stopAudio(it) }
        output = Environment.getExternalStorageDirectory().absolutePath + "/Recordings/new.mp3"
        Log.d(logTag, output)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recorder, container, false)
    }
}
