package com.essa.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ModelDisplay extends AppCompatActivity {

    private static final String TAG = "ModelDisplay";
    private static final int imageSize = 224; // Upewnij się, że odpowiada rozmiarowi modelu
    private Button camera, gallery, classifyButton;
    private ImageView imageView;
    private TextView result;
    private List<String> class_names = new ArrayList<>();
    private Bitmap currentImage;
    private Interpreter tflite;

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    currentImage = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(currentImage);
                }
            });

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                try {
                    currentImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    imageView.setImageBitmap(currentImage);
                } catch (IOException e) {
                    Log.e(TAG, "Error opening image from gallery", e);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_display);

        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);
        classifyButton = findViewById(R.id.classifyButton);
        imageView = findViewById(R.id.imageView);
        result = findViewById(R.id.result);

        loadClassNames();
        if (loadTFLiteModel()) {
            camera.setOnClickListener(view -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraLauncher.launch(cameraIntent);
                }
            });

            gallery.setOnClickListener(view -> galleryLauncher.launch("image/*"));

            classifyButton.setOnClickListener(view -> {
                if (currentImage != null) {
                    classifyImage(currentImage);
                } else {
                    result.setText("No image selected");
                }
            });
        } else {
            Log.e(TAG, "Error loading TFLite model");
        }
    }

    private void loadClassNames() {
        try {
            InputStream is = getAssets().open("class_names.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                class_names.add(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading class names file", e);
        }
    }

    private boolean loadTFLiteModel() {
        try {
            ByteBuffer buffer = loadModelFile();
            if (buffer != null) {
                tflite = new Interpreter(buffer);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading TFLite model", e);
            return false;
        }
    }

    private ByteBuffer loadModelFile() {
        try {
            InputStream is = this.getAssets().open("model.tflite");
            byte[] model = new byte[is.available()];
            is.read(model);
            ByteBuffer buffer = ByteBuffer.allocateDirect(model.length);
            buffer.order(ByteOrder.nativeOrder());
            buffer.put(model);
            buffer.rewind();
            is.close();
            return buffer;
        } catch (IOException e) {
            Log.e(TAG, "Error opening model file", e);
            return null;
        }
    }

    private void classifyImage(Bitmap image) {
        Bitmap scaledImage = scaleImage(image);
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(scaledImage);
        float[][] outputVal = new float[1][class_names.size()];
        tflite.run(inputBuffer, outputVal);
        displayResult(outputVal);
    }

    private Bitmap scaleImage(Bitmap image) {
        return Bitmap.createScaledBitmap(image, imageSize, imageSize, true);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * 3 * 4); // Rozmiar * 4 (float)
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[imageSize * imageSize];

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, true);
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {
                final int val = intValues[i * imageSize + j];
                byteBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
                byteBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
                byteBuffer.putFloat((val & 0xFF) / 255.0f);
            }
        }
        return byteBuffer;
    }

    private void displayResult(float[][] resultArray) {
        int maxIndex = -1;
        float maxConfidence = 0;
        for (int i = 0; i < class_names.size(); i++) {
            if (resultArray[0][i] > maxConfidence) {
                maxConfidence = resultArray[0][i];
                maxIndex = i;
            }
        }
        String className = maxIndex >= 0 ? class_names.get(maxIndex) : "Unknown";
        result.setText(className);
    }
}
