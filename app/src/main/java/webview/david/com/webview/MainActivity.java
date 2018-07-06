package webview.david.com.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class MainActivity extends Activity  {
    Button b1;
    EditText ed1;
    Button snap;
    Button view;
    File imageFile;

    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1=(Button)findViewById(R.id.button);
        ed1=(EditText)findViewById(R.id.editText);

        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        //display instruction image
        wv1.loadUrl("file:///android_asset/webview.html");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ed1.getText().toString();

                wv1.getSettings().setLoadsImagesAutomatically(true);
                wv1.getSettings().setJavaScriptEnabled(true);
                wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                wv1.loadUrl("http://"+url);
            }
        });

        snap=(Button)findViewById(R.id.takeSnapshot);
        snap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                takeScreenshot(v);
            }
        });


        view=(Button)findViewById(R.id.viewSnapshot);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                viewSnapshot(v);
            }
        });

    }
    public void takeScreenshot(View view) {
        try {
            //path onto SD card
            String mPath = Environment.getExternalStorageDirectory().toString();
            //create bitmap screen capture. true and then false
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            imageFile = new File(mPath,"/snapshot.png");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void viewSnapshot(View view) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }


    //So you can click on link when viewing in browser window. shouldOverrideUrlLoading() is called.
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
