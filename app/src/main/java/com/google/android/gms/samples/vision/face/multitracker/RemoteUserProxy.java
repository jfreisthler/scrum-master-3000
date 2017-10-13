package com.google.android.gms.samples.vision.face.multitracker;

import android.graphics.drawable.Drawable;

import com.google.android.gms.samples.vision.face.multitracker.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Face;
import android.graphics.Bitmap;
/**
 * Created by fre955 on 10/12/17.
 */

public class RemoteUserProxy {
    public Drawable proxyImage = null;
    public int x;
    public int y;
    public int top;
    public int bottom;
    public Bitmap mAvatarImage;

    RemoteUserProxy(Bitmap avatarImage){
        mAvatarImage = avatarImage;
    }
    public float getWidth(){
        return 0;
    }
    public float getHeight(){
        return 0;
    }
}
