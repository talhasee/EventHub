package com.example.eventhub;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class CalendarAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private Context mContext;
    private ContentResolver mResolver;
    private OnCalendarTaskCompleted mListener;
    private boolean mIsDeleteOperation;
    private String mEventTitle;

    public CalendarAsyncTask(Context context, OnCalendarTaskCompleted listener) {
        mContext = context;
        mListener = listener;
        mResolver = context.getContentResolver();
    }

    public void setDeleteOperation(String eventTitle) {
        mIsDeleteOperation = true;
        mEventTitle = eventTitle;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        Utility utility = new Utility(mContext);
        if (mIsDeleteOperation) {
            utility.deleteEventByTitle(mContext, mEventTitle);
        }
        return utility.readCalendarEvent(mContext);
    }

    @Override
    protected void onPostExecute(ArrayList<String> eventList) {
        super.onPostExecute(eventList);
        mListener.onCalendarTaskCompleted(eventList);
    }

    public interface OnCalendarTaskCompleted {
        void onCalendarTaskCompleted(ArrayList<String> eventList);

        void onCalendarTaskCompleted(ArrayList<String> eventList, int position);
    }
}
