/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jeroenmols.legofy;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * @author Jeroen Mols on 27/04/16.
 */
public class LegofyPicassoTransformation implements Transformation {

    public static final String LEGOFY = "legofy";
    private final Context applicationContext;

    public LegofyPicassoTransformation(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap legofiedBitmap = Legofy.with(applicationContext).convert(source);
        source.recycle();
        return legofiedBitmap;
    }

    @Override
    public String key() {
        return LEGOFY;
    }
}
