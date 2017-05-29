package in.collectivegood.dbsibycgf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class BarCodeActivity extends AppCompatActivity {

    SurfaceView cameraView;
    BarcodeDetector detector;
    CameraSource cameraSource;
    TextView barCodeValueView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);

        barCodeValueView = (TextView) findViewById(R.id.bar_code_text);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);
        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(480, 480)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                    cameraSource.start(cameraView.getHolder());
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    final String displayValue = barcodes.valueAt(0).displayValue;
                    barCodeValueView.post(new Runnable() {
                        @Override
                        public void run() {
                            barCodeValueView.setText(displayValue);
                        }
                    });
                }
            }

        });

    }
}
