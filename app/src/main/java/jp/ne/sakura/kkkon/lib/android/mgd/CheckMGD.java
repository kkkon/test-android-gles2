package jp.ne.sakura.kkkon.lib.android.mgd;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

public class CheckMGD
{
    private static final String TAG = "CheckMGD";

    private static final boolean USE_MGD = true;


    public static boolean checkMGDtargetSdkVersion(final Context context)
    {
        boolean result = false;

        if ( null == context )
        {
            return false;
        }

        int appTargetSdkVersion = 0;
        {
            final ApplicationInfo infoApp = context.getApplicationInfo();
            if ( null != infoApp )
            {
                appTargetSdkVersion = infoApp.targetSdkVersion;
            }
        }
        Log.d( TAG, "App targetSdkVer=" + appTargetSdkVersion );

        boolean underTargetSdk26App = false;
        if ( appTargetSdkVersion < 26 )
        {
            underTargetSdk26App = true;
        }
        else
        {
            underTargetSdk26App = false;
        }


        int mgdTargetSdkVersion = 0;
        {
            final PackageManager pm = context.getPackageManager();
            if ( null != pm )
            {
                try
                {
                    final PackageInfo infoPackage = pm.getPackageInfo("com.arm.mgd.androidapp", 0);
                    if ( null != infoPackage )
                    {
                        Log.d( TAG, "MGD ver=" + infoPackage.versionName );
                        final ApplicationInfo infoApp = infoPackage.applicationInfo;
                        if ( null != infoApp )
                        {
                            final int targetSdkVersion = infoApp.targetSdkVersion;
                            Log.d( TAG, "MGD targetSdk=" + targetSdkVersion );
                            mgdTargetSdkVersion = targetSdkVersion;

                            if ( 24 <= Build.VERSION.SDK_INT )
                            {
                                final int minSdkVersion = infoApp.minSdkVersion;
                                Log.d(TAG, "MGD minSdkVer=" + minSdkVersion);
                            }
                        }
                    }
                }
                catch ( PackageManager.NameNotFoundException e )
                {
                    Log.d( TAG, "", e );
                }
            }
        }

        boolean underTargetSdk26MGD = false;
        if ( mgdTargetSdkVersion < 26 )
        {
            underTargetSdk26MGD = true;
        }
        else
        {
            underTargetSdk26MGD = false;
        }

        {
            final String msg = String.format("targetSdkVersion: app=%d mgd=%d", appTargetSdkVersion, mgdTargetSdkVersion);
            Log.i(TAG, msg);
        }

        if ( Build.VERSION.SDK_INT < 26 )
        {
            result = true;
        }
        else
        {
            if ( 0 == mgdTargetSdkVersion )
            {
                Log.e( TAG, "!!! MGD not found !!!\n" );
            }
            else
            {
                // exist separate SELinux domain by targetSdkVersion
                if (
                    (underTargetSdk26MGD && underTargetSdk26App)
                    || (!underTargetSdk26MGD && !underTargetSdk26App)
                )
                {
                    // OK. same SELinux domain
                    result = true;
                }
                else
                {
                    // rejected by SELinux, between 'untrusted_app' and 'untrusted_app_25'
                    if ( underTargetSdk26MGD )
                    {
                        Log.e( TAG, "!!! Your App targetSdkVersion high !!!\nYou must change to 25 or lower" );
                    }
                    else
                    {
                        Log.e( TAG, "!!! MGD targetSdkVersion high !!!\nYou must change to 26 or higher" );
                    }
                }
            }
        }

        return result;
    }

    static
    {
        if ( USE_MGD )
        {
            // final String tag = "[ MGD ]";
            try
            {
                System.loadLibrary("MGD");
                Log.d( TAG, "MGD interceptor loaded." );
            }
            catch ( UnsatisfiedLinkError e )
            {
                Log.e( TAG, "MGD not loaded: " + e.getMessage() );
                Log.w( TAG, Log.getStackTraceString(e) );
            }
        }
    }
}
