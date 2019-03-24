package jp.ne.sakura.kkkon.test.android.gles2;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private static final String TAG = "GLRenderer";

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig)
    {
        Log.d( TAG, "onSurfaceCreated: enter" );
        GLES20.glClearColor( 0.0f, 1.0f, 0.0f, 1.0f );
        Log.d( TAG, "onSurfaceCreated: leave" );
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height)
    {
        Log.d( TAG, "onSurfaceChanged: enter" );
        Log.d( TAG, String.format(" onSurfaceChanged: w=%d,h=%d", width, height ) );
        GLES20.glViewport( 0, 0, width, height );
        Log.d( TAG, "onSurfaceChanged: leave" );
    }

    @Override
    public void onDrawFrame(GL10 gl10)
    {
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
    }
}
