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
public class STATS19_HHOLD_ID implements Serializable {

    private static final long serialVersionUID = 1L;

    private byte wave;
    private short CASEID;
    private short CASEW1;

    public STATS19_HHOLD_ID() {
    }

    public STATS19_HHOLD_ID(byte wave, short CASEID, short CASEW1) {
        this.wave = wave;
        this.CASEID = CASEID;
        this.CASEW1 = CASEW1;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof STATS19_HHOLD_ID) {
            STATS19_HHOLD_ID o;
            o = (STATS19_HHOLD_ID) obj;
            //if (hashCode() == o.hashCode()) {
            if (CASEID == o.CASEID) {
                if (CASEW1 == o.CASEW1) {
                    if (wave == o.wave) {
                        return true;
                    }
                }
//                    if (CASEW1 == null) {
//                        if (o.CASEW1 == null) {
//                            if (wave == o.wave) {
//                                return true;
//                            }
//                        }
//                    } else {
//                        if (o.CASEW1 != null) {
//                            if (CASEW1.shortValue() == o.CASEW1.shortValue()) {
//                                if (wave == o.wave) {
//                                    return true;
//                                }
//                            }
//                        }
//                    }
            }
            //}
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.wave;
        hash = 59 * hash + this.CASEID;
        hash = 59 * hash + this.CASEW1;
        return hash;
    }

    /**
     * @return the wave
     */
    public byte getWave() {
        return wave;
    }

    /**
     * @return the CASEID
     */
    public short getCASEID() {
        return CASEID;
    }

    /**
     * @return the CASEW1
     */
    public short getCASEW1() {
        return CASEW1;
    }
}
