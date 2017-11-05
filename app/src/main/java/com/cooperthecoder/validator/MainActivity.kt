package com.cooperthecoder.validator

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import io.fotoapparat.Fotoapparat
import io.fotoapparat.FotoapparatSwitcher
import io.fotoapparat.facedetector.processor.FaceDetectorProcessor
import io.fotoapparat.facedetector.view.RectanglesView
import io.fotoapparat.parameter.LensPosition
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.parameter.selector.FocusModeSelectors
import io.fotoapparat.parameter.selector.LensPositionSelectors
import io.fotoapparat.parameter.selector.Selectors
import io.fotoapparat.photo.BitmapPhoto
import io.fotoapparat.view.CameraView
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var fotoapparatSwitcher: FotoapparatSwitcher
    private lateinit var frontFotoapparat: Fotoapparat
    private lateinit var backFotoapparat: Fotoapparat
    private lateinit var takePictureButton: AppCompatImageButton
    private lateinit var progressBar: ProgressBar

    private val cameraPermissionsDelegate = PermissionsDelegate(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cameraView = findViewById(R.id.camera_view) as CameraView
        val faceRectangleView = findViewById(R.id.face_rectangle) as RectanglesView
        takePictureButton = findViewById(R.id.take_picture_button) as AppCompatImageButton
        progressBar = findViewById(R.id.progress_bar) as ProgressBar
        val switchButton = findViewById(R.id.switch_button) as AppCompatImageButton

        cameraView.setOnClickListener {
            fotoapparatSwitcher.currentFotoapparat.focus()
        }

        takePictureButton.setOnClickListener {
            showLoading(true)
        }

        switchButton.setOnClickListener {
            switchCameras()
        }

        val faceDetector = FaceDetectorProcessor.with(this)
                .listener {
                    faceRectangleView.setRectangles(it)
                }
                .build()

        if (!cameraPermissionsDelegate.hasCameraPermission()) {
            cameraPermissionsDelegate.requestCameraPermission()
        }
        backFotoapparat = createFotoapparat(cameraView, faceDetector, LensPosition.BACK)
        frontFotoapparat = createFotoapparat(cameraView, faceDetector, LensPosition.FRONT)
        fotoapparatSwitcher = FotoapparatSwitcher.withDefault(backFotoapparat)
    }


    override fun onStart() {
        super.onStart()
        if (cameraPermissionsDelegate.hasCameraPermission()) {
            fotoapparatSwitcher.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (cameraPermissionsDelegate.hasCameraPermission()) {
            fotoapparatSwitcher.stop()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (cameraPermissionsDelegate.resultGranted(requestCode, permissions, grantResults)) {
            fotoapparatSwitcher.currentFotoapparat.start()
        }
    }

    private fun canSwitchCameras(): Boolean {
        return frontFotoapparat.isAvailable == backFotoapparat.isAvailable
    }

    private fun switchCameras() {
        if (canSwitchCameras()) {
            if (fotoapparatSwitcher.currentFotoapparat == frontFotoapparat) {
                fotoapparatSwitcher.switchTo(backFotoapparat)
            } else {
                fotoapparatSwitcher.switchTo(frontFotoapparat)
            }
        } else {
            Toast.makeText(this, getString(R.string.cannot_switch_cameras), Toast.LENGTH_LONG).show()
        }
    }


    private fun takePicture(): Single<BitmapPhoto> {
        return fotoapparatSwitcher.currentFotoapparat.takePicture()
                .toBitmap()
                .adapt(SingleAdapter.toSingle())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
    }


    private fun rotateAngle(): Float {
        return if (fotoapparatSwitcher.currentFotoapparat == frontFotoapparat) {
            -90F
        } else {
            90F
        }
    }

    private fun showLoading(loading: Boolean) {
        takePictureButton.isClickable = !loading
        if (loading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showDialog(dialogText: String, title: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
                .setMessage(dialogText)
                .setTitle(title)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun createFotoapparat(cameraView: CameraView, faceDetector: FaceDetectorProcessor, lensPosition: LensPosition): Fotoapparat {
        return Fotoapparat.with(this)
                .into(cameraView)
                .lensPosition(LensPositionSelectors.lensPosition(LensPosition.BACK))
                .focusMode(Selectors.firstAvailable(
                        FocusModeSelectors.autoFocus(),
                        FocusModeSelectors.continuousFocus(),
                        FocusModeSelectors.fixed()
                ))
                .cameraErrorCallback { error ->
                    error.printStackTrace()
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_LONG).show()
                }
                .lensPosition(LensPositionSelectors.lensPosition(lensPosition))
                .frameProcessor(faceDetector)
                .previewScaleType(ScaleType.CENTER_CROP)
                .build()
    }

}

