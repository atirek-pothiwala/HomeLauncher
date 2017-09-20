package alivemind.com.homelauncher;

import android.os.AsyncTask;

/**
 * Created by Admin on 9/20/2017.
 */

public class CustomAsyncTask extends AsyncTask<String, String, String> {

    public interface AsyncListener {

        void onPreExecute();

        void onPostExecute(String result);

        void onProgressUpdate(String... values);

        String doInBackground(String... params);

    }

    AsyncListener listener;

    public CustomAsyncTask(AsyncListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        listener.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {
        return listener.doInBackground(params);
    }
}
