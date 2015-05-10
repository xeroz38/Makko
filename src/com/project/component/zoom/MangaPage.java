package com.project.component.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author $Author$
 * @version $Revision$
 */
public class MangaPage
{
    public  String         path;
    public  ImageViewTouch imageView;
    private Context        ctx;
    private boolean        isPageLoaded;

    public MangaPage(Context ctx, String filePath)
    {
        this.ctx = ctx;
        imageView = new ImageViewTouch(ctx);
        imageView.setImageResource(android.R.color.transparent);
        path = filePath;

    }


    public void loadImage()
    {
        if (!isPageLoaded)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmapReset(bitmap, true);
            isPageLoaded = true;

        }
        else
        {
            imageView.resetZoom();
        }

    }

    public void unloadImage()
    {
        if (isPageLoaded)
        {
            imageView.setImageBitmapReset(null, true);
            isPageLoaded = false;

        }
    }

    public void reset()
    {
        imageView.resetZoom();
    }

    public int getImageSize(Bitmap bm)
    {
        return bm.getRowBytes() * bm.getHeight();
    }

    public int getBitmapSize()
    {
        Bitmap bm = imageView.getDisplayBitmap().getBitmap();
        if (bm != null)
        {
            return bm.getRowBytes() * bm.getHeight();
        }
        else
        {
            return 0;
        }
    }
}
