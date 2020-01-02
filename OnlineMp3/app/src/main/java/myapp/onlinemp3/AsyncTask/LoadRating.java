package myapp.onlinemp3.AsyncTask;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import myapp.onlinemp3.Interfaces.RatingListener;
import myapp.onlinemp3.Utils.Constant;
import myapp.onlinemp3.Utils.JsonUtils;

public class LoadRating extends AsyncTask<String,String,Boolean> {

    private String msg = "";
    private String rate="0";
    private RatingListener ratingListener;

    public LoadRating(RatingListener ratingListener) {
        this.ratingListener = ratingListener;
    }

    @Override
    protected void onPreExecute() {
        ratingListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        String json = JsonUtils.getJSONString(url);
        try {
            JSONObject jOb = new JSONObject(json);
            JSONArray jsonArray = jOb.getJSONArray(Constant.TAG_ROOT_1);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                msg = c.getString("MSG");

                if(!msg.contains("already rated")) {
                    rate = c.getString("rate_avg");
                }

            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (Exception ee) {
            ee.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        ratingListener.onEnd(String.valueOf(s),msg,Integer.parseInt(rate));
        super.onPostExecute(s);
    }
}
