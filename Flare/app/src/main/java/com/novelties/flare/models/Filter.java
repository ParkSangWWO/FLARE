package com.novelties.flare.models;

public class Filter {
    private float brightness;
    private float contrast;
    private float saturation;
    private float hue;
    private float sharpness;
    private float exposure;
    private float gamma;

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
    }

    public float getSharpness() {
        return sharpness;
    }

    public void setSharpness(float sharpness) {
        this.sharpness = sharpness;
    }

    public float getExposure() {
        return exposure;
    }

    public void setExposure(float exposure) {
        this.exposure = exposure;
    }

    public float getGamma() {
        return gamma;
    }

    public void setGamma(float blur) {
        this.gamma = blur;
    }

    @Override
    public String toString() {
        return String.format("%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f", brightness, contrast, saturation, hue, sharpness, exposure, gamma);
    }

    public static Filter fromString(String filterString) {
        String[] values = filterString.split(",");

        Filter filter = new Filter();
        filter.setBrightness(Float.parseFloat(values[0]));
        filter.setContrast(Float.parseFloat(values[1]));
        filter.setSaturation(Float.parseFloat(values[2]));
        filter.setHue(Float.parseFloat(values[3]));
        filter.setSharpness(Float.parseFloat(values[4]));
        filter.setExposure(Float.parseFloat(values[5]));
        filter.setGamma(Float.parseFloat(values[6]));

        return filter;
    }
}
