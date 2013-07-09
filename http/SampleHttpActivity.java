import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import android.app.Activity;
import android.os.Bundle;

public class SampleHttpActivity extends Activity {
	private HttpClient httpClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//uncomment line below to set content view
		//setContentView(R.layout.main);
		httpClient = CustomHttpClient.getHttpClient();
		getHttpContent();
	}

	public void getHttpContent() {
		try {
			HttpGet request = new HttpGet("http://www.google.com/");
			String page = httpClient.execute(request,
					new BasicResponseHandler());
			System.out.println(page);
		} catch (IOException e) {
			// covers:
			// ClientProtocolException
			// ConnectTimeoutException
			// ConnectionPoolTimeoutException
			// SocketTimeoutException
			e.printStackTrace();
		}
	}
}