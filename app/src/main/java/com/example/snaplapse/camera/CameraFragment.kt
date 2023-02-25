package com.example.snaplapse.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.snaplapse.R
import com.example.snaplapse.databinding.FragmentCameraBinding
import com.example.snaplapse.view_models.CameraViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private lateinit var safeContext: Context

    private lateinit var _binding: FragmentCameraBinding
    private val binding get() = _binding

    private val viewModel: CameraViewModel by activityViewModels()

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            val requestMultiplePermissions = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions.containsValue(false)) {
                    Toast.makeText(safeContext, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                } else {
                    startCamera()
                }
            }
            requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
        }
        binding.shutterButton.setOnClickListener { takePhoto() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        }, ContextCompat.getMainExecutor(safeContext))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(ContextCompat.getMainExecutor(safeContext), object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val buffer = image.planes[0].buffer
                buffer.rewind()
                val bytes = ByteArray(buffer.capacity())
                buffer.get(bytes)
                var imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imageBitmap = Bitmap.createBitmap(
                    imageBitmap,
                    0,
                    0,
                    imageBitmap.width,
                    imageBitmap.height,
                    Matrix().apply { postRotate(image.imageInfo.rotationDegrees.toFloat()) },
                    true
                )
                viewModel.setImageBitmap(imageBitmap)
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentContainerView, PhotoEditFragment())
                transaction.commit()
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {}
        })
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
