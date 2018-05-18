package com.graduation.chary.asr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
//
import com.baidu.speech.VoiceRecognitionService;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;

import java.util.ArrayList;

public class AsrDialog {
    public static final String TAG = AsrDialog.class.getSimpleName();
    SpeechRecognizerCallBack callBack;
    Activity activity = null;
    Context context = null;
    String result = null;
    private SpeechRecognizer speechRecognizer;




    public AsrDialog(Context context){
        this.context = context;
    }


    /**
     * 设置回调接口
     * @param callBack 是SpeechRecognizerCallBack对象
     */
    public void setCallBack(SpeechRecognizerCallBack callBack)    {
        this.callBack = callBack;
    }


    /**
     * 开始语音识别
     */
    public void Start()
    {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, new ComponentName(context, VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(new MyRecognitionListener());

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"zh-CN");
        speechRecognizer.startListening(intent);
    }
    /**
     * 停止录音，但是识别将继续
     */
    public void Stop()
    {
        speechRecognizer.stopListening();
    }
    /**
     * 取消本次识别，已有录音也将不再识别
     */
    public void cancel()
    {
        speechRecognizer.cancel();
    }
    /**
     * 销毁语音识别器，释放资源
     */
    public void Destroy()
    {
        speechRecognizer.destroy();
    }

    public class MyRecognitionListener implements RecognitionListener
    {
        /**
         * 开始说话，当用户开始说话，会回调此方法。
         */
        @Override
        public void onBeginningOfSpeech() {

        }
        /**
         * 音量变化，引擎将对每一帧语音回调一次该方法返回音量值。
         */
        @Override
        public void onRmsChanged(float rmsdB) {

        }
        /**
         * 获取原始语音，此方法会被回调多次，buffer是当前帧对应的PCM语音数据，拼接后可得到完整的录音数据。
         */
        @Override
        public void onBufferReceived(byte[] buffer) {

        }
        /**
         * 说话结束，当用户停止说话后，将会回调此方法。
         */
        @Override
        public void onEndOfSpeech() {

        }
        /**
         * 识别出错，识别出错，将会回调此方法，调用该方法之后将不再调用onResults方法。
         * @param error 错误码
         */
        @Override
        public void onError(int error) {

        }
        /**
         * 识别最终结果，返回最终识别结果，将会回调此方法。
         * @param results 识别结果
         */
        @Override
        public void onResults(Bundle results) {

            String text =  results.get("results_recognition").toString().replace("]", "").replace("[", "");
            callBack.getResult(text);
            result =  text;
            Toast.makeText(context, "text : "+ text, Toast.LENGTH_SHORT).show();

        }
        /**
         * 识别临时结果，返回临时识别结果，将会回调此方法。
         * @param partialResults 临时结果
         */
        @Override
        public void onPartialResults(Bundle partialResults) {

        }
        /**
         * 识别事件返回，返回识别事件，将会回调此方法。
         * @param eventType 事件类型
         * @param params 参数
         */
        @Override
        public void onEvent(int eventType, Bundle params) {

        }
        /**
         * 识别准备就绪，只有当此方法回调之后才能开始说话，否则会影响识别结果。
         */
        @Override
        public void onReadyForSpeech(Bundle params) {


        }
    }
    /**
     * 识别结果回调接口
     * @author
     *
     */
    public interface SpeechRecognizerCallBack
    {
        /**
         * 返回结果
         * @param result String 结果
         */
        public void getResult(String result);
    }
}
