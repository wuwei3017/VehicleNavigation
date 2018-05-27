package chary.nyist.com.baidumapdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BNEventDialog extends Dialog {

    private Context mContext;

    private LinearLayout mRouteGuideLl;

    private TextView mRemainTimeTx;
    private TextView mRemainDistanceTx; // 剩余总距离
    private TextView mCurrentSpeedTx;

    private ImageView mTurnImage;
    private TextView mGoDistanceTx;
    private TextView mNextRoadTx;

    private TextView mAlongMeters;
    private TextView mCurrentRoadTx;

    private ImageView mEnlargeImg;

    private TextView mLocateTx;

    public BNEventDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater infalter = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = infalter.inflate(R.layout.navi_event_dialog, null);
        setContentView(layout);

        if (layout == null) {
            return ;
        }

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = 60;
        lp.height = 60;
        window.setAttributes(lp);
        window.setGravity(Gravity.TOP | Gravity.LEFT);


    }

    public void updateLocateState(boolean hasLocate) {
        if (mLocateTx != null) {
            mLocateTx.setText(hasLocate ? "定位成功" : "定位中");
        }
    }

    public void onEnlageShow(int type, Bitmap arrowBmp, Bitmap bgBmp) {
        if (mEnlargeImg != null) {
            mEnlargeImg.setImageBitmap(arrowBmp);
            mEnlargeImg.setBackgroundDrawable(new BitmapDrawable(bgBmp));
            mEnlargeImg.setVisibility(View.VISIBLE);
        }
        if (mRouteGuideLl != null) {
            mRouteGuideLl.setVisibility(View.GONE);
        }
    }

    public void onEnlargeHide() {
        if (mEnlargeImg != null) {
            mEnlargeImg.setVisibility(View.GONE);
        }
        if (mRouteGuideLl != null) {
            mRouteGuideLl.setVisibility(View.VISIBLE);
        }
    }

    public void updateTurnIcon(Bitmap map) {
        if (mTurnImage != null) {
            mTurnImage.setImageBitmap(map);
        }
    }

    public void updateGoDistanceTx(String tx) {
        if (mGoDistanceTx != null) {
            mGoDistanceTx.setText(tx);
        }
    }

    public void updateNextRoad(String nextRoad) {
        if (mNextRoadTx != null) {
            mNextRoadTx.setText(nextRoad);
        }
    }

    public void updateAlongMeters(String alongMeters) {
        if (mAlongMeters != null) {
            mAlongMeters.setText(alongMeters);
        }
    }

    public void updateCurrentRoad(String currentRoad) {
        if (mCurrentRoadTx != null) {
            mCurrentRoadTx.setText(currentRoad);
        }
    }

    public void updateCurrentSpeed(String speed) {
        if (mCurrentSpeedTx != null) {
            mCurrentSpeedTx.setText(speed);
        }
    }

    public void updateRemainDistance(String distance) {
        if (mRemainDistanceTx != null) {
            mRemainDistanceTx.setText(distance);
        }
    }

    public void updateRemainTime(String time) {
        if (mRemainTimeTx != null) {
            mRemainTimeTx.setText(time);
        }
    }
}
