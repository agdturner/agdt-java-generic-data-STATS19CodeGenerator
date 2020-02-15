/*
 * Copyright 2018 geoagdt.
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
package uk.ac.leeds.ccg.data.stats19.data;

import java.io.IOException;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Environment;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Object;

/**
 * STATS19_Data This is a skeleton class just for the sake of keeping the
 * STAT19_Environment file the same in both this code STATS19 code generation
 * project and the STATS19 data project.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_Data extends STATS19_Object {

    private static final long serialVersionUID = 1L;

    public STATS19_Data(STATS19_Environment se) {
        super(se);
    }

    public boolean clearSomeData() throws IOException {
        return false;
    }

    public int clearAllData() throws IOException {
        return 0;
    }
}
