/*
 * Copyright 2011 Robert Theis
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
package edu.sfsu.cs.orange.ocr.language;

import edu.sfsu.cs.orange.ocr.CaptureActivity;
import edu.sfsu.cs.orange.ocr.R;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Class to perform translations in the background.
 */
public final class TranslateAsyncTask extends AsyncTask<String, String, Boolean> {

    private static final String TAG = TranslateAsyncTask.class.getSimpleName();

    private CaptureActivity activity;
    private TextView ntv;
    private View progressView;
    private TextView targetLanguageTextView;
    private String sourceLanguageCode;
    private String targetLanguageCode;
    private String sourceText;
    private String translatedText = "";

    public TranslateAsyncTask(CaptureActivity activity, String sourceLanguageCode, String targetLanguageCode,
                              String sourceText) {
        this.activity = activity;
        this.sourceLanguageCode = sourceLanguageCode;
        this.targetLanguageCode = targetLanguageCode;
        this.sourceText = sourceText;

        ntv = (TextView) activity.findViewById(R.id.status_view_top);
        progressView = (View) activity.findViewById(R.id.indeterminate_progress_indicator_view);
        targetLanguageTextView = (TextView) activity.findViewById(R.id.translation_language_text_view);
    }

    @Override
    protected Boolean doInBackground(String... arg0) {
        translatedText = Translator.translate(activity, sourceLanguageCode, targetLanguageCode, sourceText);

        // Check for failed translations.
        if (translatedText.equals(Translator.BAD_TRANSLATION_MSG)) {
            return false;
        }

        return true;
    }

    @Override
    protected synchronized void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            //Log.i(TAG, "SUCCESS");
            if (targetLanguageTextView != null) {
                targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
            }
            ntv.setText(translatedText);
            ntv.setVisibility(View.VISIBLE);


        } else {
            Log.e(TAG, "FAILURE");
            targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
            targetLanguageTextView.setText("Unavailable");

        }

        // Turn off the indeterminate progress indicator
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
    }
}
