package jp.ac.university.wakayama.docoikumikan.app.enjoyYourLive;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import java.net.URL;


public class MainActivity extends Activity {
    // �V�F�C�N�Ɣ��肷�邵�����l
    private static final int SHAKE_THRESHOLD = 5;
    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    private TextView mTvShakeCounter;
    private int mCount = 0;
    private long mPrevTime;
    private float mPrevX;
    private float mPrevY;
    private float mPrevZ;
 //   private final static String URL="http://192.168.51.108:3000/php/";
    private final static String URL="http://192.168.43.15:3000/php/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // �V�F�C�N�J�E���^�[�̏�����
        mTvShakeCounter = (TextView) findViewById(R.id.ShakeCount);

        // �X�C�b�`�E�B�W�F�b�g�̏�����
        Switch shakeSwitch = (Switch) findViewById(R.id.AccelerationSwitch);
        shakeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // �Z���T�[ON
                    // �����x�Z���T�[���擾
                    List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
                    if (sensors.size() > 0) {
                        // �����x�Z���T�[���Z���T�[�}�l�[�W���[�ɓo�^
                        Sensor s = sensors.get(0);
                        mSensorManager.registerListener(mSensorEventListener, s,
                                SensorManager.SENSOR_DELAY_UI);
                    }
                } else { // �Z���T�[OFF
                    // �����x�Z���T�[���Z���T�[�}�l�[�W���[����폜
                    mSensorManager.unregisterListener(mSensorEventListener);
                    mTvShakeCounter.setText(getString(R.string.shake_count, 0));
                }
            }
        });

        // �Z���T�[�}�l�[�W���[���擾
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // �Z���T�[�C�x���g���X�i�[�̐ݒ�
        mSensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                // �ȑO�Z���T�[���ғ��������Ԃƌ��݂̎��Ԃ̍������߁A
                // 400ms�ȏ�o�߂��Ă���Ώ�������(�f�B���C��݂���)
                long nowTime = System.currentTimeMillis();
                long diffTime = nowTime - mPrevTime;
                if (diffTime > 400) {
                    // X,Y,Z���̉����x���擾
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // �ȑO�擾���������x�̍������߃V�F�C�N�l�ɂ���
                    float shakeValue = Math.abs(x + y + z - mPrevX - mPrevY - mPrevZ);

                    // �V�F�C�N�l���������l�ȏ�ł���΃V�F�C�N����
                    if (shakeValue > SHAKE_THRESHOLD) {
                        // ��ʂ̃J�E���g�𑝂₷
                    	//setContentView(R.layout.change_layout);
                        mCount++;
                        mTvShakeCounter.setText(getString(R.string.shake_count, mCount));
                        
                    	if( mCount >= 10) {

                    		wemot();
                    		
                            mSensorManager.unregisterListener(mSensorEventListener);
                            mTvShakeCounter.setText(getString(R.string.shake_count, 0));
                    		
                    		Log.v("Move","mCount was over 20.");
                    		Intent intent = new Intent(MainActivity.this,FinishActivity.class);
                    		startActivity(intent);
                    		MainActivity.this.finish();
                    	}
                        
                    }

                    // �擾���ԂƉ����x��ۑ�
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
            

	public void wemot() {
	


        //HTTP�ʐM

       try {
    	   Log.v("Move","move to wemot");
    	   
            String str= http2str(URL);

        	Log.v("result",str);
        } catch (Exception e) {

        }

    }

    

    //HTTP�ʐM��������

	private static String http2str(String path) throws Exception { 
		Log.v("move","move to http2str");

        byte[] w=http2data(path);

        Log.v("move"," "+ w);
        
        return new String(w);

    }    

    

	//HTTP�ʐM

   public static byte[] http2data(String path) throws Exception {

        HttpURLConnection c=null;

        InputStream in=null;

        ByteArrayOutputStream out=null;

        try {

           //HTTP�ڑ��̃I�[�v��(2)

            URL url=new URL(path);

            c=(HttpURLConnection)url.openConnection();

            c.setRequestMethod("GET");

            c.connect();

            in=c.getInputStream();


            in.close();

            c.disconnect();

            return out.toByteArray();

        } catch (Exception e) {

            try {

                if (c!=null) c.disconnect();

                if (in!=null) in.close();

                if (out!=null) out.close();

            } catch (Exception e2) {

            }

            throw e;

        }
    }     


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
