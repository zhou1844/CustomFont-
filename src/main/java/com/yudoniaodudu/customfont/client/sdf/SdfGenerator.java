package com.yudoniaodudu.customfont.client.sdf;

import com.mojang.blaze3d.platform.NativeImage;

import java.util.Arrays;

public final class SdfGenerator {
    private SdfGenerator() {
    }

    public static void convertRegionAlphaToSdfInPlace(NativeImage image, int x0, int y0, int w, int h, int pixelRange) {
        if (w <= 0 || h <= 0) {
            return;
        }
        if (pixelRange <= 0) {
            return;
        }

        byte[] mask = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = image.getPixelRGBA(x0 + x, y0 + y);
                int a = (rgba >>> 24) & 0xFF;
                mask[y * w + x] = (byte) a;
            }
        }

        float[] outsideGrid = new float[w * h];
        float[] insideGrid = new float[w * h];
        float inf = 1.0e20f;
        for (int i = 0; i < mask.length; i++) {
            int a = mask[i] & 0xFF;
            boolean inside = a > 127;
            outsideGrid[i] = inside ? inf : 0.0f;
            insideGrid[i] = inside ? 0.0f : inf;
        }

        float[] distToOutside2 = edt2d(outsideGrid, w, h);
        float[] distToInside2 = edt2d(insideGrid, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                boolean inside = (mask[idx] & 0xFF) > 127;
                float sd = inside ? (float) Math.sqrt(distToOutside2[idx]) : -(float) Math.sqrt(distToInside2[idx]);
                float v = 0.5f + (sd / (float) pixelRange) * 0.5f;
                int outA = clamp255(Math.round(v * 255.0f));
                int rgba = (outA << 24) | 0x00FFFFFF;
                image.setPixelRGBA(x0 + x, y0 + y, rgba);
            }
        }
    }

    public static void convertRegionLuminanceToSdfInPlace(NativeImage image, int x0, int y0, int w, int h, int pixelRange) {
        if (w <= 0 || h <= 0) {
            return;
        }
        if (pixelRange <= 0) {
            return;
        }

        byte[] mask = new byte[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = image.getPixelRGBA(x0 + x, y0 + y);
                int a = (rgba >>> 24) & 0xFF;
                mask[y * w + x] = (byte) a;
            }
        }

        float[] outsideGrid = new float[w * h];
        float[] insideGrid = new float[w * h];
        float inf = 1.0e20f;
        for (int i = 0; i < mask.length; i++) {
            int a = mask[i] & 0xFF;
            boolean inside = a > 127;
            outsideGrid[i] = inside ? inf : 0.0f;
            insideGrid[i] = inside ? 0.0f : inf;
        }

        float[] distToOutside2 = edt2d(outsideGrid, w, h);
        float[] distToInside2 = edt2d(insideGrid, w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int idx = y * w + x;
                boolean inside = (mask[idx] & 0xFF) > 127;
                float sd = inside ? (float) Math.sqrt(distToOutside2[idx]) : -(float) Math.sqrt(distToInside2[idx]);
                float v = 0.5f + (sd / (float) pixelRange) * 0.5f;
                int out = clamp255(Math.round(v * 255.0f));
                int rgba = (0xFF << 24) | (out << 16) | (out << 8) | out;
                image.setPixelRGBA(x0 + x, y0 + y, rgba);
            }
        }
    }

    private static int clamp255(int v) {
        if (v < 0) return 0;
        if (v > 255) return 255;
        return v;
    }

    private static float[] edt2d(float[] grid, int w, int h) {
        float[] tmp = new float[w * h];
        float[] out = new float[w * h];

        float[] f = new float[Math.max(w, h)];
        float[] d = new float[Math.max(w, h)];
        int[] v = new int[Math.max(w, h)];
        float[] z = new float[Math.max(w, h) + 1];

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                f[y] = grid[y * w + x];
            }
            edt1d(f, h, d, v, z);
            for (int y = 0; y < h; y++) {
                tmp[y * w + x] = d[y];
            }
        }

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                f[x] = tmp[y * w + x];
            }
            edt1d(f, w, d, v, z);
            for (int x = 0; x < w; x++) {
                out[y * w + x] = d[x];
            }
        }

        return out;
    }

    private static void edt1d(float[] f, int n, float[] d, int[] v, float[] z) {
        Arrays.fill(d, 0, n, 0.0f);
        int k = 0;
        v[0] = 0;
        z[0] = -Float.MAX_VALUE;
        z[1] = Float.MAX_VALUE;

        for (int q = 1; q < n; q++) {
            float s;
            while (true) {
                int vk = v[k];
                float fk = f[vk];
                float fq = f[q];
                s = ((fq + q * q) - (fk + vk * vk)) / (2.0f * (q - vk));
                if (s > z[k]) {
                    break;
                }
                k--;
            }
            k++;
            v[k] = q;
            z[k] = s;
            z[k + 1] = Float.MAX_VALUE;
        }

        k = 0;
        for (int q = 0; q < n; q++) {
            while (z[k + 1] < q) {
                k++;
            }
            int vk = v[k];
            float dx = q - vk;
            d[q] = dx * dx + f[vk];
        }
    }
}
