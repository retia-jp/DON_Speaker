package com.example.retia.don_speaker;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import java.util.Locale;

//"https://techbooster.org/android/application/550/"から引用
public class DON_Speaker extends AppCompatActivity
        implements View.OnClickListener, TextToSpeech.OnInitListener {

    final private Float SPEECH_SLOW = 0.5f;
    final private Float SPEECH_NORMAL = 1.0f;
    final private Float SPEECH_FAST = 1.5f;
    final private Float PITCH_LOW = 0.5f;
    final private Float PITCH_NORMAL = 1.0f;
    final private Float PITCH_HIGH = 1.5f;
    private TextToSpeech    tts;
    private Button buttonSpeech;
    private Button buttonStopSpeech;
    private Button buttonSlow;
    private Button buttonNormal;
    private Button buttonFast;
    private Button buttonLowPitch;
    private Button buttonNormalPitch;
    private Button buttonHighPitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don__speaker);

        // ボタンのClickListenerの登録
        buttonSpeech = (Button)findViewById(R.id.button);
        buttonStopSpeech = (Button)findViewById(R.id.button2);
        buttonSpeech.setOnClickListener(this);
        buttonStopSpeech.setOnClickListener(this);

        // TextToSpeechオブジェクトの生成
        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != tts) {
            // TextToSpeechのリソースを解放する
            tts.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            Locale locale = Locale.ENGLISH;
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(locale);
            } else {
                Log.d("", "Error SetLocale");
            }
        } else {
            Log.d("", "Error Init");
        }
    }

    private void speechText() {
        String string = ((EditText)findViewById(R.id.EditText)).getText().toString();
        if (0 < string.length()) {

            tts.speak(string, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void stopSpeech() {
        if (tts.isSpeaking()) {
         // 読み上げ中なら止める
         tts.stop();
         }

        // 読み上げ開始
    }

    @Override
    public void onClick(View v) {
        if (buttonSpeech == v) {
            speechText();
        } else if (buttonSlow == v) {
            // 再生速度の設定
            tts.setSpeechRate(SPEECH_SLOW);
        } else if (buttonNormal == v) {
            // 再生速度の設定
            tts.setSpeechRate(SPEECH_NORMAL);
        } else if (buttonFast == v) {
            // 再生速度の設定
            tts.setSpeechRate(SPEECH_FAST);
        } else if (buttonLowPitch == v) {
            // 再生ピッチの設定
            tts.setPitch(PITCH_LOW);
        } else if (buttonNormalPitch == v) {
            // 再生ピッチの設定
            tts.setPitch(PITCH_NORMAL);
        } else if (buttonHighPitch == v) {
            // 再生ピッチの設定
            tts.setPitch(PITCH_HIGH);
        } else if (buttonStopSpeech == v) {
            stopSpeech();
        }
    }
}
