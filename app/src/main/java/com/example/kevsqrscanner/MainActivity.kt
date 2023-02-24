package com.example.kevsqrscanner

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.kevsqrscanner.databinding.ActivityMainBinding

const val CAMERA_REQUEST_CODE = 10001



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scannerView
        binding.TvScanner
        setUpPermissions()

        codeScanner()


    }
    fun codeScanner()
    {
        codeScanner = CodeScanner(this,binding.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    binding.TvScanner.text = it.text

                }
            }
            errorCallback  = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(this@MainActivity,"Camera Initialization error:  ${it.message}", Toast.LENGTH_LONG).show()

                }
            }
            binding.scannerView.setOnClickListener {
                codeScanner.startPreview()
            }

        }

        fun onResume() {
            super.onResume()
            codeScanner.startPreview()
        }
        onPause();
        {
            super.onPause()
            codeScanner.releaseResources()
        }


    }

    fun setUpPermissions()
    {
        var perm = ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)

        if (perm!=PackageManager.PERMISSION_GRANTED)
        {
            makeRequest()
        }

    }
    fun makeRequest()
    {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            CAMERA_REQUEST_CODE ->{
                 if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED )
                 {
                     Toast.makeText(this,"Please enable camera permissions",Toast.LENGTH_LONG).show()
                 }
                else
                 {
                     //success
                 }
            }
        }
    }

}