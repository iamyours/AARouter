package io.github.iamyours.aarouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import io.github.iamyours.aarouter.annotation.Route;

@Route(path = "/app/main")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARouter.getInstance().init(this);
    }

    public void toTest(View v) {
        ARouter.getInstance()
                .build("/app/test")
                .withString("title", "test")
                .navigation(this, 1);
    }
}
