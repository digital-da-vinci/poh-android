package pollsofhumanity.hardikar.com.pollsofhumanity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestion;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.GetQuestionListener;
import pollsofhumanity.hardikar.com.pollsofhumanity.server.QuestionHolder;

/**
 * Created by ameya on 9/6/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private ManageSharedPref manageSharedPref;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Alarm received this time");
        manageSharedPref = new ManageSharedPref(context);
        new GetQuestion(context, gQListener).execute();

        System.out.println(manageSharedPref.getQuestion());
    }

    GetQuestionListener gQListener = new GetQuestionListener() {
        @Override
        public void onGetQuestionComplete(QuestionHolder question) {
            System.out.println("Got question");

            manageSharedPref.setQuestionExists(true);
            manageSharedPref.setIsQuestionAnswered(false);
            manageSharedPref.setUpdated(true);

            manageSharedPref.setQuestion(question.getQuestion());
            manageSharedPref.setId(question.getId());
           //manageSharedPref.updateQuestion(question.getQuestion(), question.getId());

        }
    };
}
