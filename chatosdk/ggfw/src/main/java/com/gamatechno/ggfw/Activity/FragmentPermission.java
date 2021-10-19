package com.gamatechno.ggfw.Activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;

import com.gamatechno.ggfw.Activity.Interfaces.PermissionResultInterface;

import java.util.ArrayList;




/**
 * Created by root on 2/26/18.
 */

public class FragmentPermission extends Fragment {


    private int KEY_PERMISSION = 0;
    private PermissionResultInterface permissionResultInterface;
    private String permissionsAsk[];


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRetainInstance(false);
    }


    public FragmentPermission askPermission(String permission) {
        this.permissionsAsk = new String[]{permission};
        return FragmentPermission.this;
    }

    public FragmentPermission askPermissions(String permissions[]) {
        this.permissionsAsk = permissions;
        return FragmentPermission.this;
    }

    public FragmentPermission setPermissionResult(PermissionResultInterface permissionResultInterface) {
        this.permissionResultInterface = permissionResultInterface;
        return FragmentPermission.this;
    }

    public FragmentPermission requestPermission(int keyPermission) {
        KEY_PERMISSION = keyPermission;
        internalRequestPermission(permissionsAsk);
        return FragmentPermission.this;
    }


    public boolean isPermissionGranted(Context context, String permission) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }


    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();


        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(getActivity(), permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {
            if (permissionResultInterface != null)
                permissionResultInterface.permissionGranted();

        } else {
            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            requestPermissions(arrayPermissionNotGranted, KEY_PERMISSION);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == KEY_PERMISSION) {
            boolean granted = true;

            for (int grantResult : grantResults) {
                if (!(grantResults.length > 0 && grantResult == PackageManager.PERMISSION_GRANTED))
                    granted = false;
            }
            if (permissionResultInterface != null) {
                if (granted) {
                    permissionResultInterface.permissionGranted();
                } else {
                    permissionResultInterface.permissionDenied();
                }
            } else {
                Log.e("ManagePermission", "permissionResultInterface callback was null");
            }
        }

    }

    public void askCompactPermission(String permission, PermissionResultInterface permissionResultInterface) {
        KEY_PERMISSION = 200;
        permissionsAsk = new String[]{permission};
        this.permissionResultInterface = permissionResultInterface;
        internalRequestPermission(permissionsAsk);
    }

    public void askCompactPermissions(String permissions[], PermissionResultInterface permissionResultInterface) {
        KEY_PERMISSION = 200;
        permissionsAsk = permissions;
        this.permissionResultInterface = permissionResultInterface;
        internalRequestPermission(permissionsAsk);
    }

}
