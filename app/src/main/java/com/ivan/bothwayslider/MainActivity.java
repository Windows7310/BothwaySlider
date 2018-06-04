package com.ivan.bothwayslider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BothwaySliderView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = findViewById(R.id.bsv_view);
        mView.setMinValue(-100);
        mView.setMaxValue(100);
        mView.setLowValue(-80);
        mView.setHighValue(0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.0 需打开硬件加速
//            getWindow().setFlags(0x1000000, 0x1000000);
//        }

//        String webUrl = "http://player.youku.com/embed/XMzYzOTc3ODk5Mg==";
//        String webUrl = "https://www.sjipano.com:9443/#/?pano_id=7503";
//        String webUrl = "https://v.qq.com/iframe/player.html?vid=i067317a6tm&tiny=0&auto=0";
//        String webUrl = "http://open.iqiyi.com/developer/player_js/coopPlayerIndex.html?vid=8bff60852ed2da85be7ae2994412e2cd&tvId=1067967600&accessToken=2.f22860a2479ad60d8da7697274de9346&appKey=3955c3425820435e86d0f4cdfe56f5e7&appId=1368";
//        mWebView = findViewById(R.id.wv_webview);
//        mWebView.loadUrl(webUrl);

//        chromium: [ERROR:gles2_cmd_decoder.cc(7639)] [.CommandBufferContext]RENDER WARNING: texture bound to texture unit 1 is not renderable. It maybe non-power-of-2 and have incompatible texture filtering.

//        WebSettings settings = mWebView.getSettings();
//
//        //webView  加载视频，下面五行必须
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        settings.setJavaScriptEnabled(true);//支持js
//        settings.setPluginState(WebSettings.PluginState.ON);// 支持插件
//        settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//
//        settings.setUseWideViewPort(true);  //将图片调整到适合webview的大小  无效
//        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        if (!mWebView.isHardwareAccelerated()) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.loadUrl(webUrl);// 加载链接
    }
}
