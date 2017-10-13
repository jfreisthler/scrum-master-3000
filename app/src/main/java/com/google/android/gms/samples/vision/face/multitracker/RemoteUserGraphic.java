package com.google.android.gms.samples.vision.face.multitracker;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Bitmap;
import android.graphics.Point;
import com.google.android.gms.samples.vision.face.multitracker.ui.camera.GraphicOverlay;

/**
 * Graphic instance for rendering remote user within an associated graphic overlay
 * view.
 */
class RemoteUserGraphic extends GraphicOverlay.Graphic {
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float JIRA_TEXT_SIZE = 40.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;
    private Paint mTextPaint;
    private Point mCenterPoint = null;

    private volatile RemoteUserProxy mRemoteUser;

    RemoteUserGraphic(RemoteUserProxy remoteUser, GraphicOverlay overlay, int x, int y) {
        super(overlay);
        mRemoteUser = remoteUser;
        mCenterPoint = new Point(x,y);

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
     * Draws the face annotations for position, size, and ID on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        RemoteUserProxy remoteUser = mRemoteUser;
        if (remoteUser == null) {
            return;
        }

        float imageWidth = mRemoteUser.mAvatarImage.getWidth();
        float imageHeight = mRemoteUser.mAvatarImage.getHeight();
        float cx = mCenterPoint.x - imageWidth / 2;
        float cy = mCenterPoint.y - imageHeight/2;

        float xOffset = 360; //Ideally based on knowing how to identify the face of an avatar, but cheating here.
        float yOffset = 180;

        canvas.drawBitmap(mRemoteUser.mAvatarImage, cx, cy, null);
        drawJiraInfoBox(canvas, cx,cy, cx + xOffset, cy + yOffset);

    }

    private void drawJiraInfoBox(Canvas canvas, float left, float top, float right, float bottom) {
        mBoundingRect = new Rect((int) left, (int) top, (int) right, (int) bottom);

        Paint mJiraPaint = new Paint(mBoxPaint);
        canvas.drawRect(mBoundingRect, mJiraPaint);
        mJiraPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top - 120f, right, top, mJiraPaint);

        mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(getAttribute("id") + ": " + getAttribute("inProcessCount") + " IP", left + 4, top - 110f + JIRA_TEXT_SIZE, mTextPaint);
    }

    private String getUserID() {
        return "Sean";
    }

    private int getInProcessCount() {
        return 2;
    }

    private int getStatus() {
        return 2;
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
