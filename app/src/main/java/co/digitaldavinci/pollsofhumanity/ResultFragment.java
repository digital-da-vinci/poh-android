package co.digitaldavinci.pollsofhumanity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.text.DecimalFormat;

import co.digitaldavinci.pollsofhumanity.R;

import co.digitaldavinci.pollsofhumanity.server.GetResults;
import co.digitaldavinci.pollsofhumanity.server.holder.ResultsHolder;
import co.digitaldavinci.pollsofhumanity.server.listener.GetResultsListener;

/**
 * Created by ameya on 10/25/15.
 */
public class ResultFragment extends Fragment {
    private ManageSharedPref manageSharedPref;
    private View view;
    private Dialog loadingDialog;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        manageSharedPref = new ManageSharedPref(getActivity().getApplicationContext());

        loadingDialog = new Dialog(getActivity());
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        view = inflater.inflate(R.layout.fragment_result, container, false);
        ((BaseActivity) getActivity()).setActionBarButtonToHome();

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");
        ((TextView) view.findViewById(R.id.no_result)).setTypeface(font);

        int resultId = manageSharedPref.getResultsId();

        if(resultId != -1){
            System.out.println("resultId\t" + resultId);

            ((TextView) loadingDialog.findViewById(R.id.action)).setText("Getting results");
            ((TextView) loadingDialog.findViewById(R.id.action)).setTypeface(font);
            loadingDialog.show();
            new GetResults(resultId, gRListener).execute();
            view.findViewById(R.id.no_result).setVisibility(View.GONE);
        }


        return view;
    }

    GetResultsListener gRListener = new GetResultsListener() {
        @Override
        public void onGetResultsComplete(ResultsHolder results) {
            ((PieChart) view.findViewById(R.id.pie_chart)).setNoCount(results.getNoCount());
            ((PieChart) view.findViewById(R.id.pie_chart)).setYesCount(results.getYesCount());

            DecimalFormat df = new DecimalFormat("#.##");

            double yesRatio = ((double) results.getYesCount()) / ((double) (results.getTotal()));

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");

            ((TextView) view.findViewById(R.id.yes_count)).setText("Yes " + df.format(yesRatio * 100) + "%");
            ((TextView) view.findViewById(R.id.yes_count)).setTypeface(font);

            ((TextView) view.findViewById(R.id.no_count)).setText("No " + df.format((1 - yesRatio) * 100) + "%");
            ((TextView) view.findViewById(R.id.no_count)).setTypeface(font);

            ((TextView) view.findViewById(R.id.question)).setText(results.getQuestion());
            ((TextView) view.findViewById(R.id.question)).setTypeface(font);

            view.findViewById(R.id.results_layout).setVisibility(View.VISIBLE);

            loadingDialog.cancel();
        }
    };
}
