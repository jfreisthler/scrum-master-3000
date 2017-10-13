/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.face.multitracker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.samples.vision.face.multitracker.ui.camera.GraphicOverlay;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new face.  The
 * multi-processor uses this factory to create face trackers as needed -- one for each individual.
 */
class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
    private GraphicOverlay mGraphicOverlay;

    FaceTrackerFactory(GraphicOverlay graphicOverlay) {
        mGraphicOverlay = graphicOverlay;
    }

    @Override
    public Tracker<Face> create(Face face) {
        FaceGraphic graphic = new FaceGraphic(mGraphicOverlay);
        return new GraphicTracker<>(mGraphicOverlay, graphic);
    }
}

/**
 * Graphic instance for rendering face position, size, and ID within an associated graphic overlay
 * view.
 */
class FaceGraphic extends TrackedGraphic<Face> {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float JIRA_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    //private static final int COLOR_CHOICES[] = {
    //Color.GREEN
    //Color.MAGENTA,
    //Color.RED,
    //Color.YELLOW
    //};
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;
    private Paint mTextPaint;

    private volatile Face mFace;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        addAttribute("id", getUserID());
        addAttribute("inProcessCount", String.valueOf(getInProcessCount()));
        addAttribute("status", String.valueOf(getStatus()));

        //mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int selectedColor = getStatusColor(Integer.parseInt(getAttribute("status"))); //COLOR_CHOICES[mCurrentColorIndex];

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setColor(selectedColor);

        mIdPaint = new Paint();
        mIdPaint.setColor(selectedColor);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(JIRA_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setColor(selectedColor);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);


    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateItem(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position, size, and ID on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // Draws a circle at the position of the detected face, with the face's track id below.
        float cx = translateX(face.getPosition().x + face.getWidth() / 2);
        float cy = translateY(face.getPosition().y + face.getHeight() / 2);
        //canvas.drawCircle(cx, cy, FACE_POSITION_RADIUS, mFacePositionPaint);
        //canvas.drawText("id: " + getId(), cx + ID_X_OFFSET, cy + ID_Y_OFFSET, mIdPaint);

        // Draws an oval around the face.
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = cx - xOffset;
        float top = cy - yOffset;
        float right = cx + xOffset;
        float bottom = cy + yOffset;
        //canvas.drawOval(left, top, right, bottom, mBoxPaint);
        //canvas.drawRect(right+2, top, right + xOffset, bottom, mBoxPaint);

        drawJiraInfoBox(canvas, left, top, right, bottom);
    }

    private void drawJiraInfoBox(Canvas canvas, float left, float top, float right, float bottom) {
        mBoundingRect = new Rect((int) left, (int) top, (int) right, (int) bottom);

        Paint mJiraPaint = new Paint(mBoxPaint);
        canvas.drawRect(mBoundingRect, mJiraPaint);
        mJiraPaint.setStyle(Style.FILL);
        canvas.drawRect(left, top - 120f, right, top, mJiraPaint);

        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(getAttribute("id") + ": " + getAttribute("inProcessCount") + " IP", left + 4, top - 110f + JIRA_TEXT_SIZE, mTextPaint);
    }

    private String getUserID() {
        return "FRE955";
    }

    private int getInProcessCount() {
        return 2;
    }

    private int getStatus() {
        return 1;
    }

    private int getStatusColor(int status) {
        int result = 0;
        switch (status) {
            case 1:
                result = Color.GREEN;
                break;
            case 2:
                result = Color.RED;
                break;
            case 3:
                result = Color.YELLOW;
                break;
            default:
                result = Color.GREEN;
        }
        return result;
    }
}
