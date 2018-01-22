package com.example.retia.don_speaker;

import android.app.Activity;
import android.app.DownloadManager;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.util.Log;
import android.widget.*;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.*;
import java.util.*;

public class DON_Speaker extends AppCompatActivity
        implements View.OnClickListener, TextToSpeech.OnInitListener {

    Timer mTimer = null;
    private TextToSpeech tts;
    private long tootId = 0;
    private HashMap<String, String> params = new HashMap<String, String>();
    private Request request;
    private String ltlContent;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don__speaker);

        final SeekBar sbVolume = (SeekBar)findViewById(R.id.seekBar);
        final TextView textVolume = (TextView)findViewById(R.id.textView5);
        final SeekBar sBSpeed = (SeekBar)findViewById(R.id.seekBar2);
        final TextView textSpeed = (TextView)findViewById(R.id.textView);
        final SeekBar sBPitch = (SeekBar)findViewById(R.id.seekBar3);
        final TextView textPitch = (TextView)findViewById(R.id.textView7);



        // ボタンのClickListenerの登録
        findViewById(R.id.speekStartBtn).setOnClickListener(this);
        findViewById(R.id.speekStopBtn).setOnClickListener(this);

        // TextToSpeechオブジェクトの生成
        tts = new TextToSpeech(this, this);

        // 音量シークバーの値を表示
        textVolume.setText(":"+sbVolume.getProgress());
        params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME
                ,String.valueOf(sbVolume.getProgress()/200f));

        sbVolume.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        textVolume.setText(":" + sbVolume.getProgress());
                        params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME
                                ,String.valueOf(sbVolume.getProgress()/200f));

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

        // 音声ピッチシークバーの値を表示
        textPitch.setText(":"+sBPitch.getProgress()/10f);
        tts.setPitch(sBPitch.getProgress()/10f);

        sBPitch.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // ツマミをドラッグしたときに呼ばれる
                        textPitch.setText(":" + sBPitch.getProgress()/10f);
                        tts.setPitch(sBPitch.getProgress()/10f);
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
            Locale locale = Locale.JAPANESE;
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
        final CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        final CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        final String apiUrl = ((EditText)findViewById(R.id.EditText)).getText().toString()
                + "/api/v1/timelines/public?local=true";
        if(mTimer == null) {
            mTimer = new Timer(true);
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    request = new Request.Builder()
                            //TODO:関係ないURL入れたときの例外処理
                            .url(apiUrl)
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

                            List<UserEntity> list = Arrays.asList(userEntity);

                            Collections.reverse(list);

                            AccountEntity[] accountEntity = gson.fromJson(result, AccountEntity[].class);

                            List<AccountEntity> accountList = Arrays.asList(accountEntity);

                            Collections.reverse(accountList);

                            for (UserEntity i : list) {
                                if (tootId < i.getId()) {
                                    tootId = i.getId();

                                    ltlContent = i.getContent();

                                    ltlContent = ltlContent.replaceAll("<.+?>", ""); //HTMLタグの除去

                                    if(checkBox.isChecked()) {
                                        ltlContent = ltlContent.replaceAll(
                                                "https?://[\\w/:%#\\$&\\?\\(\\)~\\.=\\+\\-]+", "URL省略");//URLの除去
                                    }

                                    if(checkBox2.isChecked()) {
                                        ltlContent = ltlContent + "あっと" + i.getAccount().getDisplayName();//ユーザー名読み上げ
                                    }

                                    tts.speak(ltlContent, TextToSpeech.QUEUE_ADD, params);
                                }
                            }

                        }
                    });
                }

            }, 0, 5000);
        }
    }

    private void stopSpeech() {
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if (tts.isSpeaking()) {
         // 読み上げ中なら止める
         tts.stop();
         }
    }

    @Override
    public void onClick(View v) {
        /*if (buttonSpeech == v){
            speechText();
        }else if (buttonStopSpeech == v) {
            stopSpeech();
        }*/
        if (v != null) {
            switch (v.getId()) {
                case R.id.speekStartBtn:
                    speechText();
                    break;

                case R.id.speekStopBtn:
                    stopSpeech();
                    break;

                default:
                    break;
            }
        }
    }

}
