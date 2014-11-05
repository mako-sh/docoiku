package jp.ac.university.wakayama.docoikumikan.app.enjoyYourLive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FinishActivity extends Activity {
	
	Button mStartBtn;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        
        mStartBtn = (Button)findViewById(R.id.btn_return);
        mStartBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	if( v == mStartBtn) {
            	
            		Log.v("OnClick","Return Button was clicked.");
            		Intent intent = new Intent(FinishActivity.this,TopActivity.class);
            		startActivity(intent);
            		FinishActivity.this.finish();
            	}
            }
        });
	}
	
}
