package com.admin.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_GO1, btn_GO2, btn_GO3;
    private TextView text_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_GO1 = (Button)findViewById(R.id.btn_go1);
        btn_GO2 = (Button)findViewById(R.id.btn_go2);
        btn_GO3 = (Button)findViewById(R.id.btn_go3);
        text_Info = (TextView)findViewById(R.id.text_Info);

        btn_GO1.setOnClickListener(btnGo1Listener);
        btn_GO2.setOnClickListener(btnGo2Listener);
        btn_GO3.setOnClickListener(btnGo3Listener);

    }

    //在畫面上按下GO1按鈕，工作5秒鐘，結束後在TextView中顯示「DONE」
    private View.OnClickListener btnGo1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Job1Task().execute(); // 產生類別並呼叫execute()
        }
    };
    //btn_GO1 AsyncTask 傳入值與回傳值都是Void
    class Job1Task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        //五秒結束後需改變TextView中的文字，因此再覆寫onPostExecute方法
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            text_Info.setText("GO1_DONE....");
        }
    }

    //按下GO2按鈕，工作n杪後，結束後在TextView中顯示「DONE」
    private View.OnClickListener btnGo2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Job2Task().execute(3);
        }
    };
    //設計Job2Task類別時，傳入值宣告為Integer，代表整數
    class Job2Task extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0] * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            text_Info.setText("GO2_DONE....");
        }
    }

    //在畫面上按下GO3按鈕，需每秒更新秒數到TextView
    private View.OnClickListener btnGo3Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new Job3Task().execute(6); //到數6妙回
        }
    };

    //工作執行過程中需要回報秒數，設計Job3Task類別時的第二個參數應宣告為Integer
    //doInBackground方法的參數使用的是Java 1.5開始提供的陣列參數語法，
    // 在編譯時會轉換為陣列，（與AsyncTask的傳入值型態配合）若是字串時，params[0]即代表陣列的第一個字串值
    class Job3Task extends AsyncTask<Integer, Integer, Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            for (int i = params[0]; i > 0; i--){
                //呼叫publishProgress方法，會自動呼叫onProgressUpdate方法，
                // 此方法負責每秒更新TextView中的文字
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        //應覆寫onPostExecute與onProgressUpdate兩個方法
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            text_Info.setText("GO3_DONE....");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            text_Info.setText(String.valueOf(values[0]));
        }
    }

}
