package com.example.shopper.viewmodels

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class VoiceViewModel(application: Application) : AndroidViewModel(application) {

    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(application)

    private val intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

    val recognizedLiveData = MutableLiveData<String>()

    val errorLiveData = MutableLiveData<String>()

    var actualSpeech = false

    init {
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.setRecognitionListener(object: RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.i("ShopperFragment", "onReadyForSpeech")
            }

            override fun onRmsChanged(rmsdB: Float) {
                Log.i("ShopperFragment", "onRmsChanged")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                Log.i("ShopperFragment", "OnBufferReceived")
            }

            override fun onPartialResults(partialResults: Bundle?) {
                Log.i("ShopperFragment", "OnPartialResults")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.i("ShopperFragment", "OnEvent")
            }

            override fun onBeginningOfSpeech() {
                Log.i("ShopperFragment", "OnBeginningOfSpeech")
                actualSpeech = true
            }

            override fun onEndOfSpeech() {
                Log.i("ShopperFragment", "OnEndOfSpeech")
            }

            override fun onError(error: Int) {
                Log.i("ShopperFragment", "onError")
                errorLiveData.value = "Could not hear you. Please try again"
            }

            override fun onResults(results: Bundle?) {
                Log.i("ShopperFragment", "onResults")
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    recognizedLiveData.value = matches[0]
                    Log.i("ShopperFragment", matches[0])
                }
            }

        })
    }

    fun startRecognition() {
        speechRecognizer.startListening(intent)

    }

    fun stopRecognition() {
        speechRecognizer.stopListening()
    }

}