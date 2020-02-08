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

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_ID implements Comparable, Serializable {

    private static final long serialVersionUID = 1L;

    private final long ID;

    public STATS19_ID(long l) {
        ID = l;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof STATS19_ID) {
            STATS19_ID o2 = (STATS19_ID) o;
            if (ID > o2.ID) {
                return 1;
            } else {
                if (ID < o2.ID) {
                    return -1;
                }
                return 0;
            }
        }
        return -2;
    }

    /**
     * @return the ID
     */
    public long getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "ID " + ID;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof STATS19_ID) {
            STATS19_ID o2;
            o2 = (STATS19_ID) o;
            if (ID == o2.ID) {
                    return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (int) (this.ID ^ (this.ID >>> 32));
        return hash;
    }

}
