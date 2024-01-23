package com.essa.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.essa.myapplication.ml.MobilenetV110224Quant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class ModelDisplay extends AppCompatActivity {

    Button selectBtn, predictBtn, captureBtn;
    TextView result;
    ImageView imageView;
    Bitmap bitmap;
    String[] labels = new String[1002];

    private Switch languageSwitch;
    private String[] labelss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_display);

        getPermission();
        loadLabels();

        selectBtn = findViewById(R.id.button2);
        predictBtn = findViewById(R.id.classifyButton);
        captureBtn = findViewById(R.id.button);

        result = findViewById(R.id.result);

        imageView = findViewById(R.id.imageView);

        selectBtn.setOnClickListener(v -> openGallery());

        captureBtn.setOnClickListener(v -> openCamera());

        predictBtn.setOnClickListener(v -> {
            if (bitmap != null) {
                performPrediction();
            }
        });
    }



    private void loadLabels() {
        int cnt = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labelsPL.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                labels[cnt] = line;
                cnt++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem loading labels", e);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 12);
    }

    private void performPrediction() {
        try {
            MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(ModelDisplay.this);

            // Skalowanie i przetwarzanie bitmapy.
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(scaledBitmap);

            // Konwersja TensorImage na ByteBuffer.
            ByteBuffer byteBuffer = tensorImage.getBuffer();

            // Tworzenie danych wejściowych dla modelu.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
            inputFeature0.loadBuffer(byteBuffer);

            // Uruchomienie modelu i otrzymanie wyników.
            MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Interpretacja wyników.
            float[] probabilities = outputFeature0.getFloatArray();
            int maxIndex = getMax(probabilities);
            result.setText(labels[maxIndex]);

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getMax(float[] arr) {
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            maxIndex = arr[i] > arr[maxIndex] ? i : maxIndex;
        }
        return maxIndex;
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11 && grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            getPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException("Problem loading image", e);
            }
        } else if (requestCode == 12 && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}
