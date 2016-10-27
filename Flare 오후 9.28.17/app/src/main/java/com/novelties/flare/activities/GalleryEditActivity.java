package com.novelties.flare.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.novelties.flare.R;
import com.novelties.flare.models.Filter;

import java.io.IOException;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.Rotation;

public class GalleryEditActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ImageView ivBrightness;
    private ImageView ivContrast;
    private ImageView ivSaturation;
    private ImageView ivHue;
    private ImageView ivSharpness;
    private ImageView ivExposure;
    private ImageView ivGamma;

    private SeekBar sbBrightness;
    private SeekBar sbContrast;
    private SeekBar sbSaturation;
    private SeekBar sbHue;
    private SeekBar sbSharpness;
    private SeekBar sbExposure;
    private SeekBar sbGamma;

    private ImageView[] imageViews;
    private SeekBar[] seekBars;

    private ImageView btnShare;
    private ImageView btnRefresh;
    private ImageView btnDownload;
    private ImageView btnDone;
    private ImageView btnRotate;

    private GPUImage gpuImage;
    private GPUImageBrightnessFilter brightness = new GPUImageBrightnessFilter();
    private GPUImageContrastFilter contrast = new GPUImageContrastFilter();
    private GPUImageSaturationFilter saturation = new GPUImageSaturationFilter();
    private GPUImageHueFilter hue = new GPUImageHueFilter();
    private GPUImageSharpenFilter sharpen = new GPUImageSharpenFilter();
    private GPUImageExposureFilter exposure = new GPUImageExposureFilter();
    private GPUImageGammaFilter gamma = new GPUImageGammaFilter();
    private GPUImageFilterGroup filterGroup;

    private Filter filter = new Filter();

    private GLSurfaceView surfaceView;

    private int CODE_ROTATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_edit);

        initView();
        initEvent();
        initFilters();
        setSeekbars();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(GalleryEditActivity.this, EditActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        ivBrightness = (ImageView) findViewById(R.id.iv_brightness);
        ivContrast = (ImageView) findViewById(R.id.iv_contrast);
        ivSaturation = (ImageView) findViewById(R.id.iv_saturation);
        ivHue = (ImageView) findViewById(R.id.iv_hue);
        ivSharpness = (ImageView) findViewById(R.id.iv_sharpness);
        ivExposure = (ImageView) findViewById(R.id.iv_exposure);
        ivGamma = (ImageView) findViewById(R.id.iv_gamma);

        sbBrightness = (SeekBar) findViewById(R.id.sb_brightness);
        sbContrast = (SeekBar) findViewById(R.id.sb_contrast);
        sbSaturation = (SeekBar) findViewById(R.id.sb_saturation);
        sbHue = (SeekBar) findViewById(R.id.sb_hue);
        sbSharpness = (SeekBar) findViewById(R.id.sb_sharpness);
        sbExposure = (SeekBar) findViewById(R.id.sb_exposure);
        sbGamma = (SeekBar) findViewById(R.id.sb_gamma);

        imageViews = new ImageView[] {ivBrightness, ivContrast, ivSaturation, ivHue, ivSharpness, ivExposure, ivGamma};
        seekBars = new SeekBar[] {sbBrightness, sbContrast, sbSaturation, sbHue, sbSharpness, sbExposure, sbGamma};

        btnShare = (ImageView) findViewById(R.id.btn_share);
        btnRefresh = (ImageView) findViewById(R.id.btn_refresh);
        btnDownload = (ImageView) findViewById(R.id.btn_download);
        btnDone = (ImageView) findViewById(R.id.btn_done);
        btnRotate = (ImageView) findViewById(R.id.btn_rotate);

        surfaceView = (GLSurfaceView) findViewById(R.id.surface_view);

        gpuImage = new GPUImage(this);
        gpuImage.setGLSurfaceView(surfaceView);

        Uri uri = getIntent().getParcelableExtra("data");
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadImage(bitmap);
    }

    private void initEvent() {
        for (int i = 0 ; i < imageViews.length ; i++) {
            ImageView iv = imageViews[i];
            final int index = i;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshSeekbarVisibility(seekBars[index]);
                }
            });
        }

        for (SeekBar seekBar : seekBars) {
            seekBar.setOnSeekBarChangeListener(this);
        }

        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CODE_ROTATE = (CODE_ROTATE + 90) % 360;
                gpuImage.setRotation(Rotation.fromInt(CODE_ROTATE));
                gpuImage.requestRender();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = Base64.encodeToString(filter.toString().getBytes(), Base64.DEFAULT).trim();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, "필터 공유"));

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSeekbars();
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title("코드 입력")
                        .content("친구가 보내준 코드를 입력해주세요.")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("코드", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            }
                        })
                        .positiveText("확인")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                EditText field = dialog.getInputEditText();
                                if (field == null) {
                                    return;
                                }
                                String input = field.getText().toString();
                                if (input.length() == 0) {
                                    Toast.makeText(GalleryEditActivity.this, "코드를 입력해주세요!", Toast.LENGTH_SHORT).show();
                                } else {
                                    dialog.dismiss();
                                    decodeFilter(input);
                                }
                            }
                        })
                        .negativeText("취소")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpuImage.saveToPictures("flare", System.currentTimeMillis() + ".jpg", new GPUImage.OnPictureSavedListener() {
                    @Override
                    public void onPictureSaved(Uri uri) {
                        Toast.makeText(GalleryEditActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GalleryEditActivity.this, EditActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        });
    }

    private void decodeFilter(String encoded) {
        String decoded = new String(Base64.decode(encoded, Base64.DEFAULT));
        filter = Filter.fromString(decoded);

        refreshFilterValues();
    }

    private void refreshFilterValues() {
        float brightness = filter.getBrightness();
        sbBrightness.setProgress((int) (brightness * 100 + 100));

        float contrast = filter.getContrast();
        sbContrast.setProgress((int) (contrast * 100));

        float saturation = filter.getSaturation();
        sbSaturation.setProgress((int) (saturation * 100));

        float hue = filter.getHue();
        sbHue.setProgress((int) hue);

        float sharpness = filter.getSharpness();
        sbSharpness.setProgress((int) ((sharpness*100)+400));

        float exposure = filter.getExposure();
        sbExposure.setProgress((int) ((exposure*100)+100));

        float gamma = filter.getGamma();
        sbGamma.setProgress((int) (gamma*100));
    }

    private void refreshSeekbarVisibility(SeekBar toShow) {
        int originVisibility = toShow.getVisibility();

        for (SeekBar seekBar : seekBars) {
            seekBar.setVisibility(View.GONE);
        }

        if (originVisibility != View.VISIBLE) {
            toShow.setVisibility(View.VISIBLE);
        }
    }

    private void initFilters() {
        filterGroup = new GPUImageFilterGroup();
        filterGroup.addFilter(brightness);
        filterGroup.addFilter(contrast);
        filterGroup.addFilter(saturation);
        filterGroup.addFilter(hue);
        filterGroup.addFilter(sharpen);
        filterGroup.addFilter(exposure);
        filterGroup.addFilter(gamma);
        gpuImage.setFilter(filterGroup);
    }

    private void loadImage(Bitmap bitmap) {
        gpuImage.setImage(bitmap);
        surfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_brightness:
                float brightnessValue = (progress-100)/100.0f;
                brightness.setBrightness(brightnessValue);
                filter.setBrightness(brightnessValue);
                break;

            case R.id.sb_contrast:
                float contrastValue = progress/100.0f;
                contrast.setContrast(contrastValue);
                filter.setContrast(contrastValue);
                break;

            case R.id.sb_saturation:
                float saturationValue = progress/100.0f;
                saturation.setSaturation(saturationValue);
                filter.setSaturation(saturationValue);
                break;

            case R.id.sb_hue:
                float hueValue = progress;
                hue.setHue(hueValue);
                filter.setHue(hueValue);
                break;

            case R.id.sb_sharpness:
                float sharpnessValue = (progress-400)/100.0f;
                sharpen.setSharpness(sharpnessValue);
                filter.setSharpness(sharpnessValue);
                break;

            case R.id.sb_exposure:
                float exposureValue = (progress-100)/100.0f;
                exposure.setExposure(exposureValue);
                filter.setExposure(exposureValue);
                break;

            case R.id.sb_gamma:
                float gammaValue = progress/100.0f;
                gamma.setGamma(gammaValue);
                filter.setGamma(gammaValue);
                break;

        }

        gpuImage.requestRender();
    }

    private void setSeekbars(){
        sbBrightness.setMax(200);
        sbBrightness.setProgress(100);

        sbContrast.setMax(400);
        sbContrast.setProgress(100);

        sbSaturation.setMax(200);
        sbSaturation.setProgress(100);

        sbHue.setMax(360);
        sbHue.setProgress(0);

        sbSharpness.setMax(800);
        sbSharpness.setProgress(400);

        sbExposure.setMax(200);
        sbExposure.setProgress(100);

        sbGamma.setMax(300);
        sbGamma.setProgress(100);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
