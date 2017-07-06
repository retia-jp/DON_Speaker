package com.example.retia.don_speaker;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.*;

import java.util.Locale;

public class DON_Speaker extends AppCompatActivity
        implements View.OnClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech    tts;
    private Button buttonSpeech;
    private Button buttonStopSpeech;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don__speaker);

        final SeekBar sbVolume = (SeekBar)findViewById(R.id.seekBar);
        final TextView textVolume = (TextView)findViewById(R.id.textView5);
        final SeekBar sBSpeed = (SeekBar)findViewById(R.id.seekBar2);
        final TextView textSpeed = (TextView)findViewById(R.id.textView);

        // ボタンのClickListenerの登録
        buttonSpeech = (Button)findViewById(R.id.button);
        buttonStopSpeech = (Button)findViewById(R.id.button2);
        buttonSpeech.setOnClickListener(this);
        buttonStopSpeech.setOnClickListener(this);

        // TextToSpeechオブジェクトの生成
        tts = new TextToSpeech(this, this);

        // 音量シークバーの値を表示
        textVolume.setText(":"+sbVolume.getProgress());

        sbVolume.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        textVolume.setText(":" + sbVolume.getProgress());

                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        // 読み上げ速度シークバーの値を表示
        textSpeed.setText(":"+sBSpeed.getProgress()/10f);
        tts.setSpeechRate(sBSpeed.getProgress()/10f);

        sBSpeed.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        textSpeed.setText(":" + sBSpeed.getProgress()/10f);
                        tts.setSpeechRate(sBSpeed.getProgress()/10f);
                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
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
        Request request = new Request.Builder()
                .url(((EditText)findViewById(R.id.EditText)).getText().toString()
                        + "/api/v1/timelines/public?local=true")
                .get()
                .build();


        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();

                Gson gson = new Gson();

                UserEntity[] userEntity = gson.fromJson(result, UserEntity[].class);

                for (int i = 19; i >= 0; i--) {
                    tts.speak(userEntity[i].getContent().replaceAll("<.+?>", "")
                            ,TextToSpeech.QUEUE_ADD, null
                    );
                }

            }
        });
    }

    private void stopSpeech() {
        if (tts.isSpeaking()) {
         // 読み上げ中なら止める
         tts.stop();
         }
    }

    @Override
    public void onClick(View v) {
        if (buttonSpeech == v){
            speechText();
        }else if (buttonStopSpeech == v) {
            stopSpeech();
        }
    }

}
