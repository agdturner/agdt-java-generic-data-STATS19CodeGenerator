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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Environment;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Object;
import uk.ac.leeds.ccg.data.stats19.core.STATS19_Strings;
import uk.ac.leeds.ccg.generic.io.Generic_IO;

/**
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class STATS19_Data extends STATS19_Object {

    private static final long serialVersionUID = 1L;

    /**
     * The main STATS19 data store. Keys are Collection IDs.
     */
    public HashMap<Short, STATS19_Collection> data;

    /**
     * Looks up from a CASEW1 to the Collection ID.
     */
    public HashMap<Short, Short> CASEW1ToCID;

    public STATS19_Collection getCollection(short cid) throws IOException, ClassNotFoundException {
        STATS19_Collection r = data.get(cid);
        if (r == null) {
            r = (STATS19_Collection) loadSubsetCollection(cid);
            data.put(cid, r);
        }
        return r;
    }

    public void clearCollection(short cID) {
        data.put(cID, null);
    }

    public STATS19_Data(STATS19_Environment se) {
        super(se);
        data = new HashMap<>();
        CASEW1ToCID = new HashMap<>();
    }

    public boolean clearSomeData() throws IOException {
        Iterator<Short> ite = data.keySet().iterator();
        while (ite.hasNext()) {
            short cID = ite.next();
            STATS19_Collection c = data.get(cID);
            cacheSubsetCollection(cID, c);
            data.put(cID, null);
            return true;
        }
        return false;
    }

    public int clearAllData() throws IOException {
        int r = 0;
        Iterator<Short> ite = data.keySet().iterator();
        while (ite.hasNext()) {
            short cID = ite.next();
            STATS19_Collection c = data.get(cID);
            cacheSubsetCollection(cID, c);
            data.put(cID, null);
            r++;
        }
        return r;
    }

    /**
     *
     * @param cID the value of collectionID
     * @param o the value of o
     */
    public void cacheSubsetCollection(short cID, Object o) throws IOException {
        cache(getSubsetCollection(cID), o);
    }

    public Path getSubsetCollection(short cID) throws IOException {
        return Paths.get(env.files.getGeneratedSTATS19SubsetsDir().toString(),
                STATS19_Strings.s_STATS19 + STATS19_Strings.symbol_underscore
                + cID + STATS19_Strings.symbol_dot + STATS19_Strings.s_dat);
    }

    /**
     *
     * @param cID the value of collectionID
     * @return
     */
    public Object loadSubsetCollection(short cID) throws IOException, ClassNotFoundException {
        return load(getSubsetCollection(cID));
    }

    /**
     *
     * @param f the Path to load Object result from.
     * @return
     */
    protected Object load(Path f) throws IOException, ClassNotFoundException {
        String m = "load " + f.toString();
        env.logStartTag(m);
        Object r = Generic_IO.readObject(f);
        env.logEndTag(m);
        return r;
    }

    /**
     *
     * @param f the value of cf
     * @param o the value of o
     */
    protected void cache(Path f, Object o) throws IOException {
        String m = "cache " + f.toString();
        env.logStartTag(m);
        Generic_IO.writeObject(o, f);
        env.logEndTag(m);
    }

}
