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

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    private final short ID;

    /**
     * The keys are CASEW1, the values are the respective combined record.
     */
    private final HashMap<Short, STATS19_Combined_Record> data;

    public STATS19_Collection(short ID) {
        this.ID = ID;
        data = new HashMap<>();
    }

    /**
     * @return the ID
     */
    public short getID() {
        return ID;
    }

    /**
     * @return the data
     */
    public HashMap<Short, STATS19_Combined_Record> getData() {
        return data;
    }

}
