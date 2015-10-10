/*
 * Copyright (C) 2012-2013 Reece H. Dunn
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

package com.reecedunn.espeak.test;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.test.AndroidTestCase;
import android.util.Log;

import com.reecedunn.espeak.TtsService;

import java.util.Locale;

import static com.reecedunn.espeak.test.TtsMatcher.isTtsLangCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TextToSpeechServiceTest extends AndroidTestCase
{
    public class TtsServiceTest extends TtsService
    {
        public TtsServiceTest(Context context)
        {
            attachBaseContext(context);
        }

        public String[] onGetLanguage() {
            return super.onGetLanguage();
        }

        public int onIsLanguageAvailable(String language, String country, String variant) {
            return super.onIsLanguageAvailable(language, country, variant);
        }

        public int onLoadLanguage(String language, String country, String variant) {
            return super.onLoadLanguage(language, country, variant);
        }
    }

    private TtsServiceTest mService = null;

    @Override
    public void setUp() throws Exception
    {
        mService = new TtsServiceTest(getContext());
        mService.onCreate();
    }

    @Override
    public void tearDown()
    {
        if (mService != null)
        {
            mService.onDestroy();
            mService = null;
        }
    }

    private void checkLanguage(String[] locale, String language, String country, String variant) {
        assertThat(locale.length, is(3));
        assertThat(locale[0], is(language));
        assertThat(locale[1], is(country));
        assertThat(locale[2], is(variant));
    }

    public void testOnLoadLanguage() {
        assertThat(mService.onLoadLanguage("eng", "", ""), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "", "");

        assertThat(mService.onLoadLanguage("eng", "USA", ""), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "USA", "");

        assertThat(mService.onLoadLanguage("eng", "GBR", "scotland"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "GBR", "scotland");

        assertThat(mService.onLoadLanguage("eng", "USA", "rp"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "USA", "");

        assertThat(mService.onLoadLanguage("eng", "", "scotland"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "", "");

        assertThat(mService.onLoadLanguage("eng", "FRA", "rp"), isTtsLangCode(TextToSpeech.LANG_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "", "");

        assertThat(mService.onLoadLanguage("eng", "FRA", ""), isTtsLangCode(TextToSpeech.LANG_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "eng", "", "");

        assertThat(mService.onLoadLanguage("ine", "", ""), isTtsLangCode(TextToSpeech.LANG_NOT_SUPPORTED));
        checkLanguage(mService.onGetLanguage(), "eng", "", "");
    }

    public void testOnIsLanguageAvailable() {
        assertThat(mService.onLoadLanguage("vie", "VNM", "saigon"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));

        assertThat(mService.onIsLanguageAvailable("eng", "", ""), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "USA", ""), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "GBR", "scotland"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "USA", "rp"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "", "scotland"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "FRA", "rp"), isTtsLangCode(TextToSpeech.LANG_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("eng", "FRA", ""), isTtsLangCode(TextToSpeech.LANG_AVAILABLE));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onIsLanguageAvailable("ine", "", ""), isTtsLangCode(TextToSpeech.LANG_NOT_SUPPORTED));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");
    }

    public void testOnGetDefaultVoiceNameFor() {
        assertThat(mService.onLoadLanguage("vie", "VNM", "saigon"), isTtsLangCode(TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE));

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "", ""), is("en"));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "USA", ""), is("en-us"));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "GBR", "scotland"), is("en-sc"));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "USA", "rp"), is("en-us"));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "", "scotland"), is("en"));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "FRA", "rp"), is("en-uk-rp")); // INCORRECT BEHAVIOUR
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("eng", "FRA", ""), is("en-uk-rp")); // INCORRECT BEHAVIOUR
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");

        assertThat(mService.onGetDefaultVoiceNameFor("ine", "", ""), is(nullValue()));
        checkLanguage(mService.onGetLanguage(), "vie", "VNM", "saigon");
    }
}