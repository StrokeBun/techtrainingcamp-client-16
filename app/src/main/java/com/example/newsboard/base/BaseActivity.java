package com.example.newsboard.base;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsboard.ui.activity.HistoryActivity;
import com.example.newsboard.ui.activity.LoginActivity;
import com.example.newsboard.ui.fragment.MineFragment;
import com.example.newsboard.util.TokenUtils;

/**
 * @Title: BaseActivity
 * @Package: base
 * @Description: base activity, provide LogoutReceiver to handle logout action
 * @author: Zhong Defeng
 * @date: 2020/11/16 14:21
 */
public class BaseActivity extends AppCompatActivity {

    private LogoutReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MineFragment.ACTION_LOGOUT);
        receiver = new LogoutReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    static class LogoutReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("退出确认");
            builder.setMessage("退出当前账号，将无法进行评论和收藏");
            builder.setCancelable(true);
            builder.setPositiveButton("确认退出", (dialog, which) -> {
                clear();
                context.startActivity(new Intent(context, LoginActivity.class));
            });

            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.show();
        }

        private void clear() {
            ActivityController.finishAll();
            TokenUtils.clearToken();
            HistoryActivity.clearHistoryNews();
        }
    }
}
