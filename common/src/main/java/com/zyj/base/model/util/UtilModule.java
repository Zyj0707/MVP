package com.zyj.base.model.util;

import android.content.Context;

import com.zyj.base.model.util.impl.CharacterParser;
import com.zyj.base.model.util.impl.PreferenceParser;
import com.zyj.base.model.util.impl.ResParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */
@Module
public class UtilModule {

    @Provides @Singleton
    public CharacterDelegate provideCharacterParser() {
        return new CharacterParser();
    }

    @Provides @Singleton
    public ResDelegate provideStringFetcher(Context context) {
        return new ResParser(context);
    }

    @Provides @Singleton
    public PreferenceDelegate providePreferenceParser(Context context) {
        return new PreferenceParser(context);
    }
}
