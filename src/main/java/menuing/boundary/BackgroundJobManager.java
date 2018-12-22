package menuing.boundary;

import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class BackgroundJobManager {

    @Schedule(hour="0", minute="0", second="0", persistent=false)
    public void dailyJob() {
        // Do your job here which should run every start of day.
    }
} 