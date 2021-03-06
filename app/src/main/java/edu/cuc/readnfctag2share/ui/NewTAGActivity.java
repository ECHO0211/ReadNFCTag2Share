package edu.cuc.readnfctag2share.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


import edu.cuc.readnfctag2share.R;
import edu.cuc.readnfctag2share.helpers.NFCHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NewTAGActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private static String TAG = NewTAGActivity.class.getSimpleName();
    private NFCHelper nfcHelper;
    private static final int RC_CAMERA_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newtag);
        findViewById(R.id.btn_begin_scan_QRCode).setOnClickListener(this);
        findViewById(R.id.btn_begin_add_tag).setOnClickListener(this);
        findViewById(R.id.btn_finish_add_tag).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        nfcHelper = new NFCHelper(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcHelper.resolveNFCIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcHelper.setupForegroundDispatch();
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcHelper.undoForegroundDispatch();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_begin_add_tag:
                findViewById(R.id.v_step1).setVisibility(View.VISIBLE);
            case R.id.btn_begin_scan_QRCode:
//                startScanQRCodeActivityForResult();
                break;
            case R.id.btn_finish_add_tag:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0 || resultCode != 0) return;
        if (data == null) return;
        String result = data.getStringExtra("RESULT");
        if (result == null) return;

        Log.i(TAG, result);
        findViewById(R.id.v_step2).setVisibility(View.VISIBLE);
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void startScanQRCodeActivityForResult() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            startActivityForResult(new Intent(this, ScanQRCodeActivity.class), 0);
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    RC_CAMERA_PERM,
                    Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
