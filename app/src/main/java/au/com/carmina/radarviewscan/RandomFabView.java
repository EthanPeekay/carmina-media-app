package au.com.carmina.radarviewscan;


import android.annotation.TargetApi;
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

public class RandomFabView extends FrameLayout
        implements
        ViewTreeObserver.OnGlobalLayoutListener
{

    private static final String tag = RandomFabView.class.getSimpleName();

    private static final int MAX = 10;
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
    private HashMap<Integer, RipplePosition> ripplePositions = new HashMap<>();
    private ArrayList<Integer> lockedPosition = new ArrayList<>();
    private boolean receiver;
    private int counter;

    public class RipplePosition
    {
        int xpos =0;
        int ypos =0;
        boolean occupied = false;
    }

    public boolean isReceiver() {
        return receiver;
    }

    public void setReceiver(boolean receiver) {
        this.receiver = receiver;
    }

   /* public interface OnRippleViewClickListener
    {
        void onRippleViewClicked(RippleModel rippleModel);
    }*/

  //  private OnRippleViewClickListener onRippleOutViewClickListener;

    public RandomFabView(Context context)
    {
        super(context);
        init(null, context);
    }

    public RandomFabView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs, context);
    }

    public RandomFabView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs, context);
    }

    @TargetApi(21)
    public RandomFabView(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, context);
    }

   /* public void setMode(int mode)
    {
        this.mode = mode;
    }

    public void setOnRippleViewClickListener(OnRippleViewClickListener listener)
    {
        this.onRippleOutViewClickListener = listener;
    }*/

    public void clear()
    {
      //  myImageList.clear();
        ripplePositions.clear();
        lockedPosition.clear();
        deviceCnt = 0;
    }

   /* public void addImage(RippleModel rippleModel)
    {
        if (myImageList.size() < MAX) {
            if (!myImageList.contains(rippleModel)) {
                myImageList.add(rippleModel);
                //Log.d("My resid ", String.valueOf(rippleModel));
            }
        }
    }*/

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
       // myImageList = new Vector<RippleModel>(MAX);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        deviceCnt = 0;
        calcGridPositions();
        /*LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.ticklayout, null);
        this.addView(view);*/
    }


    @Override
    public void onGlobalLayout()
    {
        int tmpW = getWidth();
        int tmpH = getHeight();
        if (width != tmpW || height != tmpH)
        {
            width = tmpW;
            height = tmpH - 50;
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
        return bssid;
    }
    public synchronized void show(String bssid) {
        //this.removeAllViews();
   /* if(deviceCnt<myImageList.size())
    {
        if (width > 0 && height > 0 && myImageList != null && myImageList.size() > 0) {
            int xCenter = width >> 1;
            int yCenter = height >> 1;

            int size = myImageList.size();

            int xItem = width / (size + 1);
            int yItem = height / (size + 1);
            LinkedList<Integer> listX = new LinkedList<>();
            LinkedList<Integer> listY = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                listX.add(i * xItem);
                listY.add(i * yItem + (yItem >> 2));
            }
            LinkedList<RippleView> listRippleTop = new LinkedList<>();
            LinkedList<RippleView> listRippleBottom = new LinkedList<>();

            LinkedList<View> listImgTop = new LinkedList<>();
            LinkedList<View> listImgBottom = new LinkedList<>();

            *//*for (int i = 0; i < size; i++)
            {*//*
            final int j = deviceCnt;//i;
            int xy[] = randomXY(random, listX, listY, xItem);

            LayoutInflater li = LayoutInflater.from(getContext());
            final View view = li.inflate(R.layout.ripplefabtick, null);
                *//*final RippleView rippleView = new RippleView(getContext());
                rippleView.setColor(Color.CYAN);*//*

            if (isReceiver()) {
                for (int i = 0; i < myImageList.size(); i++) {
                    RippleModel rippleModel = myImageList.get(i);
                    Log.d("List display ", rippleModel.getIp() + "  " + rippleModel.getName());

                    //userImageView.setPlaceholderImage(ContextCompat.getDrawable(getContext(), AvatarAdapter.imageId[myImageList.get(j).getDrawable()]));
                               *//*FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabsd);*//*
                               *//*fab.setBackgroundTintList(ColorStateList.valueOf(mytb));*//*

                    final CircularTextView clickView = (CircularTextView) view.findViewById(R.id.clickView);
                    int mytb = rippleModel.getDrawable();
                    if (mytb == 0)
                        mytb = Util.getRandomColor();
                    String hexColor = String.format("#%06X", (0xFFFFFF & mytb));

                    clickView.setStrokeWidth(1);
                    //clickView.setStrokeColor(hexColor);
                    clickView.setStrokeColor("#FFFFFF");
                    clickView.setTextColor(Color.WHITE);
                    clickView.setTypeface(Util.fontSimplonBP_Regular);
                    Log.d("Color", "Color - " + hexColor + " mytb - " + mytb);
                    clickView.setSolidColor(hexColor);
                    Util.setHexColor = hexColor;
                    //RippleView innerRippleView = (RippleView) view.findViewById(R.id.innerRippleView);
                    TextView clientTxt = (TextView) view.findViewById(R.id.clientNameTxt);
                    clientTxt.setText(rippleModel.getName());
                    clientTxt.setTextColor(Util.WHITE);
                    clientTxt.setVisibility(View.VISIBLE);
                    final LinearLayout circleBlackView = (LinearLayout) view.findViewById(R.id.circleBlackView);
                    circleBlackView.setVisibility(GONE);
                            *//*innerRippleView.setColor(mytb);
                            innerRippleView.startRippleAnimation();
                            innerRippleView.setVisibility(GONE);*//*

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        clickView.setBackgroundTintList(ColorStateList.valueOf(mytb));
                    }

                    TextView fabsdtext = (TextView) view.findViewById(R.id.fabsdtext);
                    //fabsdtext.setTextColor(Util.WHITE);
                    clickView.setTextColor(Util.WHITE);

                    Log.d("User name------", myImageList.get(j).getName() + "  ...  " + j);
                    String usertitle = myImageList.get(j).getName();

                    if (usertitle.length() == 0)
                        clickView.setText("");
                        //fabsdtext.setText("");
                    else if (usertitle.length() > 1) {
                        String text = "" + usertitle.charAt(0) + usertitle.charAt(1);
                        String string = usertitle.toString().trim();
                        if (string.indexOf(" ") > -1) {
                            String[] split = string.split(" ");
                            int len = split.length;
                            if (split.length > 1) {
                                if (split[1].trim().length() > 0) {
                                    text = "" + usertitle.charAt(0) + split[1].trim().charAt(0);
                                }
                            }
                        }
                        //fabsdtext.setText(text.toUpperCase());
                        clickView.setText(text.toUpperCase());
                    } else if (usertitle.charAt(0) != ' ') {
                        //fabsdtext.setText(usertitle.toUpperCase());
                        clickView.setText(usertitle.toUpperCase());
                    }
                    clickView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onRippleOutViewClickListener != null)
                                onRippleOutViewClickListener.onRippleViewClicked((RippleModel) (view.getTag(R.id.action_search)));
                            //rippleView.setColor(Color.GREEN);
                            Log.d("User name------", myImageList.get(j).getIp() + "  ...  " + j);
                        }
                    });
                }
            } else {
                myImageList.get(deviceCnt).setListener(new RippleModel.Update() {
                    @Override
                    public void onUpdate(String bssid) {
                        //if (bssid.equalsIgnoreCase(getBssid()))
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //userImageView.setPlaceholderImage(ContextCompat.getDrawable(getContext(), AvatarAdapter.imageId[myImageList.get(j).getDrawable()]));
                               *//*FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabsd);*//*
                               *//*fab.setBackgroundTintList(ColorStateList.valueOf(mytb));*//*

                                    final CircularTextView clickView = (CircularTextView) view.findViewById(R.id.clickView);
                                    int mytb = myImageList.get(j).getDrawable();
                                    clickView.setStrokeWidth(2);
                                    clickView.setStrokeColor("#FFFFFF");
                                    clickView.setTextColor(Color.WHITE);
                                    clickView.setTypeface(Util.fontSimplonBP_Regular);
                                    String hexColor = String.format("#%06X", (0xFFFFFF & mytb));
                                    Log.d("Color", "Color - " + hexColor + " mytb - " + mytb);
                                    clickView.setSolidColor(hexColor);

                                    //RippleView innerRippleView = (RippleView) view.findViewById(R.id.innerRippleView);
                                    TextView clientTxt = (TextView) view.findViewById(R.id.clientNameTxt);
                                    clientTxt.setText(myImageList.get(j).getName());
                                    clientTxt.setTextColor(Util.WHITE);
                                    clientTxt.setVisibility(View.VISIBLE);
                                    final LinearLayout circleBlackView = (LinearLayout) view.findViewById(R.id.circleBlackView);
                                    circleBlackView.setVisibility(GONE);
                            *//*innerRippleView.setColor(mytb);
                            innerRippleView.startRippleAnimation();
                            innerRippleView.setVisibility(GONE);*//*

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        clickView.setBackgroundTintList(ColorStateList.valueOf(mytb));
                                    }

                                    TextView fabsdtext = (TextView) view.findViewById(R.id.fabsdtext);
                                    //fabsdtext.setTextColor(Util.WHITE);
                                    clickView.setTextColor(Util.WHITE);

                                    Log.d("User name------", myImageList.get(j).getName() + "  ...  " + j);
                                    String usertitle = myImageList.get(j).getName();

                                    if (usertitle.length() == 0)
                                        clickView.setText("");
                                        //fabsdtext.setText("");
                                    else if (usertitle.length() > 1) {
                                        String text = "" + usertitle.charAt(0) + usertitle.charAt(1);
                                        String string = usertitle.toString().trim();
                                        if (string.indexOf(" ") > -1) {
                                            String[] split = string.split(" ");
                                            int len = split.length;
                                            if (split.length > 1) {
                                                if (split[1].trim().length() > 0) {
                                                    text = "" + usertitle.charAt(0) + split[1].trim().charAt(0);
                                                }
                                            }
                                        }
                                        //fabsdtext.setText(text.toUpperCase());
                                        clickView.setText(text.toUpperCase());
                                    } else if (usertitle.charAt(0) != ' ') {
                                        //fabsdtext.setText(usertitle.toUpperCase());
                                        clickView.setText(usertitle.toUpperCase());
                                    }
                                    clickView.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (onRippleOutViewClickListener != null)
                                                onRippleOutViewClickListener.onRippleViewClicked((RippleModel) (view.getTag(R.id.action_search)));
                                            //rippleView.setColor(Color.GREEN);
                                            Log.d("User name------", myImageList.get(j).getIp() + "  ...  " + j);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }

            //Log.e("ImageList..", String.valueOf(myImageList.get(deviceCnt)));
            final RippleModel model = myImageList.get(deviceCnt);
            view.setTag(R.id.action_search, model);
                *//*rippleView.setTag(R.id.action_search,model);
                rippleView.startRippleAnimation();
                rippleView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onRippleOutViewClickListener!=null)
                         onRippleOutViewClickListener.onRippleViewClicked((RippleModel) (view.getTag(R.id.action_search)));
                        //rippleView.setColor(Color.GREEN);
                        Log.d("User name------",myImageList.get(j).getIp()+"  ...  "+j);
                    }
                });*//*

                *//*int strWidth = rippleView.getMeasuredWidth();

                xy[IDX_RIPPLE_LENGTH] = strWidth;

                if (xy[IDX_X] + strWidth > width - (xItem*//**//* >> 1 *//**//*))
                {
                    int baseX = width - strWidth;

                    xy[IDX_X] = baseX - xItem + random.nextInt(xItem >> 1);
                }
                else if (xy[IDX_X] == 0)
                {

                    xy[IDX_X] = Math.max(random.nextInt(xItem), xItem / 3);
                }

                xy[IDX_DIS_Y] = Math.abs(xy[IDX_Y] - yCenter);

                rippleView.setTag(xy);
                view.setTag(xy);

                if (xy[IDX_Y] > yCenter)
                {
                    listRippleBottom.add(rippleView);
                    listImgBottom.add(view);
                }
                else
                {
                    listRippleTop.add(rippleView);
                    listImgTop.add(view);
                }*//*

            *//*}*//*
            listImgBottom.add(view);
            deviceCnt += 1;
            //Log.d("Center ","X Center "+xCenter+" Y Center "+yCenter);
            //calculateDiff(xCenter, yCenter);
            //attach2Screen(listRippleTop,listImgTop, xCenter, yCenter, yItem,"top");
            attach2Screen(listRippleBottom, listImgBottom, xCenter, yCenter, yItem, "bottom");
        }
    }*/
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
                posObj.xpos = posX;
                posObj.ypos = posY;
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
        //return ripplePositions.get(pos);
        return ripplePositions.get(lockedPosition.get(lockedPosition.size()-1));
    }

    private int getRandomNum()
    {
        //return counter++;
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
        try {
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
        }
        catch(Exception e)
        {
            if(calcSeries == 0 || calcSeries == 1)
            {
                randomY = yTopCenter + new Random().nextInt(10);
            }
            else if(calcSeries == 2 || calcSeries == 3)
            {
                randomY = yBottomCenter - new Random().nextInt(10);
            }
        }



        return randomY;
    }

    private void calculateDiff(int xCenter, int yCenter)
    {
        Log.d("  xCenter  ", String.valueOf(xCenter)+"  yCenter  "+ String.valueOf(yCenter)+"  centerDiff "+ String.valueOf(centerDiff));

        xStart = 10;
        xEnd = (xCenter * 2) - 145;
        Log.d("  xStart  ", String.valueOf(xStart)+"  xEnd  "+ String.valueOf(xEnd));

        yStart = 10;
        yEnd = (yCenter * 2) - 145;
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
