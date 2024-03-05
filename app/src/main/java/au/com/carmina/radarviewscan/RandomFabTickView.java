package au.com.carmina.radarviewscan;


import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class RandomFabTickView extends FrameLayout
        implements
        ViewTreeObserver.OnGlobalLayoutListener
{

    private static final String tag = RandomFabTickView.class.getSimpleName();
    private DownloadManager.Request request;
    public static final int MAX = 5;
    private static final int IDX_X = 0;
    private static final int IDX_Y = 1;
    private static final int IDX_RIPPLE_LENGTH = 2;
    private static final int IDX_DIS_Y = 3;
    private static final int TEXT_SIZE = 12;

    private Random random;
    private Vector<String> vecKeywords;
    private int width;
    private int height;
    private int fontColor = 0xff0000ff;
    private int shadowColor = 0xdd696969;

    private int rippleX = 0;
    private int rippleY = 0;
    private Handler handler;
    private int deviceCnt = 0;
    private int clickCnt = 0;
    View view = null;

    private int reduceDist = 60;
    private int centerDiff = 75;
    private int xLeftCenter = 0;
    private int xRightCenter = 0;
    private int yTopCenter = 0;
    private int yBottomCenter = 0;
    private int xStart = 0;
    private int xEnd = 0;
    private int yStart = 0;
    private int yEnd = 0;
    private int calcSeries = 0;
    private int imageSize = 0;
    private boolean isSelected = false;
    private HashMap<Integer, RipplePosition> ripplePositions = new HashMap<>();
    private ArrayList<Integer> lockedPosition = new ArrayList<>();
    //private boolean added = false;

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

   /* public interface OnRippleViewClickListener
    {
        void onRippleViewClicked(RippleModel rippleModel);
        void onRippleViewRemoved(RippleModel rippleModel);
    }*/

  //  private OnRippleViewClickListener onRippleOutViewClickListener;

    public RandomFabTickView(Context context)
    {
        super(context);
        init(null, context);
    }

    public RandomFabTickView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, context);
    }

    public RandomFabTickView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @TargetApi(21)
    public RandomFabTickView(Context context, AttributeSet attrs, int defStyleAttr,
                             int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

   /*public void setMode(int mode)
    {
        this.mode = mode;
    }
*/


    public class RipplePosition
    {
        int xpos =0;
        int ypos =0;
        boolean occupied = false;
    }

    public void clear()
    {
       // myImageList.clear();
        ripplePositions.clear();
        lockedPosition.clear();
        deviceCnt = 0;
        clickCnt = 0;
        //Util.CONNECTEDUSERDETAILS.clear();
    }




    public void addKeyWord(String keyword)
    {
        if (vecKeywords.size() < MAX)
        {
            if (!vecKeywords.contains(keyword))
                vecKeywords.add(keyword);
        }
    }

    public Vector<String> getKeyWords()
    {
        return vecKeywords;
    }

    public void removeKeyWord(String keyword)
    {
        if (vecKeywords.contains(keyword))
        {
            vecKeywords.remove(keyword);
        }
    }

    private void init(AttributeSet attrs, Context context)
    {
        random = new Random();
        this.handler = new Handler();
        vecKeywords = new Vector<String>(MAX);
     //   myImageList = new Vector<RippleModel>(MAX);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        deviceCnt = 0;
        clickCnt = 0;
        calcGridPositions();
     //   Provider.getInstance().register(this);
        /*LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.ticklayout, null);
        this.addView(view);*/
    }

    /*private synchronized void updateConnectedUserList()
    {
        try {

                Iterator<RippleModel> resultiter = myImageList.iterator();
                while (resultiter.hasNext()) {
                    RippleModel model = resultiter.next();
                    if (resp.contains(model.getIp())) {
                        model.parseUserResponse(resp);
                    }
                }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }*/


    @Override
    public void onGlobalLayout()
    {
        int tmpW = getWidth();
        int tmpH = getHeight();
        if (width != tmpW || height != tmpH)
        {
            width = tmpW;
            height = tmpH;
            //Log.d(tag, "RandomImageView width = " + width + "; height = " + height);
        }
    }

    public void clearView()
    {
        this.removeAllViews();
    }
    private String bssid;

    public String getBssid()
    {
        if(bssid==null)
            bssid = "";
        return bssid;
    }




    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void calcGridPositions()
    {
        int width = 220;
        int height = 200;
        int posX = 0;
        int posY = 0;
        int count=0;
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                posX = 60 + i * width;
                posY = 20 + j * height;
                Log.d("position", "count - "+count+" xpos - " + posX + " ypos - " + posY);
                RipplePosition posObj = new RipplePosition();
                posObj.xpos = posX;//GetDipsFromPixel(posX);
                posObj.ypos = posY;//GetDipsFromPixel(posY);
                count++;
                ripplePositions.put(count, posObj);
            }
        }
    }

    private RipplePosition getRandomPosition()
    {
        int pos = 0;
        if(ripplePositions.size()==0)
        {
            calcGridPositions();
            lockedPosition.clear();
        }
        if(ripplePositions.size()>0) {
            pos = getRandomNum();
            Log.d("getRandomPosition", "random pos - " + pos);
            if (ripplePositions.get(pos) != null) {
                if (ripplePositions.get(pos).occupied) {
                    Log.d("getRandomPosition", "is occupied - " + pos);
                    if(lockedPosition.size()<9)
                    {
                        Log.d("getRandomPosition", "calling recursively " + pos);
                        getRandomPosition();
                    }
                }
            }
            if(ripplePositions.get(pos) != null)
            {
                ripplePositions.get(pos).occupied = true;
                if(!lockedPosition.contains(pos))
                {
                    Log.d("getRandomPosition", "added pos - " + pos);
                    lockedPosition.add(pos);
                    Log.d("getRandomPosition", "lockedPosition  - " + lockedPosition);
                }
            }
        }
        Log.d("getRandomPosition", "final postion - " + pos);
        return ripplePositions.get(lockedPosition.get(lockedPosition.size()-1));
    }

    private int getRandomNum()
    {
        Random r = new Random();
        return r.nextInt(ripplePositions.size()-1)+1;
    }

    private int getRandomX()
    {
        int min = 0;
        int max = 0;
        int randomX = 0;
        Random r = new Random();

        if(calcSeries == 0 || calcSeries == 3)
        {
            min = xStart;
            max = xLeftCenter;
            randomX = r.nextInt(max - min + 1) + min;
        }
        else if(calcSeries == 1 || calcSeries == 2)
        {
            min = xRightCenter;
            max = xEnd;
            randomX = r.nextInt(max - min + 1) + min;
        }

        return randomX;
    }

    private int getRandomY()
    {
        int min = 0;
        int max = 0;
        int randomY = 0;
        Random r = new Random();

        if(calcSeries == 0 || calcSeries == 1)
        {
            min = yStart;
            max = yTopCenter;
            randomY = r.nextInt(max - min + 1) + min;
        }
        else if(calcSeries == 2 || calcSeries == 3)
        {
            min = yBottomCenter;
            max = yEnd;
            randomY = r.nextInt(max - min + 1) + min;
        }

        return randomY;
    }

    private void calculateDiff(int xCenter, int yCenter)
    {
        Log.d("  xCenter  ", String.valueOf(xCenter)+"  yCenter  "+ String.valueOf(yCenter)+"  centerDiff "+ String.valueOf(centerDiff));

        xStart = 10;
        xEnd = (xCenter * 2) - 155;
        Log.d("  xStart  ", String.valueOf(xStart)+"  xEnd  "+ String.valueOf(xEnd));

        yStart = 10;
        yEnd = (yCenter * 2) - 155;
        Log.d("  yStart  ", String.valueOf(yStart)+"  yEnd  "+ String.valueOf(yEnd));

        xLeftCenter = xCenter - centerDiff;
        xRightCenter = xCenter + centerDiff;
        yTopCenter = yCenter - centerDiff;
        yBottomCenter = yCenter + centerDiff;

        Log.d("  xLeftCenter  ", String.valueOf(xLeftCenter)+"  xRightCenter  "+ String.valueOf(xRightCenter)+
                "  yTopCenter "+ String.valueOf(yTopCenter)+"  yBottomCenter "+ String.valueOf(yBottomCenter));
    }

    private int[] randomXY(Random ran, LinkedList<Integer> listX,
                           LinkedList<Integer> listY, int xItem)
    {
        int[] arr = new int[4];
        arr[IDX_X] = listX.remove(ran.nextInt(listX.size()));
        arr[IDX_Y] = listY.remove(ran.nextInt(listY.size()));
        return arr;
    }


    private boolean isXMixed(int startA, int endA, int startB, int endB)
    {
        boolean result = false;
        if (startB >= startA && startB <= endA)
        {
            result = true;
        }
        else if (endB >= startA && endB <= endA)
        {
            result = true;
        }
        else if (startA >= startB && startA <= endB)
        {
            result = true;
        }
        else if (endA >= startB && endA <= endB)
        {
            result = true;
        }
        return result;
    }

}
