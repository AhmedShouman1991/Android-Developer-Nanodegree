package com.Showman.apps.moviegallery.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MoviesFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> syncTask;
    @Override
    public boolean onStartJob(final JobParameters job) {

        syncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                MoviesSyncTasks.syncNewMovies(context);
                jobFinished(job, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(job, false);
            }
        }.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (syncTask != null) syncTask.cancel(true);
        return true;
    }
}
