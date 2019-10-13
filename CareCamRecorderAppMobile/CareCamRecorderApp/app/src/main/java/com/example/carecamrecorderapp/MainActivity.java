package com.example.carecamrecorderapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

 */

import static java.nio.charset.StandardCharsets.UTF_8;

//import android.media.Image;


public class MainActivity extends AppCompatActivity {

    //Declare Variables
    Button btnRecord, btnStopRecord, btnPlay, btnStop, btnFile1, btnFile2, btnFile3, btnReset;
    Integer profile = 1;
    String pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
/*
    // Get a non-default Storage bucket
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://carecam-593ba.appspot.com");
    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    // Create a reference to "mountains.jpg"
    StorageReference mountainsRef = storageRef.child("testImage.jpg");

    // Create a reference to 'images/mountains.jpg'
    //StorageReference mountainImagesRef = storageRef.child("C:\\Users\\Donavan See\\AndroidStudioProjects\\CareCamRecorderApp\\testImage.jpg");
    /*
    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
    StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
    UploadTask uploadTask = mountainsRef.putBytes(data);

     */



// While the file names are the same, the references point to different files
// mountainsRef.getName().equals(mountainImagesRef.getName());    // true
//mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
    /*
    private FirebaseAuth mAuth;
    mAuth = FirebaseAuth.getInstance();

    FileInputStream serviceAccount = new FileInputStream("C:\Users\Donavan See\AndroidStudioProjects\CareCamRecorderApp\carecam-593ba-firebase-adminsdk-gr35d-a83ad42f4e.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("<BUCKET_NAME>.appspot.com")
            .build();
    FirebaseApp.initializeApp(options);

    Bucket bucket = StorageClient.getInstance().bucket();

     */

// 'bucket' is an object defined in the google-cloud-storage Java library.
// See http://googlecloudplatform.github.io/google-cloud-java/latest/apidocs/com/google/cloud/storage/Bucket.html
// for more details.

    /*
    Storage storage = StorageOptions.getDefaultInstance().getService();
    BlobId blobId = BlobId.of("carecam-593ba.appspot.com", "blob_carecam");
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
    Blob blob = storage.create(blobInfo, "Hello, Cloud Storage!".getBytes(UTF_8));
     */

    final int REQUEST_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnRecord = (Button)findViewById(R.id.btnStartRecord);
        btnStop = (Button)findViewById(R.id.btnStop);
        btnStopRecord = (Button)findViewById(R.id.btnStopRecord);
        btnFile1 = (Button)findViewById(R.id.btnFile1);
        btnFile1.setBackgroundColor(Color.GREEN);
        btnFile2 = (Button)findViewById(R.id.btnFile2);
        btnFile3 = (Button)findViewById(R.id.btnFile3);
        btnReset = (Button)findViewById(R.id.btnReset);
        //detectFaces("testImage.jpg", System.out);


        if(checkPermissionFromDevice())
        {
            btnReset.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    btnStopRecord.setEnabled(true);
                    btnPlay.setEnabled(true);
                    btnRecord.setEnabled(true);
                    btnStop.setEnabled(true);
                    profile = 1;
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";
                    btnFile1.setBackgroundColor(Color.GREEN);
                    btnFile2.setBackgroundColor(Color.BLACK);
                    btnFile3.setBackgroundColor(Color.BLACK);
                    for(int i=1; i<=3; i++){
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+i+"_audio_record.3gp");
                        Toast.makeText(MainActivity.this, "I made it to"+i, Toast.LENGTH_SHORT).show();
                        file.delete();
                    }
                }
            });

            btnFile1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    profile = 1;
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";
                    Toast.makeText(MainActivity.this, pathSave.toString(), Toast.LENGTH_SHORT).show();
                    btnFile1.setBackgroundColor(Color.GREEN);
                    btnFile2.setBackgroundColor(Color.BLACK);
                    btnFile3.setBackgroundColor(Color.BLACK);
                }
            });

            btnFile2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    profile = 2;
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";
                    Toast.makeText(MainActivity.this, pathSave.toString(), Toast.LENGTH_SHORT).show();
                    btnFile2.setBackgroundColor(Color.GREEN);
                    btnFile1.setBackgroundColor(Color.BLACK);
                    btnFile3.setBackgroundColor(Color.BLACK);
                }
            });

            btnFile3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    profile = 3;
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";
                    Toast.makeText(MainActivity.this, pathSave.toString(), Toast.LENGTH_SHORT).show();
                    btnFile3.setBackgroundColor(Color.GREEN);
                    btnFile2.setBackgroundColor(Color.BLACK);
                    btnFile1.setBackgroundColor(Color.BLACK);
                }
            });

            btnRecord.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){

                    btnPlay.setEnabled(false);
                    btnStop.setEnabled(false);
                    btnStopRecord.setEnabled(true);

                    //pathSave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ profile.toString()+"_audio_record.3gp";
                    setupMediaRecorder();

                    try{

                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e){
                        e.printStackTrace();
                    }


                    Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                }
            });

            btnStopRecord.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    mediaRecorder.stop();
                    btnStopRecord.setEnabled(false);
                    btnPlay.setEnabled(true);
                    btnRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                }
            });

            btnPlay.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    btnStop.setEnabled(true);
                    btnPlay.setEnabled(false);
                    btnStopRecord.setEnabled(false);
                    btnRecord.setEnabled(false);

                    mediaPlayer = new MediaPlayer();
                    try{
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    btnStopRecord.setEnabled(false);
                    btnRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnPlay.setEnabled(true);

                    if(mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }
                }
            });

        } else {
            requestPermission();

        }
    }

    private void setupMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED && record_audio_result == PackageManager.PERMISSION_GRANTED;
    }
/*
    public static void detectFaces(String filePath, PrintStream out){
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = null;
        try {
            imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        }
        catch (Exception e){
            out.println("Exception thrown in detectFaces. Error: " + e);
        }
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.FACE_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
                    out.printf(
                            "anger: %s\njoy: %s\nsurprise: %s\nposition: %s",
                            annotation.getAngerLikelihood(),
                            annotation.getJoyLikelihood(),
                            annotation.getSurpriseLikelihood(),
                            annotation.getBoundingPoly());
                }
            }
        }
        catch (IOException i){
            out.println("Exception thrown in detectFaces. Error: " + i);
        }
    }

 */
}
