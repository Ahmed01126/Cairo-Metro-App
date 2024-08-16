package com.radwan.metroapp.services;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechService implements TextToSpeech.OnInitListener {
    private final TextToSpeech textToSpeech;
    private StringBuilder Speech;
    public TextToSpeechService(Context context) {
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(Locale.US);
            textToSpeech.setSpeechRate(0.6f);
            textToSpeech.setPitch(1.2f);
        }
    }

    public void speak(StringBuilder text) {
        textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
    
    public StringBuilder speakTextSubString(StringBuilder fullText) {
        int startIndex = fullText.indexOf("~");
        fullText.setCharAt(startIndex, ' ');
        int endIndex = fullText.indexOf("~");
        fullText.setCharAt(endIndex, ' ');
        
        // Check if both '~' and '~' are present and in the correct order
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            String substring = fullText.substring(startIndex + 1, endIndex);
            Speech.append(substring);
        } else {
            // Handle cases where the parentheses are not present or in the wrong order
            // textToSpeech.speak("No valid text found between parentheses", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        return fullText;
    }

    public StringBuilder getSpeech() {
        return Speech;
    }
}