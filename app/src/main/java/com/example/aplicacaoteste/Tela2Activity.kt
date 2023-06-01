package com.example.aplicacaoteste

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.aplicacaoteste.databinding.ActivityTela2Binding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private lateinit var binding: ActivityTela2Binding

class Tela2Activity : AppCompatActivity() {

    private lateinit var cameraProviderFuture: ListenableFuture <ProcessCameraProvider>

    private lateinit var cameraSelector: CameraSelector

    private lateinit var imgCaptureExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTela2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        imgCaptureExecutor = Executors.newSingleThreadExecutor() //cuida de uma thread separada para gravar o arquivo

        //metodo startCamera
        startCamera()

        //botão para takephoto
        binding.takephoto.setOnClickListener {
            takePhoto()
        }
    }

    private fun startCamera(){
        cameraProviderFuture.addListener({

            imageCapture = ImageCapture.Builder().build() //programa saber q a imagem vai ser capturada

            val cameraProvider = cameraProviderFuture.get() //referencia
            val preview = Preview.Builder().build().also { //also diz que irá implementar mais uma coisa
                it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }

            try {
                //abrir o preview.
                cameraProvider.unbindAll()
                //atrelar ciclo de vida com a tela
                cameraProvider.bindToLifecycle(this, cameraSelector, preview , imageCapture)



            } catch (e: Exception){
                Log.e("CameraPreview" , "Falha ao abrir a camera")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        //codigo para tirar a foto
        imageCapture?.let {

            //arquivo para gravar a foto.
            val fileName = "FOTO_JPEG_${System.currentTimeMillis()}"
            val file = File( externalMediaDirs[0] , fileName)

            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()

            it.takePicture(
                outputFileOptions,
                imgCaptureExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.i("CameraPreview", "A imagem foi salva no diretório: ${file.toUri()}")
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(binding.root.context , "Erro ao salvar foto" , Toast.LENGTH_LONG).show()
                        Log.e("CameraPreview" , "Excessão ao gravar arquivo da foto: $exception")
                    }

                }

            )
        }
    }
}