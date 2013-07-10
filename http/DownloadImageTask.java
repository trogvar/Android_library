import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
	private Context mContext;
	private TextView mProgressView;
	private ImageView mImageView;
	
	DownloadImageTask(Context context, Integer idProgressView, Integer idImageView) {
		mContext = context;
		mProgressView = (TextView)((Activity)mContext).findViewById(idProgressView);
		mImageView = (ImageView)((Activity)mContext).findViewById(idImageView);
	}
	
	@Override
	protected void onPreExecute() {
		// do some setup work here if needed
		super.onPreExecute();
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		Log.v("doInBackground", "doing download of image"); 
		return downloadImage(urls);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		Log.v("onProgressUpdate", "Progress: " + values[0].toString());
		if (mProgressView != null)
			mProgressView.setText("Progress so far: " + values[0]);
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			mImageView.setImageBitmap(result);
			Log.v("onPostExecute", "Finished successfully. Setting bitmap to ImageView");
		} else {
			Log.v("onPostExecute", "Finished with NULL result.");
			if (mProgressView != null) {
				mProgressView.setText("Problem downloading image");
			}
		}
	}

	private Bitmap downloadImage(String... urls) {
		HttpClient httpClient = CustomHttpClient.getHttpClient();
		try {
			HttpGet request = new HttpGet(urls[0]);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 10000);
			request.setParams(params);
			
			publishProgress(25);
			
			HttpResponse response = httpClient.execute(request);
			
			publishProgress(50);
			
			byte[] image = EntityUtils.toByteArray(response.getEntity());
			
			publishProgress(75);
			
			Bitmap mBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
			
			publishProgress(100);
			
			return mBitmap;
		} catch (IOException e) {
			//covers:
			//	ClientProtocolException
			//	ConnectionTimeoutException
			//	ConnectionPoolTimeoutException
			//	SocketTimeoutException
			e.printStackTrace();
		}
		return null;
	}
}
