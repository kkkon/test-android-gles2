package jp.ne.sakura.kkkon.test.android.gles2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import jp.ne.sakura.kkkon.lib.android.mgd.CheckMGD;

public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    boolean hasGLES2 = false;
    GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        CheckMGD.checkMGDtargetSdkVersion( this );

        final ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if ( null != am )
        {
            final ConfigurationInfo devConfInfo = am.getDeviceConfigurationInfo();
            if ( null != devConfInfo )
            {
                Log.d( TAG, "reqGlEsVer=" + Integer.toHexString(devConfInfo.reqGlEsVersion) );
                if ( 0x20000 <= devConfInfo.reqGlEsVersion )
                {
                    hasGLES2 = true;
                }
            }
        }

        if ( hasGLES2 )
        {
            mGLView = new GLSurfaceView(this);
            mGLView.setEGLContextClientVersion(2);
            if ( 11 <= Build.VERSION.SDK_INT )
            {
                mGLView.setPreserveEGLContextOnPause(true);
            }
            mGLView.setRenderer( new GLRenderer() );

            setContentView( mGLView );
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if ( null != mGLView )
        {
            mGLView.onPause();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if ( null != mGLView )
        {
            mGLView.onResume();
        }
    }

}
