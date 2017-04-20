# AsyncTask
Android AsyncTask－耗時工作設計
應用程式在手機上執行時，主要負責與使用者互動，是以一個專屬的執行緒進行，這個執行緒稱為「UI thread」或「main thread」。

耗時工作處理－AsyncTask類別
Async是非同步「Asynchronized」的簡寫，它允許開發人員設計能在背景執行的工作，並提供方法能夠與UI thread溝通互動，適合進行較短時間（數秒）的耗時工作。

設計一個類別並繼承android.os.AsyncTask

AsyncTask<傳入值型態, 更新進度型態, 結果型態>

傳入值型態--代表工作是否需要傳入資料，若需要，則要先定義資料的型態，(Integer、String、Boolean..)
，若不需要任何傳入值可使用Void(大寫的V)類別。
 將工作寫在doInBackground方法中。

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
    
 呼叫execute方法後，AsyncTask會自動執行必要的方法後自動執行doInBackground方法
 ，而不是由開發人員直接去呼叫doInBackground方法，這樣不會以背景執行緒執行。    
 
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
   
   // 在編譯時會轉換為陣列，（與AsyncTask的傳入值型態配合）若是字串時，
   
   //params[0]即代表陣列的第一個字串值
      
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
    
與UI thread互通的方法

一個AsyncTask的執行流程有四個階段，除了doInBackground方法之外，AsyncTask類別定義了另外三個方法能夠與UI Thread或UI元件互動

onPreExecute-之前 在背景工作執行之前會自動執行的方法，開發人員可覆寫onPreExecute，在方法內撰寫程式碼

onProgressUpdate-過程 在下載過程中可以手動呼叫的方法，在doInBackground中呼叫「publishProgress方法」會自動執行onProgressUpdate方法內的程式碼

onPostExecute-之後  在背景工作執行完成後會自動執行的方法，開發人員可覆寫onPostExecute，在方法內撰寫工作完成後必要的程式碼。


學習參考資料來源：http://litotom.com/2016/03/26/asynctask%EF%BC%8D%E8%80%97%E6%99%82%E5%B7%A5%E4%BD%9C%E8%A8%AD%E8%A8%88/
