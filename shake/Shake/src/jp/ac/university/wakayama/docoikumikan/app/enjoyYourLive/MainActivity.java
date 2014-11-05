package jp.ac.university.wakayama.docoikumikan.app.enjoyYourLive;

import java.util.List;

import jp.ac.university.wakayama.docoikumikan.app.enjoyYourLive.R;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
    // シェイクと判定するしきい値
    private static final int SHAKE_THRESHOLD = 5;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private TextView mTvShakeCounter;
    private int mCount = 0;
    private long mPrevTime;
    private float mPrevX;
    private float mPrevY;
    private float mPrevZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // シェイクカウンターの初期化
        mTvShakeCounter = (TextView) findViewById(R.id.ShakeCount);

        // スイッチウィジェットの初期化
        Switch shakeSwitch = (Switch) findViewById(R.id.AccelerationSwitch);
        shakeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // センサーON
                    // 加速度センサーを取得
                    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                    if (sensors.size() > 0) {
                        // 加速度センサーをセンサーマネージャーに登録
                        Sensor s = sensors.get(0);
                        mSensorManager.registerListener(mSensorEventListener, s,
                                SensorManager.SENSOR_DELAY_UI);
                    }
                } else { // センサーOFF
                    // 加速度センサーをセンサーマネージャーから削除
                    mSensorManager.unregisterListener(mSensorEventListener);
                    mTvShakeCounter.setText(getString(R.string.shake_count, 0));
                }
            }
        });

        // センサーマネージャーを取得
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // センサーイベントリスナーの設定
        mSensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                // 以前センサーが稼働した時間と現在の時間の差を求め、
                // 400ms以上経過していれば処理する(ディレイを設ける)
                long nowTime = System.currentTimeMillis();
                long diffTime = nowTime - mPrevTime;
                if (diffTime > 400) {
                    // X,Y,Z軸の加速度を取得
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // 以前取得した加速度の差を求めシェイク値にする
                    float shakeValue = Math.abs(x + y + z - mPrevX - mPrevY - mPrevZ);

                    // シェイク値がしきい値以上であればシェイク処理
                    if (shakeValue > SHAKE_THRESHOLD) {
                        // 画面のカウントを増やす
                    	//setContentView(R.layout.change_layout);
                        mCount++;
                        mTvShakeCounter.setText(getString(R.string.shake_count, mCount));
                        
                    	if( mCount >= 20) {
                        	
                    		Log.v("Move","mCount was over 20.");
                    		Intent intent = new Intent(MainActivity.this,FinishActivity.class);
                    		startActivity(intent);
                    		MainActivity.this.finish();
                    	}
                        
                    }

                    // 取得時間と加速度を保存
                    mPrevTime = nowTime;
                    mPrevX = x;
                    mPrevY = y;
                    mPrevZ = z;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
