package mx.coderia.iuvareapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();

        loadWebView();
    }

    public void loadWebView() {

        if (isOnline()){
            setContentView(R.layout.activity_main);

            mWebView = (WebView) findViewById(R.id.activity_main_webview);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder
                            .setTitle("Lo sentimos")
                            .setMessage("Ocurrió un error al cargar el contenido de la app.")
                            .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    loadWebView();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progress.dismiss();
                }
            });

            mWebView.setWebChromeClient(new WebChromeClient());

            // Enable Javascript
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mWebView.loadUrl("http://socios.iuvare.mx");

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder
                    .setTitle("No hay conexión a internet")
                    .setMessage("Conéctate a una red Wi-Fi o habilita tus datos celulares.")
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            loadWebView();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
