package com.example.snaplapse

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel: ViewModel() {
    private val _imageBitmap = MutableLiveData<Bitmap>()
    val imageBitmap: LiveData<Bitmap> get() = _imageBitmap

    private val _photoDescription = MutableLiveData<String>()
    val photoDescription: LiveData<String> get() = _photoDescription

    fun setImageBitmap(imageBitmap: Bitmap) {
        _imageBitmap.value = imageBitmap
    }

    fun setPhotoDescription(photoDescription: String) {
        _photoDescription.value = photoDescription
    }
}